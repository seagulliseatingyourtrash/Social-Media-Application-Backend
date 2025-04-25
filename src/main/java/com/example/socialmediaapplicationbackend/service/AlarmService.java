package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.repository.AlarmEntityRepository;
import com.example.socialmediaapplicationbackend.repository.EmitterRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static String ALARM_NAME = "alarm";

    private final AlarmEntityRepository alarmEntityRepository;
    private final EmitterRepository emitterRepository;
    private final UserEntityRepository userEntityRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

}
