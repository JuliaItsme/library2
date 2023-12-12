package ru.goryacheva.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.goryacheva.library.models.Book;
import ru.goryacheva.library.models.Person;
import ru.goryacheva.library.service.BookService;
import ru.goryacheva.library.service.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public BookController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String getAllBooks(@RequestParam(name = "page", required = false) Integer page,
                              @RequestParam(name = "books_Per_Page", required = false) Integer booksPerPage,
                              @RequestParam(name = "sort_by_year", required = false) boolean sortByYear, Model model) {
        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.getAll(sortByYear));
        else
            model.addAttribute("books", bookService.getAllWithPagination(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, @ModelAttribute("person") Person p, Model model) {
        model.addAttribute("book", bookService.get(id));
        Person person = bookService.getPersonByBookId(id);
        if (person != null)
            model.addAttribute("person", person);
        else
            model.addAttribute("people", personService.getAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult error) {
        if (error.hasErrors())
            return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.get(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id, BindingResult error) {
        if (error.hasErrors())
            return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/clean")
    public String cleanPerson(@PathVariable("id") int id) {
        bookService.deletePersonForBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/choose")
    public String choosePerson(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        // У person назначено только поле id, остальные поля - null
        bookService.addPersonForBook(id, person);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBooks(@RequestParam("query") String title, Model model) {
        model.addAttribute("books", bookService.getBooksByTitle(title));
        return "books/search";
    }
}
