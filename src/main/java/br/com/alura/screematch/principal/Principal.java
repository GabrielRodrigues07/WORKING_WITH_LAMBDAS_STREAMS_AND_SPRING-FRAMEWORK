package br.com.alura.screematch.principal;

import br.com.alura.screematch.model.DadosEpisodio;
import br.com.alura.screematch.model.DadosSerie;
import br.com.alura.screematch.model.DadosTemporada;
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

    public void exibMenu(){
        System.out.println("Dígite o nome da série para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = converte.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> dadosTemporadaList = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            String temporadaJson = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY + TEMPORADA + i);
            DadosTemporada dadosTemporada = converte.obterDados(temporadaJson, DadosTemporada.class);
            dadosTemporadaList.add(dadosTemporada);
        }

        dadosTemporadaList.forEach(System.out::println);
        
        dadosTemporadaList.forEach(dadosTemporada -> {
            if (!dadosTemporada.episodios().isEmpty()) {
                dadosTemporada.episodios().forEach(dadosEpisodio -> System.out.println(dadosEpisodio.titulo()));
            }
        });

        List<DadosEpisodio> dadosEpisodioList = dadosTemporadaList.stream()
                .flatMap(dadosTemporada -> dadosTemporada.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodioList.stream()
                .filter(dadosEpisodio -> !Objects.equals(dadosEpisodio.avaliacao(), "N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

    }
}
