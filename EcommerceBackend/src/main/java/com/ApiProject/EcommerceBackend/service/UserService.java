package com.ApiProject.EcommerceBackend.service;

import com.ApiProject.EcommerceBackend.api.model.LoginBody;
import com.ApiProject.EcommerceBackend.api.model.PasswordResetBody;
import com.ApiProject.EcommerceBackend.api.model.RegistrationBody;
import com.ApiProject.EcommerceBackend.exception.EmailFailureException;
import com.ApiProject.EcommerceBackend.exception.EmailNotFoundException;
import com.ApiProject.EcommerceBackend.exception.UserAlreadyExistsException;
import com.ApiProject.EcommerceBackend.exception.UserNotVerifiedException;
import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.VerificationToken;
import com.ApiProject.EcommerceBackend.model.dao.LocalUserDAO;
import com.ApiProject.EcommerceBackend.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private VerificationTokenDAO verificationTokenDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;

    public UserService(LocalUserDAO localUserDAO, VerificationTokenDAO verificationTokenDAO, EncryptionService encryptionService, JWTService jwtService, EmailService emailService) {
        this.localUserDAO = localUserDAO;
        this.verificationTokenDAO = verificationTokenDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {
        if(localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        LocalUser user =new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstName());
        user.setLastname(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken=createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
//        verificationTokenDAO.save(verificationToken);
        return localUserDAO.save(user);
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser =localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if(opUser.isPresent()){
            LocalUser user=opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                if(user.isEmailVerified()) {
                    return jwtService.generateJWT(user);
                }else{
                    List<VerificationToken> verificationTokens =user.getVerificationTokens();
                    boolean resend=verificationTokens.size()==0 ||verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis()-(60*60*1000)));
                    if(resend){
                        VerificationToken verificationToken=createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }

                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }
    @Transactional
    public boolean verifyUser(String token){
        Optional<VerificationToken> opToken=verificationTokenDAO.findByToken(token);
        if(opToken.isPresent()){
            VerificationToken verificationToken=opToken.get();
            LocalUser user =verificationToken.getUser();
            if(!user.isEmailVerified()){
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            String token = jwtService.generatePasswordResetJWT(user);
            emailService.sendPasswordResetEmail(user, token);
        } else {
            throw new EmailNotFoundException();
        }
    }


    public void resetPassword(PasswordResetBody body) {
        String email = jwtService.getResetPasswordEmail(body.getToken());
        Optional<LocalUser> opUser = localUserDAO.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            user.setPassword(encryptionService.encryptPassword(body.getPassword()));
            localUserDAO.save(user);
        }
    }

}
