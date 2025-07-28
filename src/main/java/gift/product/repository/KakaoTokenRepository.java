package gift.product.repository;


import gift.product.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface KakaoTokenRepository  extends JpaRepository<KakaoToken, Long> {
}
