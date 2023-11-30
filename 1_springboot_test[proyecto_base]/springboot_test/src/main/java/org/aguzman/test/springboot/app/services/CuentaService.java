package org.aguzman.test.springboot.app.services;

import java.math.BigDecimal;

import org.aguzman.test.springboot.app.models.Cuenta;

public interface CuentaService {

	Cuenta findById(Long id);
	int revisarTotalTransferencias(Long bancoId);
	BigDecimal revisarSaldo(Long cuentaId);
	void transferir(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto, Long bancoId);
}
