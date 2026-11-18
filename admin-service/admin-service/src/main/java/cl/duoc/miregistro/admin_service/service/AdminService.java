package cl.duoc.miregistro.admin_service.service;



import cl.duoc.miregistro.admin_service.dto.AdminDtos.AsignarRequest;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.LiquidadorDTO;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.MetricasAdminDTO;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.PanelAdminResponse;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.SiniestroAdminDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {

    private final RestClient siniestrosClient;
    private final RestClient usuariosClient;

    public AdminService(
        @Qualifier("siniestrosClient") RestClient siniestrosClient,
        @Qualifier("usuariosClient") RestClient usuariosClient
    ) {
        this.siniestrosClient = siniestrosClient;
        this.usuariosClient = usuariosClient;
    }

    public List<SiniestroAdminDTO> listarSiniestros(String filtro) {
        try {
            List<SiniestroAdminDTO> todos = siniestrosClient.get()
                .uri("/api/siniestros")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

            if (todos == null) return List.of();

            return todos.stream()
                .filter(s -> {
                    if ("SIN_ASIGNAR".equalsIgnoreCase(filtro)) return s.getLiquidadorId() == null;
                    if ("ASIGNADOS".equalsIgnoreCase(filtro)) return s.getLiquidadorId() != null;
                    return true;
                })
                .sorted(Comparator
                    .comparing((SiniestroAdminDTO s) -> s.getLiquidadorId() != null)
                    .thenComparing(SiniestroAdminDTO::getFechaOcurrencia, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error al consultar siniestros-service");
        }
    }

    public List<LiquidadorDTO> listarLiquidadores() {
        try {
            List<LiquidadorDTO> usuarios = usuariosClient.get()
                .uri("/api/usuarios")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

            if (usuarios == null) return List.of();

            return usuarios.stream()
                .filter(u -> u.getRolNombre() != null && u.getRolNombre().toUpperCase().contains("LIQUIDADOR"))
                .toList();
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error al consultar usuarios-service");
        }
    }

    public PanelAdminResponse obtenerPanel(String filtro) {
        List<SiniestroAdminDTO> todos = listarSiniestros("TODOS");
        List<SiniestroAdminDTO> filtrados = listarSiniestros(filtro);
        List<LiquidadorDTO> liquidadores = listarLiquidadores();

        long total = todos.size();
        long sinAsignar = todos.stream().filter(s -> s.getLiquidadorId() == null).count();
        long cerrados = todos.stream().filter(s -> "Cerrado".equalsIgnoreCase(s.getEtapaNombre())).count();
        long activos = todos.stream().filter(s -> s.getLiquidadorId() != null && !"Cerrado".equalsIgnoreCase(s.getEtapaNombre())).count();

        MetricasAdminDTO metricas = new MetricasAdminDTO(total, sinAsignar, activos, cerrados);
        return new PanelAdminResponse(metricas, filtrados, liquidadores);
    }

    public SiniestroAdminDTO asignarLiquidador(String numero, AsignarRequest request) {
        String nombreLiquidador = request.getLiquidadorNombre();

        if (nombreLiquidador == null || nombreLiquidador.isBlank()) {
            nombreLiquidador = listarLiquidadores().stream()
                .filter(l -> request.getLiquidadorId().equals(l.getId()))
                .map(LiquidadorDTO::getNombre)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liquidador no encontrado"));
        }

        AsignarRequest payload = new AsignarRequest(request.getLiquidadorId(), nombreLiquidador);

        try {
            return siniestrosClient.put()
                .uri("/api/siniestros/{numero}/asignar", numero.trim().toUpperCase())
                .body(payload)
                .retrieve()
                .body(SiniestroAdminDTO.class);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo asignar el siniestro en siniestros-service");
        }
    }
}