package br.com.alura.screematch;

import br.com.alura.screematch.service.ConsumoApi;
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

		var json = service.obterDados("https://www.omdbapi.com/?apikey=afaa114e&t=supernatural");

		System.out.println(json);
	}
}
