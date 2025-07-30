package gift.product.dto;


import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
	@NotBlank
	String code) {
}
