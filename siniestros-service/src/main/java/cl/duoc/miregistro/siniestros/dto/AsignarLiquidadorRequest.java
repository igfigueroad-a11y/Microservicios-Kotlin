package cl.duoc.miregistro.siniestros.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarLiquidadorRequest {

    @NotNull(message = "El id del liquidador es obligatorio")
    private Long liquidadorId;

    @NotBlank(message = "El nombre del liquidador es obligatorio")
    private String liquidadorNombre;
}