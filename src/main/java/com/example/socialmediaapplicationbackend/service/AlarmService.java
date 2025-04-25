package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.model.AlarmArgs;
import com.example.socialmediaapplicationbackend.model.AlarmType;
import com.example.socialmediaapplicationbackend.model.entity.UserEntity;
import com.example.socialmediaapplicationbackend.repository.AlarmEntityRepository;
import com.example.socialmediaapplicationbackend.repository.EmitterRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static String ALARM_NAME = "alarm";

    private final AlarmEntityRepository alarmEntityRepository;
    private final EmitterRepository emitterRepository;
    private final UserEntityRepository userEntityRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public void send(AlarmType type, AlarmArgs args, Integer receiverId) {
        UserEntity userEntity = userEntityRepository.findById(receiverId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity entity = AlarmEntity.of(type, args, userEntity);
        alarmEntityRepository.save(entity);
        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
                    try {
                        it.send(SseEmitter.event()
                                .id(entity.getId().toString())
                                .name(ALARM_NAME)
                                .data(new AlarmNoti()));
                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
                    }
                },
                () -> log.info("No emitter founded")
        );
    }

}
