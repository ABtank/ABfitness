package ru.abfitness.oapi.services;

import ru.abfitness.oapi.dto.EmailDto;

import javax.mail.MessagingException;
import java.security.Principal;

public interface EmailService {
    void sendSimpleMessage(String[] to, String subject, String text);
    void sendMessageWithAttachment(
            String[] to, String subject, String text, String pathToAttachment) throws MessagingException;
}
