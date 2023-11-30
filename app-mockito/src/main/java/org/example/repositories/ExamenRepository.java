package org.example.repositories;

import java.util.List;

import org.example.models.Examen;

public interface ExamenRepository {
	List<Examen> findAll();
	Examen guardar(Examen examen);

}
