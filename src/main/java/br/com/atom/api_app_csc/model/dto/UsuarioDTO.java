package br.com.atom.api_app_csc.model.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String playerId;
    private String cpf;
    private String email;
    private String nome;
    private String username;
}