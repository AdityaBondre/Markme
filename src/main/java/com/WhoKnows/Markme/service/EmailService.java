package com.WhoKnows.Markme.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendRegistrationEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🎉 Welcome to MarkMe - Your Attendance Partner! 🚀");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>Welcome to MarkMe, " + name + "!</h2>"
                    + "<p>Dear " + name + ",</p>"
                    + "<p>We are thrilled to have you onboard! 🎉 MarkMe is here to make attendance marking seamless for students and professors.</p>"
                    + "<p>With MarkMe, you can:</p>"
                    + "<ul>"
                    + "<li>Effortlessly track attendance.</li>"
                    + "<li>Stay updated with your session records.</li>"
                    + "<li>Experience a hassle-free attendance system.</li>"
                    + "</ul>"
                    + "<p><b>Get started now and explore the features of MarkMe!</b></p>"
                    + "<p>If you have any questions, feel free to reach out to our support team.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>MarkMe Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }

    public void emailForSuccessfullyMarkedAttendance(String to, String subjectName, String sectionName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Attendance Successfully Marked - " + subjectName);

            String emailContent = "<html><body>"
                    + "<h2 style='color: #2E86C1;'>Attendance Confirmation</h2>"
                    + "<p>Hello,</p>"
                    + "<p>Your attendance for <b>" + subjectName + "</b> in section <b>" + sectionName + "</b> has been successfully recorded.</p>"
                    + "<p>Thank you for your participation.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>MarkMe Team</b></p>"
                    + "<hr>"
                    + "<p style='font-size: 12px; color: gray;'>This is an automated email, please do not reply.</p>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }


}
