package gift.product.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record CreateOrderRequest(
	String message,

	@NotNull
	@Min(1)
	int quantity
) {
}
