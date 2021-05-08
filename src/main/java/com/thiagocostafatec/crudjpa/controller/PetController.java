package com.thiagocostafatec.crudjpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thiagocostafatec.crudjpa.entity.Pet;
import com.thiagocostafatec.crudjpa.repository.PetRepository;

@Controller
@RequestMapping("/pets/")
public class PetController {

	@Autowired
	private PetRepository petRepository;

	@GetMapping("showForm")
	public String showPetForm(Pet pet) {
		return "add-pet";
	}

	@GetMapping("list")
	public String pets(Model model) {
		model.addAttribute("pets", this.petRepository.findAll());
		return "index";
	}

	@PostMapping("add")
	public String addPet(@Valid Pet pet, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-pet";
		}

		this.petRepository.save(pet);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Pet pet = this.petRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid pet id : " + id));

		model.addAttribute("pet", pet);
		return "update-pet";
	}

	@PostMapping("update/{id}")
	public String updatePet(@PathVariable("id") long id, @Valid Pet pet, BindingResult result, Model model) {
		if (result.hasErrors()) {
			pet.setId(id);
			return "update-pet";
		}

		// update pet
		petRepository.save(pet);

		// get all pets ( with update)
		model.addAttribute("pets", this.petRepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deletePet(@PathVariable("id") long id, Model model) {

		Pet pet = this.petRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid pet id : " + id));

		this.petRepository.delete(pet);
		model.addAttribute("pets", this.petRepository.findAll());
		return "index";

	}
}