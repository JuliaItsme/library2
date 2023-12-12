package ru.goryacheva.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.goryacheva.library.models.Book;
import ru.goryacheva.library.models.Person;
import ru.goryacheva.library.repository.BookRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book get(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Book> getAll(boolean sortByYear) {
        if(sortByYear)
            return repository.findAll(Sort.by("year_of_production"));
        else
            return repository.findAll();
    }

    public List<Book> getAllWithPagination(int page, int booksPerPage, boolean sortByYear) {
        if(sortByYear)
            return repository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year_of_production"))).getContent();
        else
            return repository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> getBooksByTitle(String title){
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        return repository.findByTitleContaining(title);
    }

    //возвращает null если у книги нет владельца
    public Person getPersonByBookId(int bookId) {
        return repository.findById(bookId).map(book -> book.getPerson()).orElse(null);
    }

    @Transactional
    public void save(Book newBook) {
        repository.save(newBook);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        Book oldBook = repository.findById(id).get();

        // добавляем новую книгу, которая не находится в Persistence context, поэтому нужен save()
        updateBook.setId(id);

        // чтобы не терялась связь при обновлении
        updateBook.setPerson(oldBook.getPerson());

        repository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    // Освобождает книгу (этот метод вызывается, когда человек возвращает книгу в библиотеку)
    @Transactional
    public void deletePersonForBook(int id) {
        repository.findById(id).ifPresent(b -> {
            b.setPerson(null);
            b.setTakeAt(null);
        });
    }

    // Назначает книгу человеку (этот метод вызывается, когда человек забирает книгу из библиотеки)
    @Transactional
    public void addPersonForBook(int id, Person person) {
        repository.findById(id).ifPresent(b -> {
            b.setPerson(person);
            b.setTakeAt(new Date());
        });
    }
}