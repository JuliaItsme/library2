package ru.goryacheva.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.goryacheva.library.models.Person;
import ru.goryacheva.library.service.PersonService;
import ru.goryacheva.library.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("people", personService.getAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.get(id));
        model.addAttribute("books", personService.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult error) {
        personValidator.validate(person, error);
        if (error.hasErrors())
            return "people/new";

        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.get(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, @PathVariable("id") int id, BindingResult error) {
        if (error.hasErrors())
            return "people/edit";
        personService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }
}
