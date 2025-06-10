package es.proyectojuegoguerra.springboot.juegospringboot.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculo") // SE CAMBIO EL NOMBRE DE LA TABLA ANTES ERA NAVE ESPACIAL
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private int vida = 1000;
    private int ataque;
    private int defensa;
    
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GuerreroEntity> guerreros = new ArrayList<>();

    public VehiculoEntity() {
		super();
	}

	public VehiculoEntity(Long id, String nombre, String tipo, int ataque, int defensa) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.ataque = ataque;
		this.defensa = defensa;
	}
	
	



	public VehiculoEntity(String nombre, String tipo, int vida, int ataque, int defensa) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.vida = vida;
		this.ataque = ataque;
		this.defensa = defensa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefensa() {
		return defensa;
	}

	public void setDefensa(int defensa) {
		this.defensa = defensa;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public List<GuerreroEntity> getGuerreros() {
		return guerreros;
	}

	public void setGuerreros(List<GuerreroEntity> guerreros) {
		this.guerreros = guerreros;
	}
	
}
