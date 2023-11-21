package com.example.demo.Dao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.User;

public interface UserDao extends JpaRepository<User, String>{
	Optional<User> findByUsername(String username);

	List<User> findByUserId(Integer id);

//	List<User> findById(Long id);
	


}
 