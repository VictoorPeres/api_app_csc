package br.com.atom.api_app_csc.service;

import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import br.com.atom.api_app_csc.repository.UsuarioRepository;
import br.com.atom.api_app_csc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    TokenAcessoAppService tokenAcessoAppService;

    public String validarUsuario(Usuario usuario) {
        if (usuario == null) {
            return "Campos não preenchidos.";
        } else if (usuario.getCpf() == null || usuario.getEmail() == null || usuario.getNome() == null ||
                usuario.getUsername() == null || usuario.getSenha() == null || usuario.getConfirmaSenha() == null) {
            return "Campos não preenchidos.";
        }else if (!usuario.getSenha().equals(usuario.getConfirmaSenha())) {
            return "As senhas não coincidem";
        }
        return null;  // Caso esteja tudo ok
    }
    public void salvar(Usuario usuario) {
        usuarioRepository.save(usuario);  // Salva o usuário no repositório
    }

    public Boolean findByEmail(String email) {
       return usuarioRepository.findByEmail(email);
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public String criarTokenAcessoApp(Usuario usuario){

        String codigoTokenRetorno = "";
        try{
            tokenAcessoAppService.desabilitarTokensAtivos(usuario);

            Calendar dataReferencia = Calendar.getInstance();

            TokenAcessoApp novoToken  = new TokenAcessoApp();
            novoToken.setStatus(StatusTokenAcessoApp.ATIVO);
            novoToken.setDataCriacao(dataReferencia.getTime());

            dataReferencia.add(Calendar.MINUTE,10);
            novoToken.setDataExpiracao(dataReferencia.getTime());
            novoToken.setUsuario(usuario);
            novoToken.setToken(JwtUtil.generateToken(usuario.getUsername()));

            tokenAcessoAppService.save(novoToken);

            codigoTokenRetorno = novoToken.getToken();

        }catch (Exception e){
            e.printStackTrace();
            codigoTokenRetorno = "Erro";
        }

        return codigoTokenRetorno;
    }
}
