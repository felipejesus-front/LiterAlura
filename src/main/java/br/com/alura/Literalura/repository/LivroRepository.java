package br.com.alura.Literalura.repository;

import br.com.alura.Literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    // Listar livros por idioma
    @Query("SELECT l FROM Livro l WHERE l.linguas LIKE CONCAT('%', :idioma, '%')")
    List<Livro> findByIdioma(@Param("idioma") String idioma);

}
