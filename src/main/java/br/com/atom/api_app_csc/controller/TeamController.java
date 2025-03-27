package br.com.atom.api_app_csc.controller;

import br.com.atom.api_app_csc.model.entity.Team;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.entity.UsuarioDTO;
import br.com.atom.api_app_csc.model.enums.StatusUsuario;
import br.com.atom.api_app_csc.service.TeamService;
import br.com.atom.api_app_csc.service.UsuarioService;
import br.com.atom.api_app_csc.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping(value = "/cadastro")
    public ResponseEntity<?> salvar(@RequestHeader("Authorization") String authHeader, @RequestBody Team team) {
        try {
            String msg = teamService.validarTeam(authHeader, team);
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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao cadastrar o time: " + e.getMessage()));
        }
    }
}
