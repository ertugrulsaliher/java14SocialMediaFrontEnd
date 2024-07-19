package com.muhammet.controller;

import com.muhammet.dto.request.CreatePostRequestDTO;
import com.muhammet.dto.response.PostListResponseDTO;
import com.muhammet.dto.response.ResponseDto;
import com.muhammet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;


    @PostMapping("/create-post")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<Boolean>> createPost(@RequestBody CreatePostRequestDTO createPostRequestDTO){
        postService.createPost(createPostRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().data(true).code(200).message("Post Paylaşıldı!").build());
    }


    @GetMapping("/get-post-list")
    @CrossOrigin("*")
    public  ResponseEntity<ResponseDto<List<PostListResponseDTO>>> getPostList(String token){
        return ResponseEntity.ok(ResponseDto.<List<PostListResponseDTO>>builder().message("Post Listesi getirildi").code(200).data(postService.getPostList(token)).build());
    }







}
