package com.placement.placement.service;

public interface EmailService {
    void sendWelcomeEmail(String toEmail, String name);
    void sendApplicationEmail(String toEmail, String studentName,
                              String companyName);
    void sendInterviewEmail(String toEmail, String studentName,
                            String companyName, String date,
                            String time, String venue);
    void sendSelectionEmail(String toEmail, String studentName,
                            String companyName, Double ctc);
    void sendRejectionEmail(String toEmail, String studentName,
                            String companyName);
    void sendOtpEmail(String toEmail, String otpCode); // ✅ Added this method
}