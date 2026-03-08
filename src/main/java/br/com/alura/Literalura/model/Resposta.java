package br.com.alura.Literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resposta {

    private Integer count;
    private String next;
    private String previous;
    private List<DadosLivro> results;

    public List<DadosLivro> getResults() {
        return results;
    }
}
