package es.proyectojuegoguerra.springboot.juegospringboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;

@Service
public class CombateService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public String combate(Long vehiculoId1, Long vehiculoId2) {
        VehiculoEntity v1 = vehiculoRepository.findById(vehiculoId1)
                .orElseThrow(() -> new RuntimeException("Vehículo 1 no encontrado"));
        VehiculoEntity v2 = vehiculoRepository.findById(vehiculoId2)
                .orElseThrow(() -> new RuntimeException("Vehículo 2 no encontrado"));

        int vidaV1 = v1.getVida();
        int vidaV2 = v2.getVida();
 
        int ataqueV1 = v1.getAtaque() + sumaAtaqueGuerreros(v1);
        int defensaV1 = v1.getDefensa() + sumaDefensaGuerreros(v1);

        int ataqueV2 = v2.getAtaque() + sumaAtaqueGuerreros(v2);
        int defensaV2 = v2.getDefensa() + sumaDefensaGuerreros(v2);

        boolean turnoV1 = true;

        while (vidaV1 > 0 && vidaV2 > 0) {
            if (turnoV1) {
                // V1 ataca a V2
                int random = (int)(Math.random() * 10) + 1;
                int dano = random + (ataqueV1 - defensaV2);
                vidaV2 -= dano;
                System.out.println("Vehiculo 1 ataca y hace " + dano + " de daño. Vida V2: " + vidaV2);
            } else {
                // V2 ataca a V1
                int random = (int)(Math.random() * 10) + 1;
                int dano = random + (ataqueV2 - defensaV1);
                vidaV1 -= dano;
                System.out.println("Vehiculo 2 ataca y hace " + dano + " de daño. Vida V1: " + vidaV1);
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

        // Opcional: actualizar la vida restante en base de datos
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
