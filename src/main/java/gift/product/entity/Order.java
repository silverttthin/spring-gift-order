package gift.product.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id")
	private Option option;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private int quantity;

	private String message;

	private LocalDateTime orderDateTime;

	protected Order() {}

	public Order(Option option, User user, int quantity, String message) {
		option.decreaseQuantity(quantity);

		this.option = option;
		this.user = user;
		this.quantity = quantity;
		this.message = message;
		this.orderDateTime = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Option getOption() {
		return option;
	}

	public User getUser() {
		return user;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}
}
