package gift.product.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreateUserRequest(
	@NotBlank
	String code
) {
}
