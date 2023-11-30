package org.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.example.Datos;
import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.ExamenRepositoryImpl;
import org.example.repositories.PreguntaRepository;
import org.example.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceImplTest {

	@Mock
	ExamenRepositoryImpl examenRepository;
	
	@Mock
	PreguntaRepositoryImpl preguntaRepository;
	
	@InjectMocks
	ExamenServiceImpl examenService;
	
	@Captor
	ArgumentCaptor<Long> captor;
	
	@BeforeEach
	void setUp() {
		//MockitoAnnotations.openMocks(this);
		/* examenRepository = mock(ExamenRepository.class);
		 preguntaRepository = mock(PreguntaRepository.class);
		 examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);*/
	}
	
	@Test
	void findExamenPorNombre() {
				
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		
		Optional<Examen> examen = examenService.findExamenPorNombre("Matematicas");

		assertTrue(examen.isPresent());
		assertEquals(5L, examen.orElseThrow().getId());
		assertEquals("Matematicas", examen.get().getNombre());
	}
	
	@Test
	void findExamenPorNombreListaVacia() {
		
		List<Examen> datos= Collections.emptyList();
		when(examenRepository.findAll()).thenReturn(datos);
		
		Optional<Examen> examen = examenService.findExamenPorNombre("Matematicas");

		assertFalse(examen.isPresent());
	}
	
	@Test
	void testPreguntasExamen() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));
	}
	
	@Test
	void testPreguntasExamenVerify() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("integrales"));
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(5L);
	}
	
	@Test
	void testNoExisteExamenVerify() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		assertNull(examen);
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(5L);
	}
	
	@Test
	void testGuardarExamen(){
		
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);
		
		when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {
			
			Long secuencia = 8L;
			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}
		});
		
		Examen examen = examenService.guardar(newExamen);
		
		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Fisica", examen.getNombre());
		
		verify(examenRepository).guardar(any(Examen.class));
		verify(preguntaRepository).guardarVarias(anyList());
	}
	
	@Test
	void testManejoException() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
		
		assertThrows(IllegalArgumentException.class, () -> examenService.findExamenPorNombreConPreguntas("Matematicas"));
		
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(isNull());
	}
	
	@Test
	void testArgumentMatchers() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(a ->a != null && a >= 5L));
	}
	
	
	@Test
	void testArgumentMatchers2() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatcher()));
	}
	
	@Test
	void testArgumentMatchers3() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		verify(examenRepository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat((argument) -> argument != null && argument > 0));
	}
	
	public static class MiArgsMatcher implements ArgumentMatcher<Long>{
		private Long argument;

		@Override
		public boolean matches(Long argument) {
			this.argument = argument;
			return argument != null && argument > 0;
		}

		@Override
		public String toString() {
			return "es para un mensaje personalizado de error que imprime mockito en caso de que falle el test, "
					+ argument + " debe ser un entero positivo";
		}	
	}
	
	@Test
	void testArgumentCaptor() {
		
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		//ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
		
		assertEquals(5L, captor.getValue());
	}
	
	@Test
	void testDoThrow() {
		
		Examen examen = Datos.EXAMEN;
		examen.setPreguntas(Datos.PREGUNTAS);
		
		doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());
		
		assertThrows(IllegalArgumentException.class, () -> examenService.guardar(examen));
		
	}
	
	@Test
	void testDoAnswer() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return id == 5L ? Datos.PREGUNTAS : null;
		}).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("geometria"));
		
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
		
	}
	
	@Test
	void testdoCallRealMethod(){
		
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(5L, examen.getId());
	}
	
	@Test
	void testSpy() {
		ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
		PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
		ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
		
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
		
		assertEquals(5, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));
		
		verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}
	
	 @Test
	    void testOrdenDeInvocaciones() {
	        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

	        examenService.findExamenPorNombreConPreguntas("Matemáticas");
	        examenService.findExamenPorNombreConPreguntas("Lenguaje");

	        InOrder inOrder = inOrder(preguntaRepository);
	        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
	        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);

	    }

	    @Test
	    void testOrdenDeInvocaciones2() {
	        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

	        examenService.findExamenPorNombreConPreguntas("Matemáticas");
	        examenService.findExamenPorNombreConPreguntas("Lenguaje");

	        InOrder inOrder = inOrder(examenRepository, preguntaRepository);
	        inOrder.verify(examenRepository).findAll();
	        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);

	        inOrder.verify(examenRepository).findAll();
	        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);

	    }

	    @Test
	    void testNumeroDeInvocaciones() {
	        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
	        examenService.findExamenPorNombreConPreguntas("Matemáticas");

	        verify(preguntaRepository).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, times(1)).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atMost(1)).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
	    }

	    @Test
	    void testNumeroDeInvocaciones2() {
	        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
	        examenService.findExamenPorNombreConPreguntas("Matemáticas");

//	        verify(preguntaRepository).findPreguntasPorExamenId(5L); falla
	        verify(preguntaRepository, times(2)).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atLeast(2)).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
	        verify(preguntaRepository, atMost(20)).findPreguntasPorExamenId(5L);
//	        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L); falla
	    }

	    @Test
	    void testNumeroInvocaciones3() {
	        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
	        examenService.findExamenPorNombreConPreguntas("Matemáticas");

	        verify(preguntaRepository, never()).findPreguntasPorExamenId(5L);
	        verifyNoInteractions(preguntaRepository);

	        verify(examenRepository).findAll();
	        verify(examenRepository, times(1)).findAll();
	        verify(examenRepository, atLeast(1)).findAll();
	        verify(examenRepository, atLeastOnce()).findAll();
	        verify(examenRepository, atMost(10)).findAll();
	        verify(examenRepository, atMostOnce()).findAll();
	    }
}




