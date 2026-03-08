package br.com.alura.Literalura.repository;

import br.com.alura.Literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);


}
