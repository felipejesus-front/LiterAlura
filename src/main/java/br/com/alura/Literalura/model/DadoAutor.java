package br.com.alura.Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadoAutor(@JsonAlias("name") String nome,
                        @JsonAlias("birth_year") Integer nascimento,
                        @JsonAlias("death_year") Integer morte) {
}
