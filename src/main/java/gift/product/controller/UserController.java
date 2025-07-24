package gift.product.controller;


import gift.product.dto.*;
import gift.product.service.KakaoService;
import gift.product.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final KakaoService kakaoService;

	public UserController(UserService userService, KakaoService kakaoService) {
		this.userService = userService;
		this.kakaoService = kakaoService;
	}

	@PostMapping("/register")
	public Long register(@RequestBody CreateUserRequest req) {
		return userService.register(req);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest req) {
		return userService.login(req);
	}

	@GetMapping("/kakao-token")
	public GetKakaoTokenResponse getKakaoToken(@RequestParam String code) {
		return kakaoService.getKakaoToken(code);
	}

}
