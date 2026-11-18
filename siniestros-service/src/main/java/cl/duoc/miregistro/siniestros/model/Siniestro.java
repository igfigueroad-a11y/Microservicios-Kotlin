package cl.duoc.miregistro.siniestros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "siniestros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Siniestro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSiniestro;

    @NotBlank(message = "Ingresa el número de siniestro")
    @Pattern(
        regexp = "^SIN-\\d{2}-\\d{6}$",
        message = "Formato inválido. Ejemplo: SIN-26-008942"
    )
    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    private Long usuarioId;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String aseguradoNombre;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String companiaNombre;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String tipoSiniestro;

    @Column(length = 20)
    private String placa = "";

    @Column(length = 20)
    private String prioridad = "Media";

    @NotBlank
    @Column(nullable = false, length = 25)
    private String fechaOcurrencia;

    @NotNull
    @Column(nullable = false)
    private Long idEtapa;

    @NotBlank
    @Column(nullable = false, length = 60)
    private String etapaNombre;

    @NotBlank
    @Column(nullable = false, length = 25)
    private String fechaUltimaActualizacion;

    @Column(length = 500)
    private String comentario = "";

    private Long liquidadorId;

    @Column(length = 120)
    private String liquidadorNombre;
}