package org.aguzman.test.springboot.app.repositories;

import java.util.List;

import org.aguzman.test.springboot.app.models.Banco;

public interface BancoRepository {
	List<Banco> findAll();
	Banco findbyId(Long id);
	void update(Banco banco);

}
