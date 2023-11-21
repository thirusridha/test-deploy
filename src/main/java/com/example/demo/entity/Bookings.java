package com.example.demo.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompsiteKey.class)
public class Bookings {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Integer id;
	@Id
	private Date date;
	private String timings;
	@ManyToOne
	@Id
	@JoinColumn(name = "user_id")
	User user;
}