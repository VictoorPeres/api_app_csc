package br.com.atom.api_app_csc.repository;

import br.com.atom.api_app_csc.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Usuario save(Usuario usuario){
        try{
            entityManager.persist(usuario);
        }catch (Exception e){
            e.printStackTrace();
        }
        return usuario;
    }

    @Transactional
    public Boolean findByEmail(String email){
        String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("email", email);
        if(query.getResultList().isEmpty()){
            return false;
        }
        return true;
    }

    @Transactional
    public Usuario findByUsername(String username){
        String jpql = "SELECT u FROM Usuario u WHERE u.username = :username";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("username", username);
        return (Usuario) query.getSingleResult();
    }

    @Transactional
    public Usuario findByPLayerId(String playerId){
        String jpql = "SELECT u FROM Usuario u WHERE u.playerId = :playerId";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("playerId", playerId);

        if(!query.getResultList().isEmpty()){
            return (Usuario) query.getSingleResult();
        }
        return null;
    }
}
