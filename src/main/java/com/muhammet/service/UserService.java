package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.dto.response.SearchUserResponseDto;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.UserRepository;
import com.muhammet.view.VwUserAvatar;
import com.muhammet.view.VwUserForFollow;
import com.muhammet.view.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtManager jwtManager;
    private final UserRepository userRepository;
    private final FollowService followService;

    public User save(UserSaveRequestDto dto) {
        return repository.save(User.builder()
                        .password(dto.getPassword())
                        .email(dto.getEmail())
                        .userName(dto.getUserName())
                .build());
    }

    public Optional<User> login(UserLoginRequestDto dto) {
       return repository.findOptionalByUserNameAndPassword(dto.getUserName(),dto.getPassword());
    }

    public List<SearchUserResponseDto> search(String userName) {
        List<User> userList;
        List<SearchUserResponseDto> result = new ArrayList<>();
        if(Objects.isNull(userName))
            userList = repository.findAll();
        else
            userList = repository.findAllByUserNameContaining(userName);
        userList.forEach(u->
            result.add(SearchUserResponseDto.builder()
                            .userName(u.getUserName())
                            .avatar(u.getAvatar())
                            .email(u.getEmail())
                    .build())
        );
        return result;
    }
    public VwUserProfile getProfileByToken(String token) {
        Optional<Long> authId = jwtManager.getAuthId(token);
        if(authId.isEmpty()){
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
        return repository.getByAuthId(authId.get());
    }

    public void editProfile(User user) {
        repository.save(user);
    }

    public List<VwUserAvatar> getUserAvatarList() {
        return userRepository.getAllUserAvatar();
    }

    public List<User> findAllbyId(List<Long> ids) {
        return repository.findAllById(ids);
    }
    public HashMap<Long,User> findAllbyIdMap(List<Long> ids) { // olmadı
        return repository.findAllById(ids).stream()
                .collect(Collectors.toMap(
                        User::getId, // User nesnesinin id'sini almak için
                        user -> user, // User nesnesini kendisi olarak eklemek için
                        (existing, replacement) -> existing, // Çakışan id'ler varsa, mevcut değeri kullanmak için
                        HashMap::new // Sonuçta elde edilecek Map'in tipi
                ));
    }

    public Map<Long,User> findAllByIdMap(List<Long> ids) {
        List<User> userList = repository.findAllById(ids);
        Map<Long,User> result = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return result;
    }

    public VwUserAvatar getUserAvatarAndUserName(Long userId) {
        return repository.getUserAvatarAndUserName(userId);
    }

    public List<VwUserForFollow> getUserNotFollowed(String token){
        Long userId = jwtManager.getAuthId(token).orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN));
        List<VwUserForFollow> usersNotFollowed = userRepository.findUsersNotFollowedBy(followService.findFollowedUserIds(userId));


        usersNotFollowed.removeIf(vwUserForFollow -> vwUserForFollow.getId().equals(userId));

//        usersNotFollowed.forEach(vwUserForFollow -> {
//            if(vwUserForFollow.getId().equals(userId)){
//                usersNotFollowed.remove(vwUserForFollow);
//            }
//        });

        return usersNotFollowed;
    }



}
