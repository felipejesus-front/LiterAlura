package br.com.alura.Literalura.principal;

//import br.com.alura.screenmatch.model.*;
//import br.com.alura.screenmatch.repository.SerieRepository;
//import br.com.alura.screenmatch.service.ConsumoApi;
//import br.com.alura.screenmatch.service.ConverteDados;

import br.com.alura.Literalura.model.DadosLivro;
import br.com.alura.Literalura.model.Livro;
import br.com.alura.Literalura.model.Resposta;
import br.com.alura.Literalura.repository.LivroRepository;
import br.com.alura.Literalura.service.ConsumoApi;
import br.com.alura.Literalura.service.ConverteDados;

import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books";
    private final String API_KEY = "&apikey=6585022c";
    private LivroRepository repositorio;

//    private List<Serie> series = new ArrayList<>();

    public Principal(LivroRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar livros
                    2 - -----
                    3 - -----
                    4 - -----
                    5 - -----
                    6 - -----
                    
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
//                    buscarEpisodioPorSerie();
                    break;
                case 3:
//                    listarSeriesBuscadas();
                    break;
                case 4:
//                    buscarSeriePorTitulo();
                    break;
                case 5:
//                    buscarSeriePorAtor();
                    break;
                case 6:
//                    buscarporCategoria();
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
                    repositorio.save(livro);
                    livrosSalvos++;
                    System.out.println("✓ Livro salvo: " + dadosLivro.titulo());
                } catch (Exception e) {
                    System.out.println("✗ Erro ao salvar " + dadosLivro.titulo() + ": " + e.getMessage());
                }
            } else {
                livrosJaExistem++;
            }
        }

        System.out.println("\n--- Resumo ---");
        System.out.println("Livros salvos: " + livrosSalvos);
        System.out.println("Livros já existentes: " + livrosJaExistem);
    }


//    private void buscarporCategoria() {
//        System.out.println("Digite o nome da categoria para busca: ");
//        var nomeCategoria = leitura.nextLine();
//
//        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
//
//        List<Serie> seriesEncontradas = repositorio.findByGenero(categoria);
//
//        if (!seriesEncontradas.isEmpty()) {
//            System.out.println("séries encontradas com a categoria: " + nomeCategoria);
//            seriesEncontradas.forEach(serie -> {
//                System.out.println(serie.getTitulo() + " - " + serie.getGenero());
//            });
//        } else {
//            System.out.println("Nenhuma série encontrada com a categoria: " + nomeCategoria);
//        }
//    }
//
//    private void buscarSeriePorAtor() {
//        System.out.println("Digite o nome do ator para busca: ");
//        var nomeAtor = leitura.nextLine();
//
//        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
//
//        if (!seriesEncontradas.isEmpty()) {
//            System.out.println("séries encontradas com o ator: " + nomeAtor);
//            seriesEncontradas.forEach(serie -> {
//                System.out.println(serie.getTitulo() + " - " + serie.getAvaliacao());
//            });
//        } else {
//            System.out.println("Nenhuma série encontrada com o ator: " + nomeAtor);
//        }
//    }
//
//    private void buscarSeriePorTitulo() {
//        System.out.println("Digite o nome da série para busca: ");
//        var nomeSerie = leitura.nextLine();
//
//        Optional<Serie> serieEncontrada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
//
//        if (serieEncontrada.isPresent()) {
//            System.out.println(serieEncontrada.get());
//        } else {
//            System.out.println("Série não encontrada");
//        }
//
//    }
//
//    private void buscarSerieWeb() {
//        DadosSerie dados = getDadosSerie();
//        Serie serie = new Serie(dados);
////        Antiga implementação para salvar as séries buscadas em memória, agora as séries são salvas no banco de dados
////        dadosSeries.add(dados);
//        repositorio.save(serie);
//        System.out.println(dados);
//    }
//
//    private DadosSerie getDadosSerie() {
//        System.out.println("Digite o nome da série para busca");
//        var nomeSerie = leitura.nextLine();
//        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
//        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//        return dados;
//    }
//
//    private void buscarEpisodioPorSerie(){
//
//        listarSeriesBuscadas();
//
//        System.out.println("Digite o nome da série para buscar os episódios: ");
//        var nomeSerie = leitura.nextLine();
//
//
//        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
//
//        if(serie.isPresent()){
//            var serieEncontrada = serie.get();
//
//            List<DadosTemporada> temporadas = new ArrayList<>();
//
//            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
//                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
//                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//                temporadas.add(dadosTemporada);
//            }
//            temporadas.forEach(System.out::println);
//
//            List<Episodio> episodios = temporadas
//                    .stream()
//                    .flatMap(d -> d.episodios().stream()
//                            .map(e -> new Episodio(d.numero(), e)))
//                    .collect(Collectors.toList());
//
//            serieEncontrada.setEpisodios(episodios);
//            repositorio.save(serieEncontrada);
//        } else {
//            System.out.println("Série não encontrada");
//        }
//    }
//    private void listarSeriesBuscadas() {
//        series = repositorio.findAll();
//
//        if (series.isEmpty()) {
//            System.out.println("Nenhuma série buscada");
//            return;
//        }
//
//        series.stream()
//            .sorted(Comparator.comparing(Serie::getGenero))
//            .forEach(System.out::println);
//
//        System.out.println(' ');
//    }
}