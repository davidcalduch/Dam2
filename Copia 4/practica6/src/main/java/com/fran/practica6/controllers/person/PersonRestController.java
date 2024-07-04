package com.fran.practica6.controllers.person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fran.practica6.controllers.Valid;
import com.fran.practica6.models.entity.Person;
import com.fran.practica6.models.services.IPersonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PersonRestController {

    @Autowired
    private IPersonService personService;

    @GetMapping("/people")
    public List<Person> index() {
        return personService.findAll();
    }

    @GetMapping("/people/{codigo}")
    public ResponseEntity<?> getPeople(@PathVariable Long codigo) {
        Person person = personService.findById(codigo);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @PutMapping("/people/update/{codigo}")
    public ResponseEntity<?> updatePerson(@PathVariable Long codigo, @Valid @RequestBody Person person, BindingResult result) {
        System.out.println("Valor de codigo: " + codigo); // Depuración
    
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    
        Person currentPerson = personService.findById(codigo);
        if (currentPerson == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error: no se pudo editar, la persona ID: " + codigo + " no existe en la base de datos!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    
        try {
            currentPerson.setName(person.getName());
            currentPerson.setBirthYear(person.getBirthYear());
            currentPerson.setGender(person.getGender());
            currentPerson.setHairColor(person.getHairColor());
            currentPerson.setSkinColor(person.getSkinColor());
            currentPerson.setEyeColor(person.getEyeColor());
            currentPerson.setHeight(person.getHeight());
            currentPerson.setMass(person.getMass());
            currentPerson.setHomeworld(person.getHomeworld());
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar la persona en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "La persona ha sido actualizada con éxito!");
        response.put("persona", currentPerson);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
