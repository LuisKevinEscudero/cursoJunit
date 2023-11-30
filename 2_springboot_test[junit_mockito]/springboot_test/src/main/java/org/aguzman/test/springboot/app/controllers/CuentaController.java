package org.aguzman.test.springboot.app.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.aguzman.test.springboot.app.models.Cuenta;
import org.aguzman.test.springboot.app.models.TransaccionDto;
import org.aguzman.test.springboot.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
	
	@Autowired
	private CuentaService cuentaService;
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cuenta detalle(@PathVariable Long id) {
		return cuentaService.findById(id);
	}
	
	@PostMapping("/transferir")
	public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
		cuentaService.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "OK");
		response.put("mensaje", "Transferencia realizada con exito");
		response.put("transaccion", dto);
		
		return ResponseEntity.ok(response);
	}

}
