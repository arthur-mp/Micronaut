package example.micronaut.repository.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import example.micronaut.domain.Genre;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Singleton
public class GenreRepositoryESImpl implements GenreRepositoryES{

    private static final Logger LOG = LoggerFactory.getLogger(GenreRepositoryESImpl.class);

    @Value("${elasticsearch.indexes.genres}")
    String genresIndex;

    private final ElasticsearchClient client;

    public GenreRepositoryESImpl(ElasticsearchClient client){
        this.client = client;
    }

    @Override
    public String saveGenreEs(String name) {
        Genre genre = new Genre(name);

        try{
            IndexRequest<Genre> indexRequest = new IndexRequest.Builder<Genre>()
                    .index(genresIndex)
                    .document(genre)
                    .build();

            IndexResponse indexResponse = client.index(indexRequest);
            String id = indexResponse.id();

            LOG.info("Document for '{}' {} successfully in ES. The id is: {}", genre, indexResponse.result(), id);
            return id;
        }catch (Exception e){
            String errorMessage = String.format("An exception occurred while indexing '%s'", genre);
            LOG.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Genre findByGenreName(String name) {
        if(name == null) return null;
        try {
            SearchResponse<Genre> searchResponse = client.search((s) ->
                    s.index(genresIndex)
                            .query(q -> q.match(m ->
                                    m.field("name")
                                            .query(name)
                            )), Genre.class
            );
            LOG.info("Searching for '{}' took {} and found {}", name, searchResponse.took(), searchResponse.hits().total().value());

            Iterator<Hit<Genre>> hits = searchResponse.hits().hits().iterator();
            if (hits.hasNext()) {
                Hit<Genre> hit = hits.next();
                Genre genre = hit.source();
                genre.setId(hit.id());
                return genre;
            }

            return null;

        } catch (Exception e) {
            String errorMessage = String.format("An exception occurred while searching for title '%s'", name);
            LOG.error(errorMessage);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Genre> listAllGenreEs() {
        try{
            SearchResponse<Genre> searchResponse = client.search((s) -> s.index(genresIndex), Genre.class);
            LOG.info("Listing all genre took {} and found {}", searchResponse.took(), searchResponse.hits().total().value());

            List<Genre> genres = new ArrayList<>();
            for (Hit<Genre> hit : searchResponse.hits().hits()) {
                Genre genre = hit.source();
                genre.setId(hit.id());
                genres.add(genre);
            }

            return genres;
        }catch (Exception e) {
            String errorMessage = "An exception occurred while listing all genres";
            LOG.error(errorMessage, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            DeleteRequest deleteRequest = DeleteRequest.of(d -> d.index(genresIndex).id(id));

            DeleteResponse deleteResponse = client.delete(deleteRequest);

            if (deleteResponse.result().name().equals("NotFound")) {
                LOG.warn("Document with ID '{}' not found for deletion.", id);
                return false;
            } else {
                LOG.info("Document with ID '{}' deleted successfully.", id);
                return true;
            }
        } catch (IOException e) {
            String errorMessage = String.format("An exception occurred while deleting document with ID '%s'", id);
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
