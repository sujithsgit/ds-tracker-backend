package com.example.drtraker.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.drtraker.demo.dto.*;
import com.example.drtraker.demo.entity.*;
import com.example.drtraker.demo.repository.*;
import com.example.drtraker.demo.security.*;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken); // ✅ DB-ல save
        userRepository.save(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    // REGISTER → generate OTP and return it
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists";
        }
        String otp = otpService.generateOtp(request.getEmail());
        otpService.storeTempUser(request.getEmail(), request);
        String generatedOtp = emailService.sendOtpEmail(request.getEmail(), otp);
        return "OTP:" + generatedOtp;
    }

    // VERIFY OTP
    public String verifyOtp(OtpVerifyRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!isValid) {
            return "Invalid OTP";
        }
        RegisterRequest tempUser = (RegisterRequest) otpService.getTempUser(request.getEmail());
        if (tempUser == null) {
            return "Session expired. Register again";
        }
        User user = new User();
        user.setName(tempUser.getName());
        user.setEmail(tempUser.getEmail());
        user.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        userRepository.save(user);
        otpService.clear(request.getEmail());
        return "User registered successfully";
    }
}