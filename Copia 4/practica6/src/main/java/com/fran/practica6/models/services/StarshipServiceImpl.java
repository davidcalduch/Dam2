package com.fran.practica6.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fran.practica6.models.dao.IStarshipDAO;
import com.fran.practica6.models.entity.Starship;


@Service
public class StarshipServiceImpl implements IStarshipService {

    @Autowired
    private IStarshipDAO starshipRepository;

    @Override
    public List<Starship> findAll() {
        return (List<Starship>) starshipRepository.findAll();
    }

    @Override
    public Starship findById(Long id) {
        return starshipRepository.findById(id).orElse(null);
    }

    @Override
    public Starship update(Starship starship) {
        return starshipRepository.save(starship);
    }

    @Override
    public void delete(Long id) {
        starshipRepository.deleteById(id);
    }

    @Override
    public Starship save(Starship starship) {
        return starshipRepository.save(starship);
    }
}


