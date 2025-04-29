package org.polina.practice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.polina.practice.views.Views;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authors")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.AuthorSummary.class, Views.BookDetails.class})
    private Long id;
    @Column(length = 70)
    @JsonView({Views.AuthorSummary.class, Views.BookDetails.class})
    private String name;
    @Column(length = 500)
    @JsonView({Views.AuthorSummary.class, Views.BookDetails.class})
    private String bio;
    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Book> books = new ArrayList<>();
}
