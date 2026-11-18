package cl.duoc.miregistro.siniestros.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarEstadoRequest {

    @NotBlank(message = "El nuevo estado es obligatorio")
    private String etapaNombre;
}