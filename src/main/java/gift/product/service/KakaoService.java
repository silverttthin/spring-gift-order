package gift.product.service;


import gift.product.dto.GetKakaoTokenResponse;
import gift.product.dto.GetKakaoUserInfoResponse;


public interface KakaoService {
	GetKakaoTokenResponse getKakaoToken(String code);
	GetKakaoUserInfoResponse getKakaoUserInfo(String accessToken);
}
