package gift.product.entity;


import jakarta.persistence.*;


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

	protected KakaoToken() {}

	public KakaoToken(User user, String accessToken, String refreshToken) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public void updateTokens(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public User getUser() {
		return this.user;
	}

}
