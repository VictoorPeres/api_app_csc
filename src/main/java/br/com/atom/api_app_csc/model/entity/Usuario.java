package br.com.atom.api_app_csc.model.entity;

import br.com.atom.api_app_csc.model.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findByUsername",
                query = "SELECT usu FROM Usuario usu WHERE usu.username = :username AND usu.statusUsuario = 1")
})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    //------------------------------------- Propriedades ------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "cd_cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "nm_usuario")
    private String nome;

    @Column(name = "nm_login")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "id_status_usuario")
    private StatusUsuario statusUsuario;

    @Transient
    private String senha;

    @Transient
    private String confirmaSenha;

}


