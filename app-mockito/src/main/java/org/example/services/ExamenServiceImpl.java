package org.example.services;

import java.util.List;
import java.util.Optional;

import org.example.models.Examen;
import org.example.repositories.ExamenRepository;
import org.example.repositories.PreguntaRepository;

public class ExamenServiceImpl implements ExamenService {
	
	private ExamenRepository examenRepository;
	private PreguntaRepository preguntaRepository;
	
	public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
		this.examenRepository = examenRepository;
		this.preguntaRepository = preguntaRepository;
	}

	@Override
	public Optional<Examen> findExamenPorNombre(String nombre) {

		return examenRepository.findAll().stream()
		.filter(e -> e.getNombre().contains(nombre))
		.findFirst();
	}

	@Override
	public Examen findExamenPorNombreConPreguntas(String nombre) {

		Optional<Examen> examenOptional = findExamenPorNombre(nombre);
		Examen examen = null;
		if(examenOptional.isPresent()) {
			examen = examenOptional.orElseThrow();
			List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
			examen.setPreguntas(preguntas);
		}
		return examen;
	}

	@Override
	public Examen guardar(Examen examen) {

		if(!examen.getPreguntas().isEmpty()) {
			preguntaRepository.guardarVarias(examen.getPreguntas());
		}

		return examenRepository.guardar(examen);
	}

}
