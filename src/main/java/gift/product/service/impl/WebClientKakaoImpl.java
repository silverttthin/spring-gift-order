package gift.product.service.impl;

import gift.product.dto.GetKakaoTokenApiResponse;
import gift.product.dto.GetKakaoTokenResponse;
import gift.product.dto.GetKakaoUserInfoResponse;
import gift.product.service.KakaoService;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class WebClientKakaoImpl implements KakaoService {

	@Value("${kakao.client-id}")
	private String clientId;
	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	@Override
	public GetKakaoTokenResponse getKakaoToken(String code) {
		try {
			GetKakaoTokenApiResponse response =  WebClient.create("https://kauth.kakao.com")
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
			return new GetKakaoTokenResponse(response.accessToken());
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

}
