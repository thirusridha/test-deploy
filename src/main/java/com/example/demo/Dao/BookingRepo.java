package com.example.demo.Dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Bookings;
import com.example.demo.entity.CompsiteKey;
import com.example.demo.entity.User;

public interface BookingRepo extends JpaRepository<Bookings,CompsiteKey>{

	List<Bookings> findByDate(Date date);
	List<Bookings> findByDateAndUser(Date date,User user);
	List<Bookings> findByUserUserId(Long id);
}
