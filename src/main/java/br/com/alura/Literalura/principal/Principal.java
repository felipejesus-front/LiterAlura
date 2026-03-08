package br.com.alura.Literalura.principal;

//import br.com.alura.screenmatch.model.*;
//import br.com.alura.screenmatch.repository.SerieRepository;
//import br.com.alura.screenmatch.service.ConsumoApi;
//import br.com.alura.screenmatch.service.ConverteDados;

import br.com.alura.Literalura.model.DadosLivro;
import br.com.alura.Literalura.model.Livro;
import br.com.alura.Literalura.model.Autor;
import br.com.alura.Literalura.model.Resposta;
import br.com.alura.Literalura.repository.LivroRepository;
import br.com.alura.Literalura.repository.AutorRepository;
import br.com.alura.Literalura.service.ConsumoApi;
import br.com.alura.Literalura.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final LivroRepository repositorio;
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books";
    private AutorRepository autorRepositorio;

//    private List<Serie> series = new ArrayList<>();

    public Principal(LivroRepository repositorio, AutorRepository autorRepositorio) {
        this.repositorio = repositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar livros
                    2 - listar livros registrados (no banco)
                    3 - listar autores registrados (no banco)
                    4 - listar autores vivos em um determinado ano (no banco)
                    5 - listar livros por idioma (no banco)
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroWeb();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEmAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private List<DadosLivro> getDadosLivro() {

        System.out.println("Digite o nome do livro para busca");
        var nomeLivro = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + "/?search=" + nomeLivro.replace(" ", "+"));

        Resposta resposta = conversor.obterDados(json, Resposta.class);

        List<DadosLivro> livros = resposta.getResults();
        return livros;
    }

    private void buscarLivroWeb() {
        List<DadosLivro> dadosLivros = getDadosLivro();

        if (dadosLivros.isEmpty()) {
            System.out.println("Nenhum livro encontrado!");
            return;
        }

        int livrosSalvos = 0;
        int livrosJaExistem = 0;

        for (DadosLivro dadosLivro : dadosLivros) {
            if (repositorio.findByTituloContainingIgnoreCase(dadosLivro.titulo()).isEmpty()) {
                try {
                    Livro livro = new Livro(dadosLivro);

                    List<Autor> autoresProcessados = livro.getAutores().stream()
                            .map(autor -> buscarOuCriarAutor(autor))
                            .collect(Collectors.toList());

                    livro.setAutores(autoresProcessados);
                    repositorio.save(livro);
                    livrosSalvos++;
                    System.out.println("Livro salvo: " + dadosLivro.titulo());
                } catch (Exception e) {
                    System.out.println("Erro ao salvar " + dadosLivro.titulo() + ": " + e.getMessage());
                }
            } else {
                livrosJaExistem++;
            }
        }

        System.out.println("\n--- Resumo ---");
        System.out.println("Livros salvos: " + livrosSalvos);
        System.out.println("Livros já existentes: " + livrosJaExistem);
    }

    private Autor buscarOuCriarAutor(Autor autor) {
        var autorExistente = autorRepositorio.findByNome(autor.getName());

        if (autorExistente.isPresent()) {
            return autorExistente.get();
        } else {
            return autorRepositorio.save(autor);
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = repositorio.findAll();

        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado no banco de dados.");
            return;
        }

        System.out.println("\n--- LIVROS REGISTRADOS ---");
        livros.forEach(livro -> {
            System.out.println("\n" + livro.getTitulo());
            System.out.println("   Autores: " + livro.getAutores());
            System.out.println("   Idiomas: " + livro.getLinguas());
        });
        System.out.println("\nTotal de livros: " + livros.size());
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();

        if (autores.isEmpty()) {
            System.out.println("\nNenhum autor registrado no banco de dados.");
            return;
        }

        System.out.println("\n--- AUTORES REGISTRADOS ---");
        autores.forEach(autor -> {
            System.out.println("\n " + autor.getName());
            System.out.println("   Nascimento: " + autor.getNascimento());
            System.out.println("   Morte: " + (autor.getMorte() != null ? autor.getMorte() : "Vivo(a)"));
        });
        System.out.println("\nTotal de autores: " + autores.size());
    }

    private void listarAutoresVivosEmAno() {
        System.out.println("\nDigite o ano para buscar autores vivos: ");
        Integer ano = leitura.nextInt();
        leitura.nextLine();

        List<Autor> autoresVivos = autorRepositorio.findAutoresVivosEmAno(ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("\nNenhum autor vivo encontrado no ano " + ano);
            return;
        }

        System.out.println("\n--- AUTORES VIVOS EM " + ano + " ---");
        autoresVivos.forEach(autor -> {
            System.out.println("\n " + autor.getName());
            System.out.println("   Nascimento: " + autor.getNascimento());
            System.out.println("   Morte: " + (autor.getMorte() != null ? autor.getMorte() : "Vivo(a)"));
        });
        System.out.println("\nTotal: " + autoresVivos.size());
    }

    private void listarLivrosPorIdioma() {
        System.out.println("\nDigite o idioma para buscar (código como 'pt', 'en', 'es', etc): ");
        String idioma = leitura.nextLine();

        List<Livro> livrosPorIdioma = repositorio.findByIdioma(idioma);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("\nNenhum livro encontrado no idioma: " + idioma);
            return;
        }

        System.out.println("\n--- LIVROS EM " + idioma.toUpperCase() + " ---");
        livrosPorIdioma.forEach(livro -> {
            System.out.println("\n " + livro.getTitulo());
            System.out.println("   Autores: " + livro.getAutores());
            System.out.println("   Idiomas: " + livro.getLinguas());
        });
        System.out.println("\nTotal de livros: " + livrosPorIdioma.size());
    }
}