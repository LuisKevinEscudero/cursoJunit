package org.aguzman.test.springboot.app.services;

import java.math.BigDecimal;

import org.aguzman.test.springboot.app.models.Banco;
import org.aguzman.test.springboot.app.models.Cuenta;
import org.aguzman.test.springboot.app.repositories.BancoRepository;
import org.aguzman.test.springboot.app.repositories.CuentaRepository;

public class CuentaServiceImpl implements CuentaService{
	
	private CuentaRepository cuentaRepository;
	private BancoRepository bancoRepository;
	
	

	public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
		this.cuentaRepository = cuentaRepository;
		this.bancoRepository = bancoRepository;
	}

	@Override
	public Cuenta findById(Long id) {
		return cuentaRepository.findById(id);
	}

	@Override
	public int revisarTotalTransferencias(Long bancoId) {
		Banco banco = bancoRepository.findbyId(bancoId);
		return banco.getTotalTransferencias();
	}

	@Override
	public BigDecimal revisarSaldo(Long cuentaId) {

		Cuenta cuenta = cuentaRepository.findById(cuentaId);
		return cuenta.getSaldo();
	}

	@Override
	public void transferir(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto, Long bancoId) {
		
		Banco banco = bancoRepository.findbyId(bancoId);
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(totalTransferencias++);
		bancoRepository.update(banco);
		
		Cuenta cuentaOrigen = cuentaRepository.findById(numeroCuentaOrigen);
		cuentaOrigen.debito(monto);
		cuentaRepository.update(cuentaOrigen);
		
		Cuenta cuentaDestino =  cuentaRepository.findById(numeroCuentaDestino);
		cuentaDestino.credito(monto);
		cuentaRepository.update(cuentaDestino);
		
	}

}
