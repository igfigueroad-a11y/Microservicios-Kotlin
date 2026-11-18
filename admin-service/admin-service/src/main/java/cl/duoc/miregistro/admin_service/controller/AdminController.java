package cl.duoc.miregistro.admin_service.controller;


import cl.duoc.miregistro.admin_service.dto.AdminDtos.AsignarRequest;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.LiquidadorDTO;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.PanelAdminResponse;
import cl.duoc.miregistro.admin_service.dto.AdminDtos.SiniestroAdminDTO;
import cl.duoc.miregistro.admin_service.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/panel")
    public ResponseEntity<PanelAdminResponse> obtenerPanel(
        @RequestParam(name = "filtro", defaultValue = "TODOS") String filtro
    ) {
        return ResponseEntity.ok(adminService.obtenerPanel(filtro));
    }

    @GetMapping("/siniestros")
    public ResponseEntity<List<SiniestroAdminDTO>> listarSiniestros(
        @RequestParam(name = "filtro", defaultValue = "TODOS") String filtro
    ) {
        return ResponseEntity.ok(adminService.listarSiniestros(filtro));
    }

    @GetMapping("/liquidadores")
    public ResponseEntity<List<LiquidadorDTO>> listarLiquidadores() {
        return ResponseEntity.ok(adminService.listarLiquidadores());
    }

    @PutMapping("/siniestros/{numero}/asignar")
    public ResponseEntity<SiniestroAdminDTO> asignarLiquidador(
        @PathVariable("numero") String numero,
        @Valid @RequestBody AsignarRequest request
    ) {
        return ResponseEntity.ok(adminService.asignarLiquidador(numero, request));
    }
}