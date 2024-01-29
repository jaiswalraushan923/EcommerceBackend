package com.ApiProject.EcommerceBackend.service;


import com.ApiProject.EcommerceBackend.exception.EmailFailureException;
import com.ApiProject.EcommerceBackend.model.LocalUser;
import com.ApiProject.EcommerceBackend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${email.from}")
    private String fromAddress;
    @Value("${app.frontend.url}")
    private String url;
    private JavaMailSender javaMailSender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage makeMailMassage(){
        SimpleMailMessage simpleMailMessage =new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException{
        SimpleMailMessage message =makeMailMassage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("verify your email to activate your account");
        message.setText("please follow the link below to verify your email to activate your account. \n "+
                 url + "/auth/verify?token="+verificationToken.getToken());
        try{
            javaMailSender.send(message);
        }catch(MailException ex){
            throw new EmailFailureException();
        }
    }

    public void sendPasswordResetEmail(LocalUser user, String token) throws EmailFailureException {
        SimpleMailMessage message = makeMailMassage();
        message.setTo(user.getEmail());
        message.setSubject("Your password reset request link.");
        message.setText("You requested a password reset on our website. Please " +
                "find the link below to be able to reset your password.\n" + url +
                "/auth/reset?token=" + token);
        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }

}
