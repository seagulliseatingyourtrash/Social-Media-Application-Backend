package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.exception.ErrorCode;
import com.example.socialmediaapplicationbackend.exception.SimpleSnsApplicationException;
import com.example.socialmediaapplicationbackend.model.entity.PostEntity;
import com.example.socialmediaapplicationbackend.model.entity.UserEntity;
import com.example.socialmediaapplicationbackend.repository.PostEntityRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String userName, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

}
