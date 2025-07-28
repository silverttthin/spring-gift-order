package gift.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SendOrderToKakaoRequest(
	@JsonProperty(value = "object_type", defaultValue = "text")
	String objectType,

	String text,

	LinkInfo link

) {
	public record LinkInfo(
		@JsonProperty(value = "web_url", defaultValue = "https://github.com/silverttthin")
		String webUrl,

		@JsonProperty(value = "mobile_web_url", defaultValue = "https://m.naver.com")
		String mobileWebUrl
	) {
	}
}
