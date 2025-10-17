package com.springboot.springproject.crud.repository;

import com.springboot.springproject.crud.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByBoardIdAndParentIsNullOrderByCreatedAtAsc(Long boardId);
}
