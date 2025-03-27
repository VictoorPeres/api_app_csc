package br.com.atom.api_app_csc.repository;


import br.com.atom.api_app_csc.model.entity.TokenAcessoApp;
import br.com.atom.api_app_csc.model.entity.Usuario;
import br.com.atom.api_app_csc.model.enums.StatusTokenAcessoApp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository("tokenAcessoAppDao")
public class TokenAcessoAppRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public TokenAcessoApp save(TokenAcessoApp tokenAcessoApp){
        try{
            entityManager.persist(tokenAcessoApp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tokenAcessoApp;
    }

    @Transactional
    public TokenAcessoApp update(TokenAcessoApp tokenAcessoApp){
        try{
            entityManager.merge(tokenAcessoApp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tokenAcessoApp;
    }

    public TokenAcessoApp findByCodigo(String token) {
        try{
            String jpql = "SELECT t FROM TokenAcessoApp t WHERE t.token = :_token";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("_token", token);
            return (TokenAcessoApp) query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public List<TokenAcessoApp> getTokenAcessoAppAtivos(Usuario usuario){
        try{
            String hql = " SELECT ta from TokenAcessoApp as ta where ta.usuario.id = :_idUsuario AND ta.status = :_statusToken ";
            Query query = entityManager.createQuery(hql);
            query.setParameter("_idUsuario",(usuario.getId()));
            query.setParameter("_statusToken", StatusTokenAcessoApp.ATIVO);

            return (List<TokenAcessoApp>) query.getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public void desabilitarTokensAtivos(Usuario usuario){
        try{
            List<TokenAcessoApp> tokensAtivos = getTokenAcessoAppAtivos(usuario);
            if(!tokensAtivos.isEmpty()){
                for(TokenAcessoApp tokenAcessoApp : tokensAtivos){
                    tokenAcessoApp.setStatus(StatusTokenAcessoApp.INATIVO);
                    update(tokenAcessoApp);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
