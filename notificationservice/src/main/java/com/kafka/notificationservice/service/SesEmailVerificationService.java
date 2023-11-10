package com.kafka.notificationservice.service;

import com.kafka.notificationservice.response.Appointment;
import com.kafka.notificationservice.response.DoctorEntity;
import com.kafka.notificationservice.response.Prescription;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SesEmailVerificationService {

    private SesClient sesClient;

    @Autowired
    private FreeMarkerConfigurer configurer;

   @Value("${email.from}")
    private String fromEmail;
    private String accessKey;
    private String secretKey;


    @PostConstruct
    public void init() {
        // When you hit the endpoint to verify the email this needs to be the access key for your AWS account
        // When you hit the endpoint to send an email this value needs to be updated to the Smtp username that you generated
        accessKey = "AKIATJRIZDIJOGQECKLT";


        // When you hit the endpoint to verify the email this needs to be the secret key for your AWS account
        // When you hit the endpoint to send an email this value needs to be updated to the Smtp password that you generated
        secretKey = "KJhDpWn12tZolPBBseIh0vP0dmNL5eWDYm6Uqjc5";//
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretKey));
        sesClient = SesClient.builder()
                .credentialsProvider(staticCredentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    }

    public void verifyEmail(String emailId) {
        sesClient.verifyEmailAddress(req -> req.emailAddress(emailId));
    }

    public void sendEmail(DoctorEntity user) throws IOException, TemplateException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", user);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("userwelcome.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, templateModel);
        sendSimpleMessage(user.getEmailId(), "Welcome Email", htmlBody);
    }

    public void sendRejectionEmail(DoctorEntity user) throws IOException, TemplateException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", user);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("userrejection.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, templateModel);
        sendSimpleMessage(user.getEmailId(), "Rejection Email", htmlBody);
    }

    public void sendAppointmentEmail(Appointment appointment) throws IOException, TemplateException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("appointment", appointment);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("appointmentconfirmation.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, templateModel);
        sendSimpleMessage(appointment.getUserEmailId(), "Appointment Confirmation", htmlBody);
    }

    public void sendPrescriptionEmail(Prescription prescription) throws IOException, TemplateException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("prescription", prescription);
        templateModel.put("medicineList", prescription.getMedicineList());
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("prescription.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, templateModel);
        sendSimpleMessage(prescription.getEmailId(), "Prescription", htmlBody);
    }

    private void sendSimpleMessage(String toEmail, String subject, String body) {

        Content textBody = Content.builder()
                .data(body)
                .build();

        Body emailBody = Body.builder()
                .html(textBody)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .source(fromEmail)
                .destination(Destination.builder().toAddresses(toEmail).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(emailBody)
                        .build())
                .build();

        SendEmailResponse emailResponse = sesClient.sendEmail(emailRequest);
    }
}
