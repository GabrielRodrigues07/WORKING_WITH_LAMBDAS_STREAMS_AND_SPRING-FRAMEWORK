package br.com.alura.screematch.principal;

import br.com.alura.screematch.model.DadosSerie;
import br.com.alura.screematch.model.DadosTemporada;
import br.com.alura.screematch.model.Episodio;
import br.com.alura.screematch.service.ConsumoApi;
import br.com.alura.screematch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados converte = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String TEMPORADA = "&season=";
    private final String API_KEY = "&apikey=6585022c";

    //TODO apikey=afaa114e

    public void exibeMenu(){
        System.out.println("Dígite o nome da série para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = converte.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);
//
        List<DadosTemporada> dadosTemporadaList = new ArrayList<>();
//
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            String temporadaJson = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY + TEMPORADA + i);
            DadosTemporada dadosTemporada = converte.obterDados(temporadaJson, DadosTemporada.class);
            dadosTemporadaList.add(dadosTemporada);
        }
//
//        dadosTemporadaList.forEach(System.out::println);
//
//        dadosTemporadaList.forEach(dadosTemporada -> {
//            if (!dadosTemporada.episodios().isEmpty()) {
//                dadosTemporada.episodios().forEach(dadosEpisodio -> System.out.println(dadosEpisodio.titulo()));
//            }
//        });
//
//        List<DadosEpisodio> dadosEpisodioList = dadosTemporadaList.stream()
//                .flatMap(dadosTemporada -> dadosTemporada.episodios().stream())
//                .collect(Collectors.toList());
//
//        System.out.println("\nTop 5 episódios");
//        dadosEpisodioList.stream()
//                .filter(dadosEpisodio -> !Objects.equals(dadosEpisodio.avaliacao(), "N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episodio> episodioList = dadosTemporadaList.stream()
                .flatMap(dadosTemporada -> dadosTemporada.episodios().stream()
                        .map(dadosEpisodio -> new Episodio(dadosTemporada.numero(), dadosEpisodio)))
                .collect(Collectors.toList());

//        System.out.println("Digite o titulo do episodio que deseja buscar: ");
//        var titulo = leitura.nextLine();
//
//        Optional<Episodio> ep = episodioList.stream()
//                .filter(episodio -> episodio.getTitulo().toUpperCase().contains(titulo.toUpperCase()))
//                .findFirst();
//
//        if (ep.isPresent()) {
//            System.out.println("Episodio encontrado!");
//            System.out.println("Temporada " + ep.get().getTemporada());
//        }
//        else {
//            System.out.println("Episodio não encontrado!");
//        }

        Map<Integer, Double> collect = episodioList.stream()
                .filter(episodio -> episodio.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        collect.forEach((temporada, avaliacao) -> System.out.printf("Temporada: %d - Avaliação: %.2f%n", temporada, avaliacao));

        DoubleSummaryStatistics doubleSummaryStatistics = episodioList.stream()
                .filter(episodio -> episodio.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.printf(" Média: %.2f%n Melhor episódio: %.2f%n Pior episódio: %.2f%n Quantidade: %d",
                doubleSummaryStatistics.getAverage(),
                doubleSummaryStatistics.getMax(),
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getCount());

//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        int ano = leitura.nextInt();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodioList.stream()
//                .filter(episodio -> Objects.nonNull(episodio.getDataLancamento()) && episodio.getDataLancamento()
//                        .isAfter(LocalDate.of(ano, 1, 1)))
//                .forEach(episodio -> System.out.printf("Temporada: %d " +
//                        "Episódio: %s " +
//                        "Data de lançamento: %s%n", episodio.getTemporada(), episodio.getTitulo(), episodio.getDataLancamento().format(formatter)));
//
    }
}
