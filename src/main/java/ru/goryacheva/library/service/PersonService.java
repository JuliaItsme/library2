package ru.goryacheva.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.goryacheva.library.models.Book;
import ru.goryacheva.library.models.Person;
import ru.goryacheva.library.repository.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person get(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    public List<Book> getBooksByPersonId(int personId) {
        Optional<Person> person = repository.findById(personId);
        if (person.isPresent())
            return extracted(person.get());
        else
            return Collections.emptyList();
    }

    public Optional<Person> getByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }

    @Transactional
    public void save(Person newPerson) {
        repository.save(newPerson);
    }

    @Transactional
    public void update(int id, Person updatePerson) {
        updatePerson.setId(id);
        repository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    private List<Book> extracted(Person person) {
        person.getBooks().forEach(book -> {
            long times = Math.abs(new Date().getTime() - book.getTakeAt().getTime());
            long days = TimeUnit.DAYS.convert(times, TimeUnit.MILLISECONDS);
            if(days > 10)
                book.setOld(true);
        });
        return person.getBooks();
    }
}