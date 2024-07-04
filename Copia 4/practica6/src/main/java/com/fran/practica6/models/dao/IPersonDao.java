package com.fran.practica6.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.fran.practica6.models.entity.Person;

public interface IPersonDao extends CrudRepository<Person, Long> {
}
