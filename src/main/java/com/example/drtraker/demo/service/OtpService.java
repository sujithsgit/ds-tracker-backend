package com.example.drtraker.demo.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OtpService {

    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, Long> otpExpiry = new HashMap<>();
    private Map<String, Object> tempUserStorage = new HashMap<>();

    private static final long EXPIRY_TIME = 5 * 60 * 1000; // 5 minutes

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + EXPIRY_TIME);

        return otp;
    }

    public void storeTempUser(String email, Object userData) {
        tempUserStorage.put(email, userData);
    }

    public Object getTempUser(String email) {
        return tempUserStorage.get(email);
    }

    public boolean verifyOtp(String email, String otp) {

        if (!otpStorage.containsKey(email)) return false;

        // ⏳ expiry check
        if (System.currentTimeMillis() > otpExpiry.get(email)) {
            clear(email);
            return false;
        }

        return otp.equals(otpStorage.get(email));
    }

    public void clear(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
        tempUserStorage.remove(email);
    }
}