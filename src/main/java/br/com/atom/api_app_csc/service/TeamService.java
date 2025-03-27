package br.com.atom.api_app_csc.service;

import br.com.atom.api_app_csc.model.entity.Team;
import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import br.com.atom.api_app_csc.repository.TeamRepository;
import br.com.atom.api_app_csc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    TokenAcessoAppService tokenAcessoAppService;

    public String validarTeam(String authHeader, Team team) {
        String token = authHeader.replace("Bearer ", "");

        if (team == null || token == null) {
            return "Campos não preenchidos.";
        } else if (team.getTeamId() == null || team.getNameTeam() == null || team.getNickNameTeam() == null) {
            return "Campos não preenchidos.";
        }else{
            TokenAcessoApp tokenAcessoApp = tokenAcessoAppService.findByCodigo(token);

            if(tokenAcessoApp == null){
                return "Token não localizado.";
            } else if (tokenAcessoApp.getStatus().ordinal() == StatusTokenAcessoApp.INATIVO.ordinal()) {
                return "Token expirado.";
            }
        }
        return null;  // Caso esteja tudo ok
    }
    public void salvar(Team team) {
        teamRepository.save(team);  // Salva o usuário no repositório
    }

    public Boolean findByNickNameTeam(String nickName) {
        return teamRepository.findByNickNameTeam(nickName);
    }

}
