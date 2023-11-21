package com.example.demo.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dao.UserDao;
import com.example.demo.entity.User;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
	@Autowired
	UserDao empDao;
	@GetMapping("/getEmp/{id}")
	public Optional<User> getEmp(@PathVariable String id) {
		System.out.println("emp" + empDao.findById(id));
		return empDao.findById(id);
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable String id) {
		empDao.deleteById(id);
		System.out.println(id + " is deleted successfully....");
	}
	
	@GetMapping("/showAll")
	public List<User> showAll() {
		List<User> empList = empDao.findAll();   
		return empList;
	}

	

	
}
