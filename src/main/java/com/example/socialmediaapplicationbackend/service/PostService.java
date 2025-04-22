package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

}
