package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {

        var count = entityManager.createQuery("select r.rating from Rating r where r.game = :game and r.player = :player")
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultList();

        if(count.isEmpty()){

            entityManager.persist(rating);
            //System.out.println("new rating");

        } else {

            entityManager.createNativeQuery("update Rating r set rating = :rating, rated_on = :ratedOn where player = :player and r.game = :game")
                    .setParameter("rating", rating.getRating())
                    .setParameter("ratedOn", rating.getRatedOn())
                    .setParameter("player", rating.getPlayer())
                    .setParameter("game", rating.getGame())
                    .executeUpdate();
            //System.out.println("rating update");
        }
    }

    @Override
    public int getRating(String player, String game) throws NoResultException{
        try {
            return (Integer) entityManager.createQuery("select r.rating from Rating r where r.player = :player and r.game = :game")
                    .setParameter("player", player)
                    .setParameter("game", game)
                    .getSingleResult();
        } catch (NoResultException nre){
            return -1;
            //throw new GameStudioException("no result exception", nre);

        }
    }


    @Override
    public int getAverageRating(String game) throws NullPointerException{

        try {
             return ((Double) entityManager.createQuery("select avg(r.rating) from Rating r where r.game = :game")
                    .setParameter("game", game)
                    .getSingleResult()).intValue();
        } catch (NullPointerException nre){
            return -1;
            //throw new GameStudioException("no result exception", nre);
        }

    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}
