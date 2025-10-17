package com.springboot.springproject.crud.service;

import com.springboot.springproject.common.utils.Util;
import com.springboot.springproject.crud.dto.ReplyDto;
import com.springboot.springproject.crud.model.Board;
import com.springboot.springproject.crud.model.Reply;
import com.springboot.springproject.crud.repository.BoardRepository;
import com.springboot.springproject.crud.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public List<ReplyDto> getRepliesByBoard(Long boardId) {
        List<Reply> replies = replyRepository.findByBoardIdAndParentIsNullOrderByCreatedAtAsc(boardId);

        return replies.stream()
                .map(this::convertToDtoWithSoftDelete)
                .toList();
    }

    public void createReply(Long boardId, String email, String provider, String authorId, String content, Long parentId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Reply reply = new Reply();
        reply.setBoard(board);
        reply.setAuthorEmail(email);
        reply.setAuthorProvider(provider);
        reply.setAuthorId(authorId);
        reply.setContent(content);

        if (parentId != null) {
            Reply parent = replyRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
            reply.setParent(parent);
        }

        replyRepository.save(reply);
    }

    public void deleteReply(Long replyId, String id) throws IllegalAccessException {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!reply.getAuthorId().equals(id)) {
            throw new IllegalAccessException("작성자만 댓글을 삭제할 수 있습니다.");
        }
        reply.setDeletedAt(LocalDateTime.now());
        replyRepository.save(reply);
    }


    private ReplyDto convertToDto(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .authorEmail(reply.getAuthorEmail())
                .createdAt(reply.getCreatedAt())
                .replies(reply.getReplies().stream()
                        .map(this::convertToDto)
                        .toList())
                .build();
    }

    private ReplyDto convertToDtoWithSoftDelete(Reply reply) {
        ReplyDto dto = new ReplyDto();
        dto.setId(reply.getId());
        dto.setAuthorEmail(Util.emailMask(reply.getAuthorEmail()));
        dto.setAuthorProvider(reply.getAuthorProvider());
        dto.setAuthorId(reply.getAuthorId());
        dto.setCreatedAt(reply.getCreatedAt());

        // soft delete 처리
        if (reply.getDeletedAt() != null) {
            dto.setContent("삭제된 댓글입니다.");
        } else {
            dto.setContent(reply.getContent());
        }

        // 자식 댓글 처리
        List<ReplyDto> childDtos = reply.getReplies().stream()
                .map(this::convertToDtoWithSoftDelete)
                .toList();
        dto.setReplies(childDtos);

        return dto;
    }
}
