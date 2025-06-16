package es.proyectojuegoguerra.springboot.juegospringboot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;

@Service
public class CombateService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    private static final Logger logger = LoggerFactory.getLogger(CombateService.class);
   

    public String combate(Long vehiculoId1, Long vehiculoId2) {
        // Validacion para que el vehiculo no se enfrente a si mismo
        if (vehiculoId1.equals(vehiculoId2)) {
            return "No se puede iniciar un combate entre el mismo vehículo.";
        }

        VehiculoEntity v1 = vehiculoRepository.findById(vehiculoId1)
                .orElseThrow(() -> new RuntimeException("Vehículo 1 no encontrado"));
        VehiculoEntity v2 = vehiculoRepository.findById(vehiculoId2)
                .orElseThrow(() -> new RuntimeException("Vehículo 2 no encontrado"));

        // Validación: ambos vehículos deben tener al menos un guerrero
        if (v1.getGuerreros() == null || v1.getGuerreros().isEmpty()) {
            logger.warn("Vehículo 1 no tiene guerreros. Combate cancelado.");
            System.out.println("Vehículo 1 no tiene guerreros. Combate cancelado.");
            return "El Vehículo 1 no tiene guerreros asignados. No se puede iniciar el combate.";
        }

        if (v2.getGuerreros() == null || v2.getGuerreros().isEmpty()) {
            logger.warn("Vehículo 2 no tiene guerreros. Combate cancelado.");
            System.out.println("Vehículo 2 no tiene guerreros. Combate cancelado.");
            return "El Vehículo 2 no tiene guerreros asignados. No se puede iniciar el combate.";
        }

        int vidaV1 = v1.getVida();
        int vidaV2 = v2.getVida();

        int ataqueV1 = v1.getAtaque() + sumaAtaqueGuerreros(v1);
        int defensaV1 = v1.getDefensa() + sumaDefensaGuerreros(v1);

        int ataqueV2 = v2.getAtaque() + sumaAtaqueGuerreros(v2);
        int defensaV2 = v2.getDefensa() + sumaDefensaGuerreros(v2);

        logger.debug("Stats V1: vida={}, ataque={}, defensa={}", vidaV1, ataqueV1, defensaV1);
        logger.debug("Stats V2: vida={}, ataque={}, defensa={}", vidaV2, ataqueV2, defensaV2);

        boolean turnoV1 = true;

        while (vidaV1 > 0 && vidaV2 > 0) {
            int random = (int)(Math.random() * 10) + 1;
            int dano;

            if (turnoV1) {
                dano = random + (ataqueV1 - defensaV2);
                vidaV2 -= dano;
                logger.info("V1 ataca con daño={} (random {}, ataque {}, defensa V2 {}). Vida V2: {}",
                        dano, random, ataqueV1, defensaV2, Math.max(vidaV2, 0));
            } else {
                dano = random + (ataqueV2 - defensaV1);
                vidaV1 -= dano;
                logger.info("V2 ataca con daño={} (random {}, ataque {}, defensa V1 {}). Vida V1: {}",
                        dano, random, ataqueV2, defensaV1, Math.max(vidaV1, 0));
            }

            turnoV1 = !turnoV1;
        }

        String resultado;
        if (vidaV1 <= 0 && vidaV2 <= 0) {
            resultado = "Empate: ambos vehículos han quedado destruidos";
        } else if (vidaV1 <= 0) {
            resultado = "Vehículo 2 gana el combate";
        } else {
            resultado = "Vehículo 1 gana el combate";
        }

        logger.info("Resultado del combate: {}", resultado);

        v1.setVida(Math.max(0, vidaV1));
        v2.setVida(Math.max(0, vidaV2));
        vehiculoRepository.save(v1);
        vehiculoRepository.save(v2);

        return resultado;
    }

    private int sumaAtaqueGuerreros(VehiculoEntity vehiculo) {
        return vehiculo.getGuerreros().stream()
                .mapToInt(GuerreroEntity::getAtaque)
                .sum();
    }

    private int sumaDefensaGuerreros(VehiculoEntity vehiculo) {
        return vehiculo.getGuerreros().stream()
                .mapToInt(GuerreroEntity::getDefensa)
                .sum();
    }
}