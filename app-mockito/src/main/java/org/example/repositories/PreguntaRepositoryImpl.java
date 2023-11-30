package org.example.repositories;

import java.util.List;

import org.example.Datos;

public class PreguntaRepositoryImpl implements PreguntaRepository{

	@Override
	public List<String> findPreguntasPorExamenId(Long id) {
		System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
		return Datos.PREGUNTAS;
	}

	@Override
	public void guardarVarias(List<String> preguntas) {
		System.out.println("PreguntaRepositoryImpl.guardarVarias");
		
	}

}
