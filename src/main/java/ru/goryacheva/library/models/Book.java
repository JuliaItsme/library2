package ru.goryacheva.library.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 100, message = "Name author should be between 2 and 100 characters")
    private String author;

    @Column(name = "year_of_production")
    @Min(value = 1000, message = "Year should be over 1000")
    private int yearOfProduction;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "take_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeAt;

    @Transient
    private Boolean isOld;

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getTakeAt() {
        return takeAt;
    }

    public void setTakeAt(Date takeAt) {
        this.takeAt = takeAt;
    }

    public Boolean isOld() {
        return isOld;
    }

    public void setOld(Boolean b) {
        isOld = b;
    }
}
