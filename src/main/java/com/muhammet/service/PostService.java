package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.CreatePostRequestDTO;
import com.muhammet.dto.response.CommentListResponseDTO;
import com.muhammet.dto.response.PostListResponseDTO;
import com.muhammet.entity.Post;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.PostRepository;
import com.muhammet.view.VwUserAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtManager jwtManager;
    private final UserService userService;
    private final CommentService commentService;

    public void updateCommentCount(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new AuthException(ErrorType.POST_NOT_FOUND));
        post.setCommentCount(post.getCommentCount() + 1L);
        postRepository.save(post);
    }

    public void createPost(CreatePostRequestDTO createPostRequestDTO) {
        Optional<Long> userId = jwtManager.getAuthId(createPostRequestDTO.getToken());
        if(userId.isEmpty()){
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        postRepository.save(Post.builder()
                        .comment(createPostRequestDTO.getComment())
                        .photo(createPostRequestDTO.getUrl())
                        .commentCount(0L)
                        .likeCount(0L)
                        .sharedDate(System.currentTimeMillis())
                        .userId(userId.get())
                .build());
    }

    public List<PostListResponseDTO> getPostList(String token) {
        Optional<Long> userId = jwtManager.getAuthId(token);
        if(userId.isEmpty()){
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        List<Post> postList = postRepository.findAll();

        List<Long> userIds = postList.stream().map(Post::getUserId).toList();

        Map<Long, User> mapUserList = userService.findAllByIdMap(userIds);

        List<PostListResponseDTO> result = new ArrayList<>();

        //List<VwUserAvatar> vwUserAvatarList = userService.getUserAvatarList(); //20K+ veri üzerinde yavaşlama yapar.

        postList.forEach(post->{
            //VwUserAvatar vwUserAvatar = vwUserAvatarList.stream().filter(Vw->Vw.getId().equals(post.getUserId())).findFirst().get(); filter maaliyetlidir!
            List<CommentListResponseDTO> commentList = commentService.getCommentsByPostID(post.getId());

            result.add(PostListResponseDTO.builder()
                    .id(post.getId())
                    .avatar(mapUserList.get(post.getUserId()).getAvatar())
                    .comment(post.getComment())
                    .commentCount(post.getCommentCount())
                    .likeCount(post.getLikeCount())
                    .sharedDate(post.getSharedDate())
                    .userID(post.getUserId())
                    .commentList(commentList)
                    .userName(mapUserList.get(post.getUserId()).getUserName())
                    .photo(post.getPhoto())
                    .build());

        });



        return result.stream().sorted(Comparator.comparing(PostListResponseDTO::getSharedDate)).toList().reversed();
    }
}
