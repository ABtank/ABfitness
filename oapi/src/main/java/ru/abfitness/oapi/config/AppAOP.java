package ru.abfitness.oapi.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.abfitness.oapi.services.EmailService;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Aspect
@Configuration
public class AppAOP {
    @Value("${app.debug}")
    private String DEBUG;
    private EmailService emailService;


    @AfterThrowing(pointcut = "execution(* ru.abfitness.oapi.controllers..*.*(..))", throwing = "ex")
    private void throwingErrorInControllers(JoinPoint joinPoint, Exception ex) {
        sendError(joinPoint, ex);
    }

    @AfterThrowing(pointcut = "execution(* ru.abfitness.oapi.services..*.*(..))", throwing = "ex")
    private void throwingErrorInServices(JoinPoint joinPoint, Exception ex) {
        sendError(joinPoint, ex);
    }


    private void sendError(JoinPoint joinPoint, Exception ex) {
        String prefix = (Boolean.getBoolean(DEBUG)) ? "DEBUG" : "ERROR";
        log.error(prefix + " : " + joinPoint.toString());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        emailService.sendSimpleMessage(new String[]{"spprtscheduler@gmail.com"}
                , prefix + " [ABFitness] " + ex
                , sw.toString());
    }
}
