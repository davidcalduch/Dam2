package com.fran.practica6.controllers.starship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fran.practica6.controllers.Valid;
import com.fran.practica6.models.entity.Starship;
import com.fran.practica6.models.services.IStarshipService;

@RestController
@RequestMapping("/api")
public class StarshipRestController {

    @Autowired
    private IStarshipService starshipService;

    @GetMapping("/starships")
    public List<Starship> index() {
        return starshipService.findAll();
    }

    @GetMapping("/starships/{codigo}")
    public ResponseEntity<?> getStarships(@PathVariable Long codigo) {
        Starship starship = starshipService.findById(codigo);
        if (starship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(starship, HttpStatus.OK);
    }

    @PutMapping("/starships/update/{codigo}")
    public ResponseEntity<?> updateStarship(@PathVariable Long codigo, @Valid @RequestBody Starship starship, BindingResult result) {
        System.out.println("Valor de codigo: " + codigo); // Depuración
    
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    
        Starship currentStarship = starshipService.findById(codigo);
        if (currentStarship == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error: no se pudo editar, la nave espacial ID: " + codigo + " no existe en la base de datos!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    
        try {
            currentStarship.setName(starship.getName());
            currentStarship.setModel(starship.getModel());
            currentStarship.setStarshipClass(starship.getStarshipClass());
            currentStarship.setManufacturer(starship.getManufacturer());
            currentStarship.setCostInCredits(starship.getCostInCredits());
            currentStarship.setLength(starship.getLength());
            currentStarship.setCrew(starship.getCrew());
            currentStarship.setPassengers(starship.getPassengers());
            currentStarship.setMaxAtmospheringSpeed(starship.getMaxAtmospheringSpeed());
            currentStarship.setHyperdriveRating(starship.getHyperdriveRating());
            currentStarship.setMglt(starship.getMglt());
            currentStarship.setCargoCapacity(starship.getCargoCapacity());
            currentStarship.setConsumables(starship.getConsumables());
    
            Starship updatedStarship = starshipService.update(currentStarship);
            return new ResponseEntity<>(updatedStarship, HttpStatus.OK);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar la nave espacial en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
