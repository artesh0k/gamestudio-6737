package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

public interface UserService {

    void addUser(User user);

    boolean isLoginCorrect(String login, String password);

    boolean isTheSameUser(String login);

    void reset();
}
