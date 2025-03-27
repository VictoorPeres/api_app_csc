package br.com.atom.api_app_csc.repository;

import br.com.atom.api_app_csc.model.entity.Team;
import br.com.atom.api_app_csc.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Team save(Team team){
        try{
            entityManager.persist(team);
        }catch (Exception e){
            e.printStackTrace();
        }
        return team;
    }

    @Transactional
    public Boolean findByNickNameTeam(String nickName){
        String jpql = "SELECT t FROM Team t WHERE t.nickNameTeam = :nickName";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("nickName", nickName);
        if(query.getResultList().isEmpty()){
            return false;
        }
        return true;
    }
}
