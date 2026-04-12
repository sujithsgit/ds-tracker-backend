package com.example.drtraker.demo.service;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public String sendOtpEmail(String toEmail, String otp) {
        System.out.println("OTP for " + toEmail + " : " + otp);
        return otp;
    }
}