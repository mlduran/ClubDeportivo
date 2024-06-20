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
@Table(name = "listas_spotify")
@Getter
@Setter
public class ListasSpotify implements Serializable {
	private static final long serialVersionUID = 3937414011943770889L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CODIGO")
	private String codigo;

	@Column(name = "ANYO")
	private String anyo;
        
        @Column(name = "PAIS")
	private String pais;


}
