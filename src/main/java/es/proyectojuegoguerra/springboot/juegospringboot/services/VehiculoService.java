package es.proyectojuegoguerra.springboot.juegospringboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public void registrarVehiculo(VehiculoEntity vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    public List<VehiculoEntity> listarVehiculos() {
        return vehiculoRepository.findAll();
    }
}
