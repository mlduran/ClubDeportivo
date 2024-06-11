package mld.playhitsgame.spotify;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios_spotify")
@Getter
@Setter
public class UserDetails implements Serializable {
	private static final long serialVersionUID = 3937414011943770889L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "ACCESS_TOKEN")
	private String accessToken;

	@Column(name = "REFRESH_TOKEN")
	private String refreshToken;

	@Column(name = "REF_ID")
	private String refId;
        
        @Column(name = "REF_SECRET")
	private String refSecret;
	
	// More information as per your need

}
