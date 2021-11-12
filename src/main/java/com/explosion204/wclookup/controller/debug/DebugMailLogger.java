package com.explosion204.wclookup.controller.debug;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Component
@Profile("debug")
public class DebugMailLogger {
    private static final Logger logger = LogManager.getLogger();
    private static final String BUG_REPORT_SUBJECT = "wclookup [DEBUG]";
    private static final String BUG_REPORT_BODY = "Request: %s %s?%s, stacktrace: %s";

    private final JavaMailSender mailSender;

    @Value("${debug.mail.receiver}")
    private String debugMailReceiver;

    public DebugMailLogger(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void log(HttpServletRequest request, Throwable e) {
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryParams = request.getQueryString();
        String stackTrace = ExceptionUtils.getStackTrace(e);
        String body = String.format(BUG_REPORT_BODY, requestMethod, requestUri, queryParams, stackTrace);

        sendEmail(debugMailReceiver, BUG_REPORT_SUBJECT, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(subject);

            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Unable to send message", e);
        }
    }
}
