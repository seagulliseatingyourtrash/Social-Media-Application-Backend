package com.example.socialmediaapplicationbackend.service;

import com.example.socialmediaapplicationbackend.exception.ErrorCode;
import com.example.socialmediaapplicationbackend.exception.SimpleSnsApplicationException;
import com.example.socialmediaapplicationbackend.fixture.TestInfoFixture;
import com.example.socialmediaapplicationbackend.fixture.UserEntityFixture;
import com.example.socialmediaapplicationbackend.model.entity.PostEntity;
import com.example.socialmediaapplicationbackend.model.entity.UserEntity;
import com.example.socialmediaapplicationbackend.repository.PostEntityRepository;
import com.example.socialmediaapplicationbackend.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PostEntityRepository postEntityRepository;

    @Test
    void should_create_post_successfully() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserName(), fixture.getPassword())));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));
    }

    @Test
    void should_throw_when_user_not_found_on_create() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_throw_when_post_not_found_on_modify() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_throw_when_user_not_found_on_modify() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_throw_when_user_is_not_post_owner_on_modify() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void should_throw_when_post_not_found_on_delete() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_throw_when_user_not_found_on_delete() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_throw_when_user_is_not_post_owner_on_delete() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void should_return_post_list_successfully() {
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void should_return_my_post_list_successfully() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Pageable pageable = mock(Pageable.class);

        when(postEntityRepository.findAllByUserId(any(), pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.my(fixture.getUserId(), pageable));
    }
    
    @Test
    void should_throw_when_user_not_found_on_get_my_posts() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> postService.my(fixture.getUserId(), mock(Pageable.class)));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


}
