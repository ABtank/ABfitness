package ru.abfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.abfitness.oapi.services.EmailService;

import javax.mail.MessagingException;

@SpringBootApplication
public class OapiApplication {
    //    Пример рассылки
    private EmailService emailService;
    @Autowired
    public void setEmailService(EmailService emailService) throws MessagingException {
        this.emailService = emailService;
//        emailService.sendSimpleMessage(new String[]{"spptrschedulerfreeapp@mail.ru","abtank@bk.ru"},"Заголовок","Какой-то там текст.");
//        emailService.sendMessageWithAttachment(new String[]{"schedulerfreeapp@gmail.com","abtank@bk.ru"},"Заголовок с файлом","Какой-то там текст.","oapi/src/main/resources/application.yaml");
    }

    public static void main(String[] args) {
        SpringApplication.run(OapiApplication.class, args);
    }

}
