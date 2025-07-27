package gift.product.service;


import gift.product.dto.GetKakaoTokenResponse;


public interface KakaoService {
	GetKakaoTokenResponse getKakaoToken(String code);
}
