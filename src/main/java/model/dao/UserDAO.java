package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.User;

import java.util.ArrayList;

public interface UserDAO {

    User create(Long iduser,
                String password,
                String email,
                String firstname,
                String surname,
                String deleted,
                Integer isAdmin,
                String gender,
                String tel,
                String employment) throws DuplicatedObjectException;

    //crea utente all'interno del cookie, modifica e cancella l'utente -> creo la sessione CREATE, UPDATE, DELETE

    public User register(
                String password,
                String email,
                String firstname,
                String surname,
                String gender,
                String tel,
                String employment) throws DuplicatedObjectException;

    ArrayList<User> show();

    void revokeAdmin(User user);

    void deleteUser(User user);

    public User update( User user);

    public void delete(User user);

    public User findLoggedUser();

    public User findByUserId(Long iduser);

    public User findByEmail(String email);

    public User update(Long id, String password, String email, String surname, String firstname, String gender, String tel, String employment);

    public void makeAdmin(User user);


    /*

    public void findUsers(ArrayList<User> userList);

    void logUser(User u);

    void logout();

    */
}
