package com.springboot.springproject.crud.controller;

import com.springboot.springproject.crud.dto.BoardDto;
import com.springboot.springproject.crud.model.Board;
import com.springboot.springproject.crud.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> list() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> get(@PathVariable Long id) {
        return boardService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BoardDto> create(@RequestBody Board board) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        String authorId = (String) auth.getPrincipal();
        String[] parts = authorId.split("_");
        String email = parts[0];
        String provider = parts[1];

        board.setAuthorId(authorId);
        board.setAuthorEmail(email);
        board.setAuthorProvider(provider);

        Board saved = boardService.save(board);
        return ResponseEntity.ok(boardService.toDto(saved)); // DTO 반환
    }


    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<BoardDto> update(@PathVariable Long id,
                                           @RequestBody Board updated) {
        Board existing = boardService.findByIdEntity(id); // 엔티티 반환 메서드
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        String authorId = (String) auth.getPrincipal();

        if (!existing.getAuthorId().equals(authorId)) {
            return ResponseEntity.status(403).body(null);
        }

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        Board saved = boardService.save(existing);
        return ResponseEntity.ok(boardService.toDto(saved));
    }


    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> delete(@PathVariable Long boardId) {
        Board existing = boardService.findByIdEntity(boardId); // 엔티티 반환
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        String authorId = (String) auth.getPrincipal();

        if (!existing.getAuthorId().equals(authorId)) {
            return ResponseEntity.status(403).body("작성자만 삭제할 수 있습니다.");
        }
        try {
            boardService.deleteBoard(boardId, authorId);
            return ResponseEntity.ok("삭제되었습니다.");
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

    }

}