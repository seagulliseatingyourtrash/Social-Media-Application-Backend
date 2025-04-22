package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.exception.ErrorCode;
import com.example.socialmediaapplicationbackend.exception.SimpleSnsApplicationException;
import com.example.socialmediaapplicationbackend.model.User;
import com.example.socialmediaapplicationbackend.model.entity.UserEntity;
import com.example.socialmediaapplicationbackend.repository.AlarmEntityRepository;
import com.example.socialmediaapplicationbackend.repository.UserCacheRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        return redisRepository.getUser(userName).orElseGet(
                () -> userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                        () -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName))
                ));
    }

    @Transactional
    public User join(String userName, String password) {
        // check the userId not exist
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });

        UserEntity savedUser = userRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(savedUser);
    }

    public String login(String userName, String password) {
        User savedUser = loadUserByUsername(userName);
        redisRepository.setUser(savedUser);
        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
    }


}
