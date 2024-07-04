package com.fran.practica6.models.services;

import java.util.List;

import com.fran.practica6.models.entity.Starship;

public interface IStarshipService {
    public List<Starship> findAll();
    public Starship save (Starship starship);
    public Starship findById(Long id);
    public void delete(Long id);
    public Starship update(Starship starship);

}
