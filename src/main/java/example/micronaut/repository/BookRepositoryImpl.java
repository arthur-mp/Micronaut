package example.micronaut.repository;

import example.micronaut.configuration.ApplicationConfiguration;
import example.micronaut.configuration.SortingAndOrderArguments;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookRepositoryImpl implements BookRepository {

    private static final List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    private final EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @ReadOnly
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    @Transactional
    public Book save(@NotBlank String name, @NotBlank String isbn, Genre genre) {
        Book book = new Book(name, isbn, genre);
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @ReadOnly
    public List<Book> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT b FROM Book as b";
        if (args.order() != null && args.sort() != null && VALID_PROPERTY_NAMES.contains(args.sort())) {
            qlString += " ORDER BY b." + args.sort() + ' ' + args.order().toLowerCase();
        }
        TypedQuery<Book> query = entityManager.createQuery(qlString, Book.class);
        query.setMaxResults(args.max() != null ? args.max() : applicationConfiguration.getMax());
        if (args.offset() != null) {
            query.setFirstResult(args.offset());
        }
        return query.getResultList();
    }

    @Override
    @Transactional
    public int update(long id, @NotBlank String name, @NotBlank String isbn, Genre genre) {
        return entityManager.createQuery("UPDATE Book b SET name = :name, isbn = :isbn, genre = :genre where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .setParameter("isbn", isbn)
                .setParameter("genre", genre)
                .executeUpdate();
    }

    @Override
    @Transactional
    public Book saveWithException(@NotBlank String name, @NotBlank String isbn, Genre genre) {
        save(name, isbn, genre);
        throw new PersistenceException();
    }
}