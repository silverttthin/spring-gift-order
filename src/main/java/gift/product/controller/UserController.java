package gift.product.controller;


import gift.product.dto.*;
import gift.product.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public Long register(@RequestBody CreateUserRequest req) {
		return userService.register(req);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest req) {
		return userService.login(req);
	}

}
