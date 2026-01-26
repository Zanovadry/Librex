package org.example.librex.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegisterMessage(String to, String username) throws MailException {
        String subject = "Rejestracja w Librex";
        String text = "Dziękujemy użytkowniku: " + username + " za zarejestrowanie się w serwisie Librex";

        sendSimpleMessage(to, subject, text);
    }

    public void sendBookAvaiableMessage(String to, String bookTitle) throws MailException {
        String subject = "Książka na którą czekasz jest dostępna!";
        String text = "Książka: " + bookTitle + " była zapisana w twojej waitliście i jest już dostępna do wypożyczenia";

        sendSimpleMessage(to, subject, text);
    }

    public void sendSimpleMessage(String to, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        //Wyświetla się i tak adres email
        message.setFrom("noreply@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        logger.info("Sent email to={} subject={}", to, subject);
    }
}
