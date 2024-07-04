package   com.fran.practica6.models.services;

import java.util.List;

import com.fran.practica6.models.entity.Person;
public interface IPersonService{
    public List<Person> findAll();
    public Person save (Person person);
    public Person findById(Long id);
    public void delete(Long id);
    public Person update(Person person);
}