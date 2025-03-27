package br.com.atom.api_app_csc.model.entity;

import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "tb_token_acesso_app")
public class TokenAcessoApp {

    /************************************************************    ATRIBUTOS    ************************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token_acesso_app", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "cd_token", length = 200)
    private String token;

    @Column(name = "dt_criacao")
    private Date dataCriacao;

    @Column(name = "dt_expiracao")
    private Date dataExpiracao;

    @Enumerated
    @Column(name = "cd_status")
    private StatusTokenAcessoApp status;


}