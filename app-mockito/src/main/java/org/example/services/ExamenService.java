package org.example.services;

import java.util.Optional;

import org.example.models.Examen;

public interface ExamenService {

	Optional<Examen> findExamenPorNombre(String nombre);
	Examen findExamenPorNombreConPreguntas(String nombre);
	Examen guardar(Examen examen);
}
