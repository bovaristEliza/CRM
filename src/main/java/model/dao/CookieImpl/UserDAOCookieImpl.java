package model.dao.CookieImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.UserDAO;
import model.mo.User;

import java.util.ArrayList;


public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    //crea utente all'interno del cookie, modifica e cancella l'utente -> creo la sessione CREATE, UPDATE, DELETE
    @Override
    public User create(Long iduser,
                       String password,
                       String email,
                       String firstname,
                       String surname,
                       String deleted,
                       Integer isAdmin,
                       String gender,
                       String tel,
                       String employment) {

        //creo un nuovo oggetto user chiamato logged user

        User loggedUser = new User();
        loggedUser.setIduser(iduser);
        loggedUser.setFirstname(firstname);
        loggedUser.setSurname(surname);
        loggedUser.setIsAdmin(isAdmin);//MEMORIZZO SOLO QUESTI PARAMETRI


        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser)); // faccio un encoding
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public User update(User loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);
        return loggedUser;
    }

    @Override
    public void delete(User loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie); //ripulisco dal cookie l'utente
    }

    @Override //metodo di lettura
    public User findLoggedUser() {
        Cookie[] cookies = request.getCookies(); //prende i cookies e li salva in un array
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    @Override
    public User findByUserId(Long iduser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public User findByEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User update(Long id, String password, String email, String surname, String firstname, String gender, String tel, String employment) {
        return null;
    }

    @Override
    public void makeAdmin(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User register(String password,
                         String email,
                         String firstname,
                         String surname,
                         String gender,
                         String tel,
                         String employment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<User> show() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void revokeAdmin(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //metodi di encode e decode/////////////////////////////////////////////////////////////////////////////////////
    private String encode(User loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getIduser() + "#" + loggedUser.getFirstname() + "#" + loggedUser.getSurname() + '#' + loggedUser.getIsAdmin();
        return encodedLoggedUser;

    }

    private User decode(String encodedLoggedUser) {

        User loggedUser = new User();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setIduser(Long.parseLong(values[0]));
        loggedUser.setFirstname(values[1]);
        loggedUser.setSurname(values[2]);
        loggedUser.setIsAdmin(Integer.parseInt(values[3]));
        //username

        return loggedUser;

    }
        /*

         @Override
    public void findUsers(ArrayList<User> userList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void logUser(User u) {

    }

    @Override
    public void logout() {

    }*/
}
