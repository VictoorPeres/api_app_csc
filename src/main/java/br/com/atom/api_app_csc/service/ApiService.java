package br.com.atom.api_app_csc.service;

import br.com.atom.api_app_csc.model.entity.PlayerDTO;
import br.com.atom.api_app_csc.model.entity.TeamDTO;
import br.com.atom.api_app_csc.model.entity.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://open.faceit.com/data/v4").build();
    }

    public PlayerDTO getPLayerPorId(String playerId) {
        try {
            String responseBody =  webClient.get()
                    .uri("/players/{playerId}", playerId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer 7262e81a-706a-4e68-9034-47b57f18303e") // Adicionando o cabeçalho Authorization com o token
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Bloqueia a execução para obter a resposta síncrona

            JSONObject jsonObject = new JSONObject(responseBody);
            PlayerDTO playerDTO = new PlayerDTO();
            playerDTO.setPlayerId(jsonObject.getString("player_id"));
            playerDTO.setNickName(jsonObject.getString("nickname"));
            playerDTO.setCountry(jsonObject.getString("country"));
            playerDTO.setDt_cadastro(jsonObject.getString("activated_at"));
            playerDTO.setSteamId64(jsonObject.getString("steam_id_64"));
            playerDTO.setSteamNickName(jsonObject.getString("steam_nickname"));
            playerDTO.setAvatarUrl(jsonObject.getString("avatar"));

            JSONObject gamesJson = jsonObject.getJSONObject("games");
            List<String> gameKeys = new ArrayList<>();
            gamesJson.keys().forEachRemaining(gameKeys::add);
            playerDTO.setGames(gameKeys);

            return playerDTO;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro ao chamar a API da Faceit: " + e.getMessage());
        }
    }

    public TeamDTO getTeamPorId(String teamId) {
        try {
            String responseBody =  webClient.get()
                    .uri("/teams/{teamId}", teamId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer 7262e81a-706a-4e68-9034-47b57f18303e") // Adicionando o cabeçalho Authorization com o token
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Bloqueia a execução para obter a resposta síncrona

            JSONObject jsonObject = new JSONObject(responseBody);
            TeamDTO teamDTO = new TeamDTO();
            teamDTO.setTeamId(jsonObject.getString("team_id"));
            teamDTO.setNickName(jsonObject.getString("nickname"));
            teamDTO.setName(jsonObject.getString("name"));
            teamDTO.setTeam_type(jsonObject.getString("team_type"));
            teamDTO.setAvatarUrl(jsonObject.getString("avatar"));
            teamDTO.setGame(jsonObject.getString("game"));


            return teamDTO;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro ao chamar a API da Faceit: " + e.getMessage());
        }
    }
}