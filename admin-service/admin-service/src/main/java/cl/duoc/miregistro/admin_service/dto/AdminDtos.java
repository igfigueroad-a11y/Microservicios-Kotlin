package cl.duoc.miregistro.admin_service.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AdminDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SiniestroAdminDTO {
        private Long idSiniestro;
        private String numero;
        private Long usuarioId;
        private String aseguradoNombre;
        private String companiaNombre;
        private String tipoSiniestro;
        private String placa;
        private String prioridad;
        private String fechaOcurrencia;
        private Long idEtapa;
        private String etapaNombre;
        private String fechaUltimaActualizacion;
        private String comentario;
        private Long liquidadorId;
        private String liquidadorNombre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LiquidadorDTO {
        @JsonAlias({"id", "idUsuario"})
        private Long id;
        private String nombre;
        private String correo;
        @JsonAlias({"rolNombre", "rol"})
        private String rolNombre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AsignarRequest {
        @NotNull(message = "Debe seleccionar un liquidador")
        private Long liquidadorId;
        private String liquidadorNombre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricasAdminDTO {
        private long total;
        private long sinAsignar;
        private long activos;
        private long cerrados;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PanelAdminResponse {
        private MetricasAdminDTO metricas;
        private List<SiniestroAdminDTO> siniestros;
        private List<LiquidadorDTO> liquidadores;
    }
}