package org.example.librex;

import org.example.librex.email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EmailServiceTest {

    @MockitoBean
    JavaMailSender mailSender;

    @Autowired
    EmailService emailService;

    @Test
    void sends_mail_via_spring_context() {
        emailService.sendSimpleMessage("a@b.com", "Temat", "Treść");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        assertThat(captor.getValue().getTo()).containsExactly("a@b.com");
    }
}
