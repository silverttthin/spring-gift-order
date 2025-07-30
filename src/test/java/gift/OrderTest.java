package gift;

import gift.product.dto.CreateOrderRequest;
import gift.product.entity.Option;
import gift.product.entity.User;
import gift.product.repository.*;
import gift.product.service.KakaoService;
import gift.product.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class OrderTest {
	@Mock
	private OptionRepository optionRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private WishListRepository wishListRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private KakaoService kakaoService;

	@Mock
	private KakaoTokenRepository kakaoTokenRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	void 없는옵션주문시예외던지기() {
		given(optionRepository.findById(any()))
			.willThrow(new IllegalArgumentException("존재하지 않는 옵션입니다."));

		CreateOrderRequest request = new CreateOrderRequest("message", 1 );

		assertThatThrownBy(() -> orderService.createOrder(any(), request, 1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 옵션입니다.");
	}

	@Test
	void 주문수량이옵션수량을초과하면에러발생() {
		Option mockOption = org.mockito.Mockito.mock(Option.class);
		User mockUser = org.mockito.Mockito.mock(User.class);

		given(optionRepository.findById(any())).willReturn(Optional.ofNullable(mockOption));
		given(userRepository.findById(any())).willReturn(Optional.ofNullable(mockUser));
		given(mockOption.getQuantity()).willReturn(1);
			doThrow(new IllegalArgumentException("수량은 1개 이상 1억 미만입니다."))
			.when(mockOption).decreaseQuantity(1000);

		CreateOrderRequest request = new CreateOrderRequest("message", 1000 );


		// when & then
		assertThatThrownBy(() -> orderService.createOrder(1L, request, 1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("수량은 1개 이상 1억 미만입니다.");

	}
}
