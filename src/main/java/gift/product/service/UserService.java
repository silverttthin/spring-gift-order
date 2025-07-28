package gift.product.service;


import gift.product.dto.*;
import gift.product.entity.KakaoToken;
import gift.product.entity.User;
import gift.product.repository.KakaoTokenRepository;
import gift.product.repository.UserRepository;
import gift.product.commons.utils.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final KakaoTokenRepository kakaoTokenRepository;
	private final KakaoService kakaoService;
	private final JwtUtil jwtUtil;


	public UserService(
		UserRepository userRepository,
		KakaoService kakaoService,
		KakaoTokenRepository kakaoTokenRepository,
		JwtUtil jwtUtil
	) {
		this.userRepository = userRepository;
		this.kakaoService = kakaoService;
		this.jwtUtil = jwtUtil;
		this.kakaoTokenRepository = kakaoTokenRepository;
	}

	public Long register(CreateUserRequest req) {
		GetKakaoTokenApiResponse tokenResponse = kakaoService.getKakaoToken(req.code());
		GetKakaoUserInfoResponse userInfo = kakaoService.getKakaoUserInfo(tokenResponse.accessToken());

		if(userRepository.findByOauthId(userInfo.id()).isPresent()){
			throw new DataIntegrityViolationException("이미 동일한 카카오 아이디로 가입한 유저입니다.");
		}

		User user = new User(userInfo.kakaoAccount().profile().nickname(), userInfo.id());
		User saved = userRepository.save(user);
		KakaoToken kakaoToken = new KakaoToken(
			saved,
			tokenResponse.accessToken(),
			tokenResponse.refreshToken(),
			tokenResponse.expiresIn(),
			tokenResponse.refreshTokenExpiresIn()
		);
		kakaoTokenRepository.save(kakaoToken);
		return saved.getId();

	}


	public LoginResponse login(LoginRequest req) {
		GetKakaoTokenApiResponse tokenResponse = kakaoService.getKakaoToken(req.code());
		GetKakaoUserInfoResponse userInfo = kakaoService.getKakaoUserInfo(tokenResponse.accessToken());
		User foundUser = userRepository.findByOauthId(userInfo.id())
			.orElseThrow(() -> new IllegalArgumentException("미가입 유저입니다. 회원가입해주세요."));

		Optional<KakaoToken> existingToken = kakaoTokenRepository.findByUser(foundUser);

		if(existingToken.isPresent()){
			KakaoToken kakaoToken = existingToken.get();
			kakaoToken.updateTokens(
				tokenResponse.accessToken(),
				tokenResponse.refreshToken(),
				tokenResponse.expiresIn(),
				tokenResponse.refreshTokenExpiresIn()
			);
		} else {
			KakaoToken kakaoToken = new KakaoToken(
				foundUser,
				tokenResponse.accessToken(),
				tokenResponse.refreshToken(),
				tokenResponse.expiresIn(),
				tokenResponse.refreshTokenExpiresIn()
			);
			kakaoTokenRepository.save(kakaoToken);
		}
		String token = jwtUtil.generateToken(foundUser);
		return new LoginResponse(token);
	}

}
