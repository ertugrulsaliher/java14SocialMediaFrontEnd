package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.CreateFollowSaveDTO;
import com.muhammet.entity.Follow;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.FollowRepository;
import com.muhammet.utility.FollowState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final JwtManager jwtManager;


    public List<Long> findFollowedUserIds(Long userId){
        return  followRepository.findFollowedUserIdsByUserId(userId);
    }

    public void save(CreateFollowSaveDTO createFollowSaveDTO) {
        Long userId = jwtManager.getAuthId(createFollowSaveDTO.getToken()).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        followRepository.save(Follow.builder()
                        .userId(userId)
                        .followId(createFollowSaveDTO.getFollowId())
                        .state(FollowState.FOLLOWED)
                .build());

    }
}
