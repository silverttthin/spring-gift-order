package gift.product.service;


import gift.product.dto.CreateUserRequest;
import gift.product.dto.GetKakaoUserInfoResponse;
import gift.product.dto.LoginRequest;
import gift.product.dto.LoginResponse;
import gift.product.entity.User;
import gift.product.repository.KakaoTokenRepository;
import gift.product.repository.UserRepository;
import gift.product.commons.utils.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final KakaoService kakaoService;
	private final JwtUtil jwtUtil;


	public UserService(
		UserRepository userRepository,
		KakaoService kakaoService,
		JwtUtil jwtUtil
	) {
		this.userRepository = userRepository;
		this.kakaoService = kakaoService;
		this.jwtUtil = jwtUtil;
	}

	public Long register(CreateUserRequest req) {
		String accessToken = kakaoService.getKakaoToken(req.code()).accessToken();
		GetKakaoUserInfoResponse userInfo = kakaoService.getKakaoUserInfo(accessToken);

		if(userRepository.findByOauthId(userInfo.id()).isPresent()){
			throw new DataIntegrityViolationException("이미 동일한 카카오 아이디로 가입한 유저입니다.");
		}

		User user = new User(userInfo.kakaoAccount().profile().nickname(), userInfo.id());
		User saved = userRepository.save(user);
		return saved.getId();

	}


	public LoginResponse login(LoginRequest req) {
		String accessToken = kakaoService.getKakaoToken(req.code()).accessToken();
		GetKakaoUserInfoResponse userInfo = kakaoService.getKakaoUserInfo(accessToken);
		User foundUser = userRepository.findByOauthId(userInfo.id())
			.orElseThrow(() -> new IllegalArgumentException("미가입 유저입니다. 회원가입해주세요."));

		String token = jwtUtil.generateToken(foundUser);
		return new LoginResponse(token);
	}

}
