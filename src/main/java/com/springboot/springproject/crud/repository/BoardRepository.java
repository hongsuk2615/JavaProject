package com.springboot.springproject.crud.repository;

import com.springboot.springproject.crud.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<Board> findByIdAndDeletedAtIsNull(Long id);
}
