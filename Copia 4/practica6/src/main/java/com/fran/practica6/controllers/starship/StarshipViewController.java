package com.fran.practica6.controllers.starship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fran.practica6.models.entity.Starship;
import com.fran.practica6.models.services.IStarshipService;

import java.util.List;

@Controller
@RequestMapping("/starships")
public class StarshipViewController {

    @Autowired
    private IStarshipService starshipService;

    @GetMapping("/delete")
    public String deleteStarship(Model model) {
        model.addAttribute("starship", new Starship());
        model.addAttribute("titulo", "Eliminar Starship");
        return "starships/delete";
    }

    @PostMapping("/delete")
    public String deleteStarship(@ModelAttribute("starship") Starship starship, BindingResult result) {
        if (result.hasErrors()) {
            return "starships/delete";
        }
        starshipService.delete(starship.getCodigo());
        return "redirect:/starships/listar";
    }

    @GetMapping("/starships/{codigo}")
    public ResponseEntity<?> getStarship(@PathVariable Long codigo) {
        Starship starship = starshipService.findById(codigo);
        if (starship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(starship, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        List<Starship> starships = starshipService.findAll();
        model.addAttribute("starships", starships);
        model.addAttribute("titulo", "Listado de Naves Estelares");
        return "starships/listar";
    }

    @GetMapping("/update")
    public String showUpdateStarshipFormGet(Model model) {
        model.addAttribute("starship", new Starship());
        model.addAttribute("titulo", "Actualizar Starship");
        return "starships/update";
    }

    @PostMapping("/update")
    public String showUpdateStarshipFormPost(@ModelAttribute("starship") Starship starship, BindingResult result) {
        if (result.hasErrors()) {
            return "starships/update";
        }
        starshipService.update(starship);
        return "redirect:/starships/listar";
    }

    @GetMapping("starships/update/{codigo}")
    public String showUpdateStarshipForm(@PathVariable Long codigo, Model model) {
        Starship starship = starshipService.findById(codigo);
        if (starship == null) {
            return "error/404"; // Manejo de error si la nave espacial no se encuentra
        }
        model.addAttribute("starship", starship);
        model.addAttribute("titulo", "Actualizar Starship");
        return "starships/update";
    }

    @PutMapping("/update/{codigo}")
    public ResponseEntity<?> updateStarship(@PathVariable Long codigo, @RequestBody Starship starship) {
        Starship currentStarship = starshipService.findById(codigo);
        if (currentStarship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentStarship.setName(starship.getName());
        currentStarship.setModel(starship.getModel());
        currentStarship.setStarshipClass(starship.getStarshipClass());
        currentStarship.setManufacturer(starship.getManufacturer());
        currentStarship.setCostInCredits(starship.getCostInCredits());
        currentStarship.setLength(starship.getLength());
        currentStarship.setCrew(starship.getCrew());

        starshipService.update(currentStarship);
        return new ResponseEntity<>(currentStarship, HttpStatus.OK);
    }

    @GetMapping("/update/form/{codigo}")
    public String showUpdateStarshipFormWithForm(@PathVariable Long codigo, Model model) {
        Starship starship = starshipService.findById(codigo);
        if (starship == null) {
            return "error/404"; // Manejo de error si la nave espacial no se encuentra
        }
        model.addAttribute("starship", starship);
        model.addAttribute("titulo", "Actualizar Starship");
        return "starships/update";
    }

    @GetMapping
    public String indexStarships(Model model) {
        model.addAttribute("titulo", "Starships");
        model.addAttribute("starships", starshipService.findAll());
        return "starships/index";
    }
   @PostMapping("/update/{codigo}")
public String updateStarship(@PathVariable Long codigo, @ModelAttribute("starship") Starship starship, BindingResult result, Model model) {
    if (result.hasErrors()) {
        return "starships/update";
    }
    Starship existingStarship = starshipService.findById(codigo);
    if (existingStarship == null) {
        model.addAttribute("error", "No se ha encontrado la nave espacial con el código " + codigo);
        return "starships/update"; // Manejo de error si la nave no se encuentra
    }
    
    existingStarship.setName(starship.getName());
    existingStarship.setModel(starship.getModel());
    // Actualizar el resto de los campos aquí
    starshipService.update(existingStarship);
    return "redirect:/starships/listar";
}

    
    

    @GetMapping("/anyadir")
    public String showAddStarshipForm(Model model) {
        model.addAttribute("titulo", "Añadir Starship");
        model.addAttribute("starship", new Starship());
        return "starships/anyadir";
    }

    @PostMapping("/anyadir")
    public String addStarship(@ModelAttribute("starship") Starship starship, BindingResult result) {
        if (result.hasErrors()) {
            return "starships/anyadir";
        }
        starshipService.save(starship);
        return "redirect:/starships/listar";
    }
    @PostMapping("/load")
    public String loadStarship(@RequestParam Long codigo, Model model) {
        Starship starship = starshipService.findById(codigo);
        if (starship == null) {
            model.addAttribute("error", "No se ha encontrado la nave espacial con el código " + codigo);
            return "starships/update"; // Manejo de error si la nave espacial no se encuentra
        }
        model.addAttribute("starship", starship);
        model.addAttribute("titulo", "Actualizar Starship");
        return "starships/update"; // Página con el formulario de actualización
    }
}
