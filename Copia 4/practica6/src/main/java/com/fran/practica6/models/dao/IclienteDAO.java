package com.fran.practica6.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.fran.practica6.models.entity.Cliente;

public interface IclienteDAO extends CrudRepository<Cliente, Long> {

}
