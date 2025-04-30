package br.com.atom.api_app_csc.model.entity;

import br.com.atom.api_app_csc.model.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_team")
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    //------------------------------------- Propriedades ------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team")
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "nickname")
    private String nickNameTeam;

    @Column(name = "nm_team")
    private String nameTeam;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamPlayer> teamPlayerList;

}


