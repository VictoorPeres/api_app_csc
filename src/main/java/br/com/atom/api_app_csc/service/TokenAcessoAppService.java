package br.com.atom.api_app_csc.service;

import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.repository.TokenAcessoAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class TokenAcessoAppService {

    @Autowired
    private TokenAcessoAppRepository tokenAcessoAppRepository;

    public TokenAcessoApp save(TokenAcessoApp tokenAcessoApp){
        return tokenAcessoAppRepository.save(tokenAcessoApp);
    }

    public TokenAcessoApp update(TokenAcessoApp tokenAcessoApp){
        return tokenAcessoAppRepository.update(tokenAcessoApp);
    }

    @Transactional(readOnly = true)
    public TokenAcessoApp findByCodigo(String token) {
        return tokenAcessoAppRepository.findByCodigo(token);
    }

    public List<TokenAcessoApp> getTokenAcessoAppAtivos(Usuario usuario){
        return tokenAcessoAppRepository.getTokenAcessoAppAtivos(usuario);
    }
    public void desabilitarTokensAtivos(Usuario usuario){
        tokenAcessoAppRepository.desabilitarTokensAtivos(usuario);
    }
}