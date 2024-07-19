package com.muhammet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentListResponseDTO {
    private Long userId;
    private Long postId;
    private Long date;
    private String comment;
    private String userAvatar;
    private String userName;
}
