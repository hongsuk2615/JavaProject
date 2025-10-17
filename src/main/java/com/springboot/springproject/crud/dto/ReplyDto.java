package com.springboot.springproject.crud.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReplyDto {
    private Long id;
    private String content;
    private String authorEmail;
    private String authorProvider;
    private String authorId;
    private LocalDateTime createdAt;
    private List<ReplyDto> replies;
}
