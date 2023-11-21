package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
public class CompsiteKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date;
//	User user;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
