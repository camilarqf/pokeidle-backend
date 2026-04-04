package br.com.pokeidle.importacao.infrastructure;

import br.com.pokeidle.shared.domain.BusinessException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableConfigurationProperties(PokeApiProperties.class)
public class PokeApiClient {

    private final RestTemplate restTemplate;
    private final PokeApiProperties properties;

    public PokeApiClient(RestTemplate restTemplate, PokeApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public PokeApiPokemonResponse buscarPokemon(String nomeOuId) {
        return get("/pokemon/" + nomeOuId, PokeApiPokemonResponse.class);
    }

    public PokeApiSpeciesResponse buscarSpecies(String nomeOuId) {
        return get("/pokemon-species/" + nomeOuId, PokeApiSpeciesResponse.class);
    }

    private <T> T get(String path, Class<T> responseType) {
        String url = properties.baseUrl() + path;
        ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new BusinessException("Falha ao consultar a PokeAPI em " + path);
        }
        return response.getBody();
    }
}
