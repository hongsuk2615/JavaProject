package com.springboot.springproject.crud.controller;

import com.springboot.springproject.crud.model.Board;
import com.springboot.springproject.crud.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public List<Board> list() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public Board get(@PathVariable Long id) {
        return boardService.findById(id);
    }

    @PostMapping
    public Board create(@RequestBody Board board) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        System.out.println(auth);

        String authorId = (String) auth.getPrincipal();
        System.out.println(authorId);
        String[] parts = authorId.split("_");
        String email = parts[0];
        String provider = parts[1];

        board.setAuthorId(authorId);
        board.setAuthorEmail(email);
        board.setAuthorProvider(provider);

        return boardService.save(board);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public Board update(@PathVariable Long id,
                        @RequestBody Board updated,
                        @AuthenticationPrincipal OAuth2User user) {
        Board existing = boardService.findById(id);

        String email = (String) user.getAttributes().get("email");
        String provider = (String) user.getAttributes().get("provider");
        String authorId = provider + "_" + email;

        if (!existing.getAuthorId().equals(authorId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        return boardService.save(existing);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal OAuth2User user) {
        Board existing = boardService.findById(id);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String authorId = (String) auth.getPrincipal();

        if (!existing.getAuthorId().equals(authorId)) {
            return ResponseEntity
                    .status(403)
                    .body("작성자만 삭제할 수 있습니다.");
        }

        boardService.delete(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}