package br.com.alura.screematch;

import br.com.alura.screematch.model.DadosEpisodio;
import br.com.alura.screematch.model.DadosSerie;
import br.com.alura.screematch.service.ConsumoApi;
import br.com.alura.screematch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreematchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreematchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var service = new ConsumoApi();
		var convert = new ConverteDados();

		var jsonS = service.obterDados("https://www.omdbapi.com/?apikey=afaa114e&t=supernatural");
		var jsonE = service.obterDados("https://www.omdbapi.com/?apikey=afaa114e&t=supernatural&season=1&episode=1");

		DadosSerie dadosSerie = convert.obterDados(jsonS, DadosSerie.class);
		DadosEpisodio dadosEpisodio = convert.obterDados(jsonE, DadosEpisodio.class);

		System.out.println(dadosSerie);
		System.out.println(dadosEpisodio);
	}
}
