package com.fran.practica6.controllers;

import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fran.practica6.models.entity.Cliente;
import com.fran.practica6.models.services.ClienteServiceImpl;

@Controller
public class IndexController {
	
	@GetMapping({"/", "", "/index", "/home"})
	public String index(Model model)
	{
		model.addAttribute("titulo", "Springboot backend API MVC");
		return "index";
	}

}
