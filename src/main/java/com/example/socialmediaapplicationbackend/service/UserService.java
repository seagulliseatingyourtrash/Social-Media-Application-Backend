package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.repository.AlarmEntityRepository;
import com.example.socialmediaapplicationbackend.repository.UserCacheRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository redisRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

}
