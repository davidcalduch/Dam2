package com.fran.practica6.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fran.practica6.models.entity.Starship;

public interface IStarshipDAO extends CrudRepository<Starship, Long> {
      
}

