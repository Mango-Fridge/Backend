package com.mango.mango.domain.user.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mango.mango.domain.user.repository.UserRepository;
import com.mango.mango.domain.user.service.UserService;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.config.oauth.impl.AppleOAuthProvider;
import com.mango.mango.config.oauth.impl.KakaoOAuthProvider;
import com.mango.mango.domain.agreementLog.constant.AgreementType;
import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.agreementLog.repository.AgreementLogRepository;
import com.mango.mango.domain.groupMembers.repository.GroupMemberRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;
import com.mango.mango.domain.user.dto.response.UserResponseDto;
import com.mango.mango.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import com.mango.mango.global.error.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AgreementLogRepository agreementLogRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    private final KakaoOAuthProvider kakaoOAuthProvider;
    private final AppleOAuthProvider appleOAuthProvider;

    // private final PasswordEncoder passwordEncoder;

    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    
    @Override
    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {

        // 이메일 유효성 체크
        if (!requestDto.getEmail().matches(emailRegex)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 필수 약관 동의 체크
        if(!requestDto.getPrivacyAgreement().equals(true) ||
                !requestDto.getServiceAgreement().equals(true)){
            throw new CustomException(ErrorCode.REQUIRED_AGREEMENT);
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 회원 저장
        User user = userRepository.save(
            User.builder()
                .email(requestDto.getEmail())
                .username(requestDto.getUsername())
                .build()
        );

        return user.getId();
    }

    @Override
    @Transactional
    public UserLoginResponseDto login(UserLoginDto requestDto, String accessToken) {
        
        User oauthUser;

        if ("KAKAO".equalsIgnoreCase(requestDto.getOauthProvider())) {
            oauthUser = kakaoOAuthProvider.getUserInfo(accessToken);
        } else if("APPLE".equalsIgnoreCase(requestDto.getOauthProvider())){
            oauthUser = appleOAuthProvider.getUserInfo(accessToken);
        }else {
            throw new CustomException(ErrorCode.INVALID_OAUTH_PROVIDER);
        }

        Optional<User> optionalUser = userRepository.findByEmailAndOauthProvider(oauthUser.getEmail(), requestDto.getOauthProvider());

        User user;
        boolean isNewUser = false;

        if (optionalUser.isPresent()) {
            // 기존 유저 존재 -> 로그인 성공
            user = optionalUser.get();
        } else {
            // 신규 유저 생성
            isNewUser = true;
            user = User.builder()
                .email(oauthUser.getEmail())
                .username(oauthUser.getUsername())
                .oauthProvider(oauthUser.getOauthProvider())
                .build();
            
            user = userRepository.save(user);

            AgreementLog privacyPolicyLog = new AgreementLog(user, AgreementType.PRIVACY_POLICY.name(), false);
            AgreementLog termsOfServiceLog = new AgreementLog(user, AgreementType.TERMS_OF_SERVICE.name(), false);

            agreementLogRepository.save(privacyPolicyLog);
            agreementLogRepository.save(termsOfServiceLog);
        }

        boolean agreePrivacyPolicy = agreementLogRepository.existsByUserAndKindAndAgreeYn(user, AgreementType.PRIVACY_POLICY.name(), true);
        boolean agreeTermsOfService = agreementLogRepository.existsByUserAndKindAndAgreeYn(user, AgreementType.TERMS_OF_SERVICE.name(), true);

        return UserLoginResponseDto.builder()
            .statusCode(200)
            .email(user.getEmail())
            .usrNm(user.getUsername())
            .usrId(user.getId())
            .oauthProvider(user.getOauthProvider())
            .agreePrivacyPolicy(agreePrivacyPolicy)
            .agreeTermsOfService(agreeTermsOfService)
            .isNewUser(isNewUser)
            .build();
    }

    @Override
    @Transactional
    public UserResponseDto getInfoByUserId(Long userId) {
        User user = createUser(userId);
    
        boolean agreePrivacyPolicy = agreementLogRepository.findByUserAndKind(user, "PRIVACY_POLICY")
            .map(AgreementLog::isAgreeYn)
            .orElse(false);

        boolean agreeTermsOfService = agreementLogRepository.findByUserAndKind(user, "TERMS_OF_SERVICE")
            .map(AgreementLog::isAgreeYn)
            .orElse(false);

        return UserResponseDto.fromEntity(user, agreePrivacyPolicy, agreeTermsOfService);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean setUsername(Long userId, String username){
        User user = createUser(userId);

        user.updateUsername(username);
        userRepository.save(user);

        return true;
    }


    @Override
    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 향후 그룹에 대한 유저 삭제도 이뤄져야 함 -> 그룹장일 때, 그룹원일 때

        List<Group> groupsOwned = groupRepository.findByGroupOwner(user);
        groupRepository.deleteAll(groupsOwned);
        agreementLogRepository.deleteByUser(user);
        groupMemberRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    public User createUser(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user;
    }

}