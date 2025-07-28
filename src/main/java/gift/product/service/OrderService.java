package gift.product.service;


import gift.product.dto.CreateOrderRequest;
import gift.product.entity.Option;
import gift.product.entity.Order;
import gift.product.entity.User;
import gift.product.entity.WishList;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.repository.UserRepository;
import gift.product.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final OptionRepository optionRepository;
	private final UserRepository userRepository;
	private final WishListRepository wishListRepository;
	public OrderService(
		OrderRepository orderRepository,
		OptionRepository optionRepository,
		UserRepository userRepository,
		WishListRepository wishListRepository

	) {
		this.orderRepository = orderRepository;
		this.optionRepository = optionRepository;
		this.userRepository = userRepository;
		this.wishListRepository = wishListRepository;
	}


	public void createOrder(Long optionId, CreateOrderRequest request, Long userId) {
		Option option = optionRepository.findById(optionId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		if(option.getQuantity() <= 0) {
			throw new IllegalArgumentException("옵션의 수량이 0 이하면 주문할 수 없습니다.");
		}

		Order order = new Order(option, user, request.quantity(), request.message());

		// 카카오 나에게 보내기 기능

		// 들어올 곳

		orderRepository.save(order);

		// 위시리스트에 아이템 존재하면 제거
		wishListRepository.findByUserAndItem(user, option.getItem())
			.ifPresent(wishListRepository::delete);

	}

}
