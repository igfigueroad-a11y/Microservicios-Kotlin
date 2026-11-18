package cl.duoc.miregistro.siniestros.controller;

import cl.duoc.miregistro.siniestros.dto.ActualizarEstadoRequest;
import cl.duoc.miregistro.siniestros.dto.AsignarLiquidadorRequest;
import cl.duoc.miregistro.siniestros.model.Siniestro;
import cl.duoc.miregistro.siniestros.service.SiniestroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/siniestros")
public class SiniestroController {

    private final SiniestroService siniestroService;

    public SiniestroController(SiniestroService siniestroService) {
        this.siniestroService = siniestroService;
    }

    @GetMapping("/existe/{numero}")
    public ResponseEntity<Map<String, Boolean>> verificarExistencia(@PathVariable("numero") String numero) {
        boolean existe = siniestroService.existeSiniestro(numero);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<Siniestro> obtenerPorNumero(@PathVariable("numero") String numero) {
        return siniestroService.buscarPorNumero(numero)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Siniestro>> listarPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(siniestroService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/liquidador/{liquidadorId}")
    public ResponseEntity<List<Siniestro>> listarPorLiquidador(@PathVariable("liquidadorId") Long liquidadorId) {
        return ResponseEntity.ok(siniestroService.listarPorLiquidador(liquidadorId));
    }

    @GetMapping
    public ResponseEntity<List<Siniestro>> listar() {
        return ResponseEntity.ok(siniestroService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Siniestro> crear(@Valid @RequestBody Siniestro siniestro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siniestroService.guardar(siniestro));
    }

    @PutMapping("/{numero}/asignar")
    public ResponseEntity<Siniestro> asignarLiquidador(
        @PathVariable("numero") String numero,
        @Valid @RequestBody AsignarLiquidadorRequest request
    ) {
        return ResponseEntity.ok(siniestroService.asignarLiquidador(numero, request));
    }

    @PutMapping("/{numero}/estado")
    public ResponseEntity<Siniestro> actualizarEstado(
        @PathVariable("numero") String numero,
        @Valid @RequestBody ActualizarEstadoRequest request
    ) {
        return ResponseEntity.ok(siniestroService.actualizarEstado(numero, request));
    }
}