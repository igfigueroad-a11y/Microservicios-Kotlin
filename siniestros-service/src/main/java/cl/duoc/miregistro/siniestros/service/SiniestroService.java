package cl.duoc.miregistro.siniestros.service;

import cl.duoc.miregistro.siniestros.dto.ActualizarEstadoRequest;
import cl.duoc.miregistro.siniestros.dto.AsignarLiquidadorRequest;
import cl.duoc.miregistro.siniestros.model.Siniestro;
import cl.duoc.miregistro.siniestros.repository.SiniestroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SiniestroService {

    private static final List<String> ORDEN_ESTADOS = List.of(
        "Recibido",
        "En evaluación",
        "En liquidación",
        "Cerrado"
    );

    private final SiniestroRepository siniestroRepository;

    public SiniestroService(SiniestroRepository siniestroRepository) {
        this.siniestroRepository = siniestroRepository;
    }

    public boolean existeSiniestro(String numero) {
        return siniestroRepository.existsByNumero(numero.trim().toUpperCase());
    }

    public Optional<Siniestro> buscarPorNumero(String numero) {
        return siniestroRepository.findByNumero(numero.trim().toUpperCase());
    }

    public List<Siniestro> listarPorUsuario(Long usuarioId) {
        return siniestroRepository.findByUsuarioId(usuarioId);
    }

    public List<Siniestro> listarPorLiquidador(Long liquidadorId) {
        return siniestroRepository.findByLiquidadorId(liquidadorId);
    }

    public List<Siniestro> listarTodos() {
        return siniestroRepository.findAll();
    }

    public Siniestro guardar(Siniestro siniestro) {
        siniestro.setNumero(siniestro.getNumero().trim().toUpperCase());
        return siniestroRepository.save(siniestro);
    }

    public Siniestro asignarLiquidador(String numero, AsignarLiquidadorRequest request) {
        Siniestro siniestro = siniestroRepository.findByNumero(numero.trim().toUpperCase())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Siniestro no encontrado"));

        if ("Cerrado".equalsIgnoreCase(siniestro.getEtapaNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede reasignar un siniestro cerrado");
        }

        siniestro.setLiquidadorId(request.getLiquidadorId());
        siniestro.setLiquidadorNombre(request.getLiquidadorNombre().trim());
        siniestro.setFechaUltimaActualizacion(LocalDate.now().toString());
        return siniestroRepository.save(siniestro);
    }

    public Siniestro actualizarEstado(String numero, ActualizarEstadoRequest request) {
        Siniestro siniestro = siniestroRepository.findByNumero(numero.trim().toUpperCase())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Siniestro no encontrado"));

        int indiceActual = obtenerIndiceEstado(siniestro.getEtapaNombre());
        int indiceNuevo = obtenerIndiceEstado(request.getEtapaNombre());

        if (indiceNuevo == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido");
        }
        if (indiceActual != -1 && indiceNuevo < indiceActual) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se permite retroceder a un estado anterior");
        }

        String estadoNormalizado = ORDEN_ESTADOS.get(indiceNuevo);
        siniestro.setEtapaNombre(estadoNormalizado);
        siniestro.setIdEtapa((long) (indiceNuevo + 1));
        siniestro.setFechaUltimaActualizacion(LocalDate.now().toString());
        return siniestroRepository.save(siniestro);
    }

    private int obtenerIndiceEstado(String estado) {
        if (estado == null) return -1;
        for (int i = 0; i < ORDEN_ESTADOS.size(); i++) {
            if (ORDEN_ESTADOS.get(i).equalsIgnoreCase(estado.trim())) {
                return i;
            }
        }
        return -1;
    }
}