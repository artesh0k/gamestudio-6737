package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        if(!isTheSameUser(user.getLogin())){
            entityManager.persist(user);
        }
    }

    @Override
    public boolean isLoginCorrect(String login, String password) {
        return (boolean) entityManager.createQuery("select case when (count(u) > 0)  then true else false end from User u where u.login = :login and u.password = :password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();

    }

    @Override
    public boolean isTheSameUser(String login) {
        return (boolean) entityManager.createQuery("select case when (count(u) > 0)  then true else false end from User u where u.login = :login")
                .setParameter("login", login)
                .getSingleResult();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
