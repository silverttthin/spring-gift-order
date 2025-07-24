package gift.product.service;



import gift.product.dto.GetKakaoTokenApiResponse;
import gift.product.dto.GetKakaoTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class KakaoService {

	@Value("${kakao.client-id}")
	private String clientId;
	@Value("${kakao.redirect-uri}")
	private String redirectUri;


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
				.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
				.retrieve()
				.bodyToMono(GetKakaoTokenApiResponse.class)
				.block();

			return new GetKakaoTokenResponse(response.accessToken());
		} catch (Exception e) {
			throw new IllegalArgumentException("잘못된 코드입니다. 코드를 재발급받아 다시 시도해주세요", e.getCause());
		}
	}


}
