package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.CreateCommentSaveDTO;
import com.muhammet.dto.response.CommentListResponseDTO;
import com.muhammet.entity.Comment;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.CommentRepository;
import com.muhammet.repository.UserRepository;
import com.muhammet.view.VwUserAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    private final JwtManager jwtManager;

    public void saveComment(CreateCommentSaveDTO createCommentSaveDTO) {
        Long userId = jwtManager.getAuthId(createCommentSaveDTO.getToken()).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        commentRepository.save(Comment.builder()
                        .comment(createCommentSaveDTO.getComment())
                        .userId(userId)
                        .postId(createCommentSaveDTO.getPostId())
                        .date(System.currentTimeMillis())
                .build());

    }

    public List<CommentListResponseDTO> getCommentsByPostID(Long id) {
        List<Comment> commentList = commentRepository.findAllByPostId(id);
        List<CommentListResponseDTO> result = new ArrayList<>();
        commentList.forEach(comment -> {
            VwUserAvatar userAvatarAndUserName = userService.getUserAvatarAndUserName(comment.getUserId());
            result.add(CommentListResponseDTO.builder()
                            .comment(comment.getComment())
                            .date(comment.getDate())
                            .postId(comment.getPostId())
                            .userId(comment.getUserId())
                            .userAvatar(userAvatarAndUserName.getAvatar())
                            .userName(userAvatarAndUserName.getUserName())
                    .build());
        });

        return result;
    }
}
