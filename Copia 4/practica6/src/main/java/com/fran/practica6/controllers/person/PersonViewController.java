package com.fran.practica6.controllers.person;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fran.practica6.models.entity.Person;
import com.fran.practica6.models.entity.Starship;
import com.fran.practica6.models.services.IPersonService;

@Controller
@RequestMapping("/persons")
public class PersonViewController {

    @Autowired
    private IPersonService personService;

    @GetMapping("/delete")
    public String deletePerson(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("titulo", "Eliminar Person");
        return "persons/delete";
    }
    @PostMapping("/delete")
    public String deletePerson(@ModelAttribute("person") Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "persons/delete";
        }
        personService.delete(person.getCodigo());
        return "redirect:/persons/listarPersonas";
    }

    @GetMapping("/persons/{codigo}")
    public String getPerson(@PathVariable Long codigo, Model model) {
        Person person = personService.findById(codigo);
        if (person == null) {
            return "error404"; // Por ejemplo, si tienes una vista llamada error404 para mostrar errores de recurso no encontrado
        }
        model.addAttribute("person", person);
        return "persons/listarPersonas"; // Esto supone que tienes una vista llamada personDetails para mostrar los detalles de la persona
    }
    @GetMapping("/listarPersonas")
    public String listar(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        model.addAttribute("titulo", "Listado de Personas");
        return "persons/listarPersonas";
    }
    @GetMapping("/update")
    public String showUpdatePersonFormGet(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("titulo", "Actualizar Person");
        return "persons/update";
    }
    @PostMapping("/update")
    public String showUpdatePersonFormPost(@ModelAttribute("person") Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "persons/update";
        }
        personService.save(person);
        return "redirect:/persons/listarPersonas";
    }
    @GetMapping("/persons/update/{codigo}") 
    public String showUpdatePersonFormGet(@PathVariable Long codigo, Model model) {
        Person person = personService.findById(codigo);
        if (person == null) {
            return "error404"; // Por ejemplo, si tienes una vista llamada error404 para mostrar errores de recurso no encontrado
        }
        model.addAttribute("person", person);
        model.addAttribute("titulo", "Actualizar Person");
        return "persons/update";
    }
    @PostMapping("/update/{codigo}")
    public ResponseEntity<?> updatePerson(@PathVariable Long codigo, @RequestBody Person person) {
        Person currentPerson = personService.findById(codigo);
        if (currentPerson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPerson.setName(person.getName());
        currentPerson.setBirthYear(person.getBirthYear());
        currentPerson.setGender(person.getGender());
        currentPerson.setHairColor(person.getHairColor());
        currentPerson.setSkinColor(person.getSkinColor());
        currentPerson.setEyeColor(person.getEyeColor());
        currentPerson.setHeight(person.getHeight());
        currentPerson.setMass(person.getMass());
        currentPerson.setHomeworld(person.getHomeworld());

        personService.save(currentPerson);
        return new ResponseEntity<>(currentPerson, HttpStatus.OK);
    }
    @GetMapping
    public String index(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        model.addAttribute("titulo", "Listado de Personas");
        return "persons/index";
    }
    @PostMapping("/update{codigo}")
    public String updatePerson(@PathVariable Long codigo, @ModelAttribute("person") Person person, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "persons/update"; // Si hay errores de validación, regresa a la página de actualización
        }
       Person currentPerson = personService.findById(codigo);
        if (currentPerson == null) {
            model.addAttribute("error", "No se ha encontrado la persona con el código " + codigo);
            return "persons/update"; // Si no se encuentra la persona, regresa a la página de actualización
        }
    
        // Actualiza los detalles de la persona con los nuevos datos
        currentPerson.setName(person.getName());
        currentPerson.setBirthYear(person.getBirthYear());
        currentPerson.setGender(person.getGender());
        currentPerson.setHairColor(person.getHairColor());
        currentPerson.setSkinColor(person.getSkinColor());
        currentPerson.setEyeColor(person.getEyeColor());
        currentPerson.setHeight(person.getHeight());
        currentPerson.setMass(person.getMass());
        currentPerson.setHomeworld(person.getHomeworld());
        personService.update(currentPerson);
    
        // Redirecciona a la página de detalles de la persona actualizada
        return "redirect:/people/" + codigo; // Suponiendo que "/people/{id}" es la página de detalles de la persona
    }
    @GetMapping("/anyadir")
    public String showAddPersonForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("titulo", "Añadir Person");
        return "persons/anyadir";
    }
    @PostMapping("/anyadir")
    public String addPerson(@ModelAttribute("person") Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "people/anyadir";
        }
        personService.save(person);
        return "redirect:/persons/listarPersonas";
    }
    @PostMapping("/load")
    public String loadPeople(@RequestParam Long codigo , Model model) {
        Person person = personService.findById(codigo);
       if(person == null){
        model.addAttribute("error", "No se ha encontrado el personaje con el código " + codigo);
        return "persons/update"; 
       }
        model.addAttribute("person", person);
        model.addAttribute("titulo", "Actualizar Person");
        return "persons/update";
    }
    
    
}
