package org.aguzman.test.springboot.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.aguzman.test.springboot.app.exceptions.DineroInsuficienteException;
import org.aguzman.test.springboot.app.models.Banco;
import org.aguzman.test.springboot.app.models.Cuenta;
import org.aguzman.test.springboot.app.repositories.BancoRepository;
import org.aguzman.test.springboot.app.repositories.CuentaRepository;
import org.aguzman.test.springboot.app.services.CuentaService;
import org.aguzman.test.springboot.app.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootTestApplicationTests {

	CuentaRepository cuentaRepository;
	BancoRepository bancoRepository;
	
	CuentaService cuentaService;
	
	@BeforeEach
	void setup() {
		cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}
	
	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		when(bancoRepository.findbyId(1L)).thenReturn(Datos.BANCO);
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		cuentaService.transferir(1L, 2L, new BigDecimal("100"), 1L);
		
		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		
		//int total = cuentaService.revisarTotalTransferencias(1L);
		
		//assertEquals(1, total);
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).update(any(Cuenta.class));
		
		verify(bancoRepository).findbyId(1L);
		verify(bancoRepository).update(any(Banco.class));
	}
	
	@Test
	void contextLoads2() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		when(bancoRepository.findbyId(1L)).thenReturn(Datos.BANCO);
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		assertThrows(DineroInsuficienteException.class, () -> cuentaService.transferir(1L, 2L, new BigDecimal("1200"), 1L));
		
		
		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		//int total = cuentaService.revisarTotalTransferencias(1L);
		
		//assertEquals(1, total);
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).update(any(Cuenta.class));
		
		verify(bancoRepository).findbyId(1L);
		verify(bancoRepository).update(any(Banco.class));
	}

}
