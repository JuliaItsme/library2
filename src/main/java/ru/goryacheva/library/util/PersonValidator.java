package ru.goryacheva.library.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.goryacheva.library.models.Person;
import ru.goryacheva.library.service.PersonService;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        Person person = (Person) object;
        if (personService.getByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "This full name is already taken");
        }
    }
}

