package br.com.atom.api_app_csc.controller;

import br.com.atom.api_app_csc.model.dto.PlayerDTO;
import br.com.atom.api_app_csc.model.dto.UsuarioDTO;
import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.dto.LoginDTO;
import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import br.com.atom.api_app_csc.model.enums.StatusUsuario;
import br.com.atom.api_app_csc.service.ApiService;
import br.com.atom.api_app_csc.service.TokenAcessoAppService;
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
        value = "/usuario",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    TokenAcessoAppService tokenAcessoAppService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private ApiService apiService;

    public UsuarioController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/cadastro")
    public ResponseEntity<?> salvar(@RequestBody Usuario usuario) {
        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            String msg = usuarioService.validarUsuario(usuario);
            if (msg != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage(msg));
            }

            if (usuarioService.findByEmail(usuario.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("E-mail já cadastrado."));
            }

            Usuario usuarioBanco = usuarioService.findByPlayerId(usuario.getPlayerId());

            if(usuarioBanco != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("PlayerId já cadastrado."));
            }

            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setStatusUsuario(StatusUsuario.ATIVO);
            usuario.setPassword(senhaCriptografada);
            usuarioService.salvar(usuario);

            usuario = usuarioService.findByPlayerId(usuario.getPlayerId());

            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setPlayerId(usuario.getPlayerId());
            usuarioDTO.setCpf(usuario.getCpf());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setNome(usuario.getNome());
            usuarioDTO.setUsername(usuario.getUsername());


            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(usuarioDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao cadastrado usuário: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            Usuario usuario = usuarioService.findByUsername(loginDTO.getUsername());
            String token = usuarioService.criarTokenAcessoApp(usuario);

            return ResponseEntity.ok(new ResponseMessage(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao autenticar: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/consulta/{playerId}")
    public ResponseEntity<?> consulta(@RequestHeader("Authorization") String authHeader, @PathVariable String playerId) {
        String token = authHeader.replace("Bearer ", "");
        TokenAcessoApp tokenAcessoApp = tokenAcessoAppService.findByCodigo(token);

        if(tokenAcessoApp == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token não localizado."));
        } else if (tokenAcessoApp.getStatus().ordinal() == StatusTokenAcessoApp.INATIVO.ordinal()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Token expirado."));
        }else{
            PlayerDTO playerDTO =  apiService.getPLayerPorId(playerId);
            return ResponseEntity.status(HttpStatus.OK).body(playerDTO);
        }
    }
}
