package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(name="users")
@NamedQuery( name = "User.resetUsers",
        query = "DELETE FROM User")
public class User {

    @Id
    @GeneratedValue
    private int ident;

    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
