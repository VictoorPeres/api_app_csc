package br.com.atom.api_app_csc.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {

    private String teamId;
    private String nickName;
    private String name;
    private String team_type;
    private String game;
    private String avatarUrl;
    private List<?> members;

}
