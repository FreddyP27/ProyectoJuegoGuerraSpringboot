package es.proyectojuegoguerra.springboot.juegospringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Long> {
}
