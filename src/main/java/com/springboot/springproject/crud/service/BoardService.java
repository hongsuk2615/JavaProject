package com.springboot.springproject.crud.service;

import com.springboot.springproject.common.utils.Util;
import com.springboot.springproject.crud.dto.BoardDto;
import com.springboot.springproject.crud.dto.ReplyDto;
import com.springboot.springproject.crud.model.Board;
import com.springboot.springproject.crud.model.Reply;
import com.springboot.springproject.crud.repository.BoardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public ResponseEntity<List<BoardDto>> findAll() {
        List<BoardDto> dtoList = boardRepository.findByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .peek(dto -> dto.setAuthorEmail(Util.emailMask(dto.getAuthorEmail())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // 단일 게시글 DTO 반환
    public ResponseEntity<BoardDto> findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (board.getDeletedAt() != null) {
            throw new RuntimeException("삭제된 게시물입니다.");
        }

        BoardDto dto = toDto(board);

        dto.setAuthorEmail(Util.emailMask(dto.getAuthorEmail()));

        return ResponseEntity.ok(dto);
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .authorEmail(board.getAuthorEmail())
                .authorId(board.getAuthorId())
                .authorProvider(board.getAuthorProvider())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public Board findByIdEntity(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    public void deleteBoard(Long BoardId, String id) throws IllegalAccessException {
        Board board = boardRepository.findById(BoardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다"));

        if (!board.getAuthorId().equals(id)) {
            throw new IllegalAccessException("작성자만 댓글을 삭제할 수 있습니다.");
        }
        board.setDeletedAt(LocalDateTime.now());
        boardRepository.save(board);
    }
}
