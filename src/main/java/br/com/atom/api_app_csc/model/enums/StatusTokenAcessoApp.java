package br.com.atom.api_app_csc.model.enums;

public enum StatusTokenAcessoApp {

    INATIVO(0,"INATIVO"), ATIVO(1,"ATIVO");

    private int codigo;
    private String status;

    StatusTokenAcessoApp(int codigo, String status){
        this.codigo = codigo;
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
