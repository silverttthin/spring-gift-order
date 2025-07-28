package gift.product.repository;


import gift.product.entity.KakaoToken;
import gift.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);
	Optional<User> findByOauthId(Long id);
}
