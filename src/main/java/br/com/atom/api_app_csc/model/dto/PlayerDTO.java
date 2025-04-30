package br.com.atom.api_app_csc.model.dto;

import lombok.Data;

@Data
public class PlayerDTO {

    private String playerId;
    private String nickName;
    private String steamId64;
    private String steamNickName;
    private String country;
    private String avatarUrl;
    private Object games;
    private String dt_cadastro;

}
