package gift.product.entity;


import jakarta.persistence.*;

import java.time.Instant;


@Entity
public class KakaoToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String accessToken;

	private String refreshToken;

	private Instant accessTokenExpiresAt;

	private Instant refreshTokenExpiresAt;

	protected KakaoToken() {}

	public KakaoToken(User user, String accessToken, String refreshToken, Instant accessTokenExpiresAt, Instant refreshTokenExpiresAt) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresAt = accessTokenExpiresAt;
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}

	public void updateTokens(String accessToken, String refreshToken, Instant accessTokenExpiresAt, Instant refreshTokenExpiresAt) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresAt = accessTokenExpiresAt;
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}

	public User getUser() {
		return this.user;
	}

}
