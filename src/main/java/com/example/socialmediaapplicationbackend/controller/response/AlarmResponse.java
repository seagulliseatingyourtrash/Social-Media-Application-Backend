package com.example.socialmediaapplicationbackend.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getRemovedAt()
        );
    }
}
