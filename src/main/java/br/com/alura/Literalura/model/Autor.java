package br.com.alura.Literalura.model;


import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String nome;
    private Integer nascimento;
    private Integer morte;

    public Autor() {}

    public Autor(DadoAutor dadoAutor) {
        this.nome = dadoAutor.nome();
        this.nascimento = dadoAutor.nascimento();
        this.morte = dadoAutor.morte();
    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public Integer getNascimento() {
        return nascimento;
    }

    public void setNascimento(Integer nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getMorte() {
        return morte;
    }

    public void setMorte(Integer morte) {
        this.morte = morte;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "nome='" + nome + '\'' +
                ", nascimento=" + nascimento +
                ", morte=" + morte +
                '}';
    }
}
