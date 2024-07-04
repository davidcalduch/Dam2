package com.fran.practica6.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fran.practica6.models.dao.IPersonDao;
import com.fran.practica6.models.entity.Person;

@Service
public class PersonServiceImpl implements IPersonService{
    @Autowired
    private IPersonDao personDao;

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return (List<Person>) personDao.findAll();
    }

    @Override
    public Person save(Person person) {
       return personDao.save(person); 
        
    }

    @Override
    @Transactional(readOnly = true)
    public Person findById(Long id) {
        return personDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long codigo) {
        personDao.deleteById(codigo);
    }
    @Override
    public Person update(Person person) {
        return personDao.save(person);
    }
    
}
