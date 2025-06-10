package es.proyectojuegoguerra.springboot.juegospringboot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Guerrero")
public class GuerreroEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String tipo;
	private int ataque;
	private int defensa;
	
	@ManyToOne
    @JoinColumn(name = "vehiculo_id") // FK que se guarda en la tabla guerrero
    private VehiculoEntity vehiculo;
	
	
	public GuerreroEntity() {
		super();
	}


	public GuerreroEntity(String nombre, String tipo, int ataque, int defensa) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
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


	public VehiculoEntity getVehiculo() {
		return vehiculo;
	}


	public void setVehiculo(VehiculoEntity vehiculo) {
		this.vehiculo = vehiculo;
	}
	
}
