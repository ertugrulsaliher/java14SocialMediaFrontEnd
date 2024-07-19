package com.muhammet.controller;

import com.muhammet.dto.request.CreateCommentSaveDTO;
import com.muhammet.dto.response.ResponseDto;
import com.muhammet.service.CommentService;
import com.muhammet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;


    @PostMapping("/create-comment")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> createComment(@RequestBody CreateCommentSaveDTO createCommentSaveDTO) {

        commentService.saveComment(createCommentSaveDTO);
        postService.updateCommentCount(createCommentSaveDTO.getPostId());
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).data(true).message("Comment Başarıyla Paylaşıldı").build());
    }




}
