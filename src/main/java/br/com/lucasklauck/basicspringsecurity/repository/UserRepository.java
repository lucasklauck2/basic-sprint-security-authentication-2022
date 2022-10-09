package br.com.lucasklauck.basicspringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lucasklauck.basicspringsecurity.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
	
	public abstract UserModel findByEmail(String email);
	
	public abstract boolean existsByEmail(String email);
}
