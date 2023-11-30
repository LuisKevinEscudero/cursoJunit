package org.example.repositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.example.Datos;
import org.example.models.Examen;

public class ExamenRepositoryImpl implements ExamenRepository{

	@Override
	public List<Examen> findAll() {
		System.out.println("ExamenRepositoryImpl.findAll");
		try {
			System.out.println("ExamenRepositoryOtro");
			TimeUnit.SECONDS.sleep(5);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return Datos.EXAMENES;
	}

	@Override
	public Examen guardar(Examen examen) {
		System.out.println("ExamenRepositoryImpl.guardar");
		return Datos.EXAMEN;
	}

}
