package gift.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.GetKakaoTokenApiResponse;
import gift.product.dto.GetKakaoUserInfoResponse;
import gift.product.dto.SendOrderToKakaoRequest;
import gift.product.entity.Order;
import gift.product.service.KakaoService;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;


@Service
public class WebClientKakaoImpl implements KakaoService {

	@Value("${kakao.client-id}")
	private String clientId;
	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Override
	public GetKakaoTokenApiResponse getKakaoToken(String code) {
		try {
			return WebClient.create("https://kauth.kakao.com")
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/oauth/token")
					.queryParam("grant_type", "authorization_code")
					.queryParam("client_id", clientId)
					.queryParam("code", code)
					.queryParam("redirect_uri", redirectUri)
					.build(true))
				.header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
				.retrieve()
				.bodyToMono(GetKakaoTokenApiResponse.class)
				.block();

		} catch (Exception e) {
			throw new IllegalArgumentException("잘못된 코드입니다. 코드를 재발급받아 다시 시도해주세요", e.getCause());
		}
	}


	@Override
	public GetKakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
		try {
			GetKakaoUserInfoResponse userInfo = WebClient.create("https://kapi.kakao.com")
				.get()
				.uri(uriBuilder -> uriBuilder
					.path("/v2/user/me")
					.build(true))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // 액세스 토큰 인가
				.header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
				.retrieve()
				.bodyToMono(GetKakaoUserInfoResponse.class)
				.block();
			return userInfo;
		} catch (Exception e) {
			throw new IllegalArgumentException("유저 정보를 불러오는데 실패했습니다. 토큰을 재발급받아 다시 시도해주세요.", e.getCause());
		}
	}

	@Override
	public void sendOrderToKakao(String accessToken, Order order) {
		try {
			SendOrderToKakaoRequest request = new SendOrderToKakaoRequest("text", formatOrderMessage(order), null);
			ObjectMapper objectMapper = new ObjectMapper();
			String templateObjectJson = objectMapper.writeValueAsString(request);

			// form-urlencoded 형태로 데이터 구성
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("template_object", templateObjectJson);
			WebClient.create("https://kapi.kakao.com/v2/api/talk/memo/default/send")
				.post()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.bodyValue(formData)
				.retrieve()
				.toBodilessEntity()
				.block();
		} catch (Exception e) {
			throw new RuntimeException("카카오 메시지 전송 오류 발생", e.getCause());
		}
	}

	private String formatOrderMessage(Order order) {
		StringBuilder message = new StringBuilder();

		message.append("📋 주문 정보\n");
		message.append("━━━━━━━━━━━━━━━\n");
		message.append("주문번호: ").append(order.getId()).append("\n");
		message.append("상품명: ").append(order.getOption().getItem().getName()).append("\n");
		message.append("옵션: ").append(order.getOption().getOptionName()).append("\n");
		message.append("수량: ").append(order.getQuantity()).append("개\n");
		message.append("주문일시: ").append(order.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");

		if (order.getMessage() != null && !order.getMessage().trim().isEmpty()) {
			message.append("메시지: ").append(order.getMessage()).append("\n");
		}

		return message.toString();
	}



}
