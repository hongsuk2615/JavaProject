package com.springboot.springproject.crud.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String authorEmail;
    private String authorId;
    private String authorProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
