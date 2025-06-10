package es.proyectojuegoguerra.springboot.juegospringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;

public interface GuerreroRepository extends JpaRepository<GuerreroEntity, Long> {
    List<GuerreroEntity> findByTipo(String tipo);
}