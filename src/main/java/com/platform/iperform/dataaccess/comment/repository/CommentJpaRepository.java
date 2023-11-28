package com.platform.iperform.dataaccess.comment.repository;

import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, UUID> {
    Optional<List<CommentEntity>> findByParentId(UUID parentId);
}
