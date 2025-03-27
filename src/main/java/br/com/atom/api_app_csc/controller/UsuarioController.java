package br.com.atom.api_app_csc.controller;

import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.entity.UsuarioDTO;
import br.com.atom.api_app_csc.model.enums.StatusUsuario;
import br.com.atom.api_app_csc.repository.UsuarioRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UsuarioController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/cadastro")
    public ResponseEntity<?> salvar(@RequestBody Usuario usuario) {
        try {
            String msg = usuarioService.validarUsuario(usuario);
            if (msg != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage(msg));
            }

            if (usuarioService.findByEmail(usuario.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("E-mail já cadastrado."));
            }

            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setStatusUsuario(StatusUsuario.ATIVO);
            usuario.setPassword(senhaCriptografada);
            usuarioService.salvar(usuario);


            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Usuário cadastrado."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao salvar usuário: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getPassword())
            );
            Usuario usuario = usuarioService.findByUsername(usuarioDTO.getUsername());
            String token = usuarioService.criarTokenAcessoApp(usuario);

            return ResponseEntity.ok(new ResponseMessage(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Erro ao autenticar: " + e.getMessage()));
        }
    }
}
