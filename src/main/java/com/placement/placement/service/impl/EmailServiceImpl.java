package com.placement.placement.service.impl;

import com.placement.placement.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    // ✅ Inject from properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String name) {
        log.info("Sending welcome email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // ✅ FIXED
            message.setTo(toEmail);
            message.setSubject("Welcome to Smart Placement System!");
            message.setText(
                    "Dear " + name + ",\n\n" +
                            "Welcome to the Smart Placement Management System!\n\n" +
                            "Your account has been created successfully.\n" +
                            "You can now:\n" +
                            "  → Browse companies\n" +
                            "  → Apply for jobs\n" +
                            "  → Track your applications\n" +
                            "  → View interview schedules\n\n" +
                            "Best of luck with your placements!\n\n" +
                            "Regards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}",
                    toEmail, e.getMessage());
        }
    }

    @Override
    public void sendApplicationEmail(String toEmail, String studentName,
                                     String companyName) {
        log.info("Sending application email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // ✅ FIXED
            message.setTo(toEmail);
            message.setSubject("Application Submitted — " + companyName);
            message.setText(
                    "Dear " + studentName + ",\n\n" +
                            "Your application has been submitted successfully!\n\n" +
                            "Company: " + companyName + "\n" +
                            "Status: APPLIED\n\n" +
                            "We will notify you about further updates.\n\n" +
                            "Best of luck!\n\nRegards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("Application email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send application email: {}",
                    e.getMessage());
        }
    }

    @Override
    public void sendInterviewEmail(String toEmail, String studentName,
                                   String companyName, String date,
                                   String time, String venue) {
        log.info("Sending interview email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // ✅ FIXED
            message.setTo(toEmail);
            message.setSubject("Interview Scheduled — " + companyName);
            message.setText(
                    "Dear " + studentName + ",\n\n" +
                            "Your interview has been scheduled!\n\n" +
                            "Company : " + companyName + "\n" +
                            "Date    : " + date + "\n" +
                            "Time    : " + time + "\n" +
                            "Venue   : " + (venue != null ? venue : "To be announced") + "\n\n" +
                            "Please be on time and carry all documents.\n\n" +
                            "Best of luck!\n\nRegards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("Interview email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send interview email: {}",
                    e.getMessage());
        }
    }

    @Override
    public void sendSelectionEmail(String toEmail, String studentName,
                                   String companyName, Double ctc) {
        log.info("Sending selection email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // ✅ FIXED
            message.setTo(toEmail);
            message.setSubject("Congratulations! Selected at " + companyName);
            message.setText(
                    "Dear " + studentName + ",\n\n" +
                            "Congratulations!\n\n" +
                            "You have been SELECTED at " + companyName + "!\n\n" +
                            "Package : " + ctc + " LPA\n\n" +
                            "Your offer letter will be shared shortly.\n\n" +
                            "Best wishes!\n\nRegards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("Selection email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send selection email: {}",
                    e.getMessage());
        }
    }

    @Override
    public void sendRejectionEmail(String toEmail, String studentName,
                                   String companyName) {
        log.info("Sending rejection email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // ✅ FIXED
            message.setTo(toEmail);
            message.setSubject("Update on Your Application — " + companyName);
            message.setText(
                    "Dear " + studentName + ",\n\n" +
                            "Thank you for applying to " + companyName + ".\n\n" +
                            "We regret to inform you that you have not been\n" +
                            "selected in this recruitment drive.\n\n" +
                            "Don't be discouraged! Keep applying!\n\n" +
                            "Best wishes!\n\nRegards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("Rejection email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send rejection email: {}",
                    e.getMessage());
        }
    }

    @Override
    public void sendOtpEmail(String toEmail, String otpCode) {
        log.info("Sending OTP password reset email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("PMS Portal Password Reset OTP");
            message.setText(
                    "Hello,\n\n" +
                            "A password reset request was initiated for your Placement Management System account.\n\n" +
                            "Your verification OTP code is: " + otpCode + "\n\n" +
                            "This code is valid for 10 minutes. If you did not request this, please ignore this email.\n\n" +
                            "Regards,\nSmart Placement Team"
            );
            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to dispatch email. Please verify your SMTP settings.");
        }
    }
}