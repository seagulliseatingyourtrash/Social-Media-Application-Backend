package com.example.socialmediaapplicationbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
}
