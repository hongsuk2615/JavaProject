package com.springboot.springproject.crud.controller;

import com.springboot.springproject.crud.dto.ReplyDto;
import com.springboot.springproject.crud.model.Reply;
import com.springboot.springproject.crud.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/board/{boardId}/replies")
    public ResponseEntity<List<ReplyDto>> getReplies(@PathVariable Long boardId) {
        List<ReplyDto> replies = replyService.getRepliesByBoard(boardId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping("/board/{boardId}/replies")
    public ResponseEntity<?> createReply(
            @PathVariable Long boardId,
            @RequestBody ReplyRequest request
    ) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String authorId = (String) auth.getPrincipal();
        String[] parts = authorId.split("_");
        String email = parts[0];
        String provider = parts[1];

        replyService.createReply(boardId, email, provider, authorId, request.getContent(), request.getParentId());
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Long replyId
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String authorId = (String) auth.getPrincipal();

        try {
            replyService.deleteReply(replyId, authorId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    public static class ReplyRequest {
        private String content;
        private Long parentId;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
}
