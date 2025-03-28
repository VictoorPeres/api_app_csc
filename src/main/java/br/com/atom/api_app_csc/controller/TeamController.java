package br.com.atom.api_app_csc.controller;

import br.com.atom.api_app_csc.model.entity.PlayerDTO;
import br.com.atom.api_app_csc.model.entity.Team;
import br.com.atom.api_app_csc.model.entity.TeamDTO;
import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import br.com.atom.api_app_csc.service.ApiService;
import br.com.atom.api_app_csc.service.TeamService;
import br.com.atom.api_app_csc.service.TokenAcessoAppService;
import br.com.atom.api_app_csc.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/teams",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    TokenAcessoAppService tokenAcessoAppService;

    @Autowired
    ApiService apiService;

    @PostMapping(value = "/cadastro")
    public ResponseEntity<?> salvar(@RequestHeader("Authorization") String authHeader, @RequestBody Team team) {
        try {

            String token = authHeader.replace("Bearer ", "");
            TokenAcessoApp tokenAcessoApp = tokenAcessoAppService.findByCodigo(token);

            if(tokenAcessoApp == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token não localizado."));
            } else if (tokenAcessoApp.getStatus().ordinal() == StatusTokenAcessoApp.INATIVO.ordinal()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token expirado."));
            }else {
                String msg = teamService.validarTeam(team);
                if (msg != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseMessage(msg));
                }

                if (teamService.findByNickNameTeam(team.getNickNameTeam())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseMessage("Nickname indisponível."));
                }

                teamService.salvar(team);


                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseMessage("Time cadastrado."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao cadastrar o time: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/consulta/{teamId}")
    public ResponseEntity<?> consulta(@RequestHeader("Authorization") String authHeader, @PathVariable String teamId) {
        String token = authHeader.replace("Bearer ", "");
        TokenAcessoApp tokenAcessoApp = tokenAcessoAppService.findByCodigo(token);

        if(tokenAcessoApp == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token não localizado."));
        } else if (tokenAcessoApp.getStatus().ordinal() == StatusTokenAcessoApp.INATIVO.ordinal()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token expirado."));
        }else{
            TeamDTO teamDTO =  apiService.getTeamPorId(teamId);
            return ResponseEntity.status(HttpStatus.OK).body(teamDTO);
        }
    }
}
