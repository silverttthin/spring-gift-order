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

	public KakaoToken(User user, String accessToken, String refreshToken, Integer accessTokenExpiresAt, Integer refreshTokenExpiresAt) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresAt = Instant.ofEpochSecond(accessTokenExpiresAt);
		this.refreshTokenExpiresAt = Instant.ofEpochSecond(refreshTokenExpiresAt);
	}

	public void updateTokens(String accessToken, String refreshToken, Integer accessTokenExpiresAt, Integer refreshTokenExpiresAt) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresAt = Instant.ofEpochSecond(accessTokenExpiresAt);
		this.refreshTokenExpiresAt = Instant.ofEpochSecond(refreshTokenExpiresAt);
	}


	public Long getId() {
		return id;
	}


	public User getUser() {
		return user;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public String getRefreshToken() {
		return refreshToken;
	}


	public Instant getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}


	public Instant getRefreshTokenExpiresAt() {
		return refreshTokenExpiresAt;
	}

}
