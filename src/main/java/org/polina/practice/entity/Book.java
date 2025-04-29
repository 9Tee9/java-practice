package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.polina.practice.views.Views;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.AuthorSummary.class, Views.BookSummary.class})
    private Long id;
    @Column(length = 100)
    @JsonView({Views.AuthorSummary.class, Views.BookSummary.class})
    private String title;
    @Column(length = 500)
    @JsonView(Views.BookSummary.class)
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonView(Views.BookDetails.class)
    private List<Author> authors = new ArrayList<>();
}
