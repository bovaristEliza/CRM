package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Notes;
import model.mo.User;
import model.dao.UserDAO;
import services.logservice.LogService;

public class UserDAOMySQLJDBCImpl implements UserDAO {


    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }



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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.


    }

    @Override
    public User register(
                         String password,
                         String email,
                         String firstname,
                         String surname,
                         String gender,
                         String tel,
                         String employment)

            throws DuplicatedObjectException {

        PreparedStatement ps;
        User user = new User();
        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setPassword(password);
        user.setEmail(email);
        user.setGender(gender);
        user.setTel(tel);
        user.setEmployment(employment);


        try {

            String sql
                    = " SELECT email "
                    + " FROM user "
                    + " WHERE "
                    + " deleted ='N' AND "
                    + " email = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, user.getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento di un utente già esistente.");
            }

            sql   = " INSERT INTO user ("
                    + "     password,"
                    + "     email,"
                    + "     firstname,"
                    + "     surname,"
                    + "     gender,"
                    + "     tel,"
                    + "     employment)"
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setString(i++, user.getPassword());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getFirstname());
            ps.setString(i++, user.getSurname());
            ps.setString(i++, user.getGender());
            ps.setString(i++, user.getTel());
            ps.setString(i++, user.getEmployment());

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        user.setIduser(rs.getLong(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;

    }

    @Override
    public User update(Long id, String password, String email, String surname, String firstname, String gender, String tel, String employment) {

        User tempUser;

        User user = new User();
        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setPassword(password);
        user.setEmail(email);
        user.setGender(gender);
        user.setTel(tel);
        user.setEmployment(employment);


        Logger logger = LogService.getApplicationLogger();

        try {
            String sql = " UPDATE user SET password=?, email = ?, firstname= ?, surname= ?,gender= ?, tel= ?,employment= ? WHERE iduser=?";

            // Preparo la query sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Completo la query con i relativi valori
            int i=1;
            ps.setString(i++, user.getPassword());
            ps.setString(i++,user.getEmail());
            ps.setString(i++, user.getFirstname());
            ps.setString(i++, user.getSurname());
            ps.setString(i++, user.getGender());
            ps.setString(i++, user.getTel());
            ps.setString(i++, user.getEmployment());

            ps.setString(i++, String.valueOf(id));

            // Eseguo la query
            // ResultSet resultSet = ps.executeQuery();
            int update = ps.executeUpdate();

            if (update > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        user.setIduser(rs.getLong(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Persona create error ", e);
            throw new RuntimeException(e);
        }

        // Ritorno oggetto user per compilare la scheda appena creata
        tempUser= findByEmail(user.getEmail());
        return tempUser;

    }


    @Override
    public User findLoggedUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User findByUserId(Long iduser) {

        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM user "
                    + " WHERE "
                    + "   iduser = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, iduser);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;

    }



    @Override
    public User findByEmail(String email) {

        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM user "
                    + " WHERE "
                    + "   email = ? AND deleted='N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;

    }

    @Override
    public ArrayList<User> show() {

        PreparedStatement ps;
        User user;
        ArrayList<User> users = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM user ";


            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                user = read(resultSet);
                users.add(user);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }



    static User read(ResultSet rs) {

        User user = new User();
        try
        {
            user.setIduser(rs.getLong("iduser"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setFirstname(rs.getString("firstname"));
            user.setSurname(rs.getString("surname"));
            user.setDeleted(String.valueOf(rs.getString("deleted")));
            user.setIsAdmin(rs.getInt("isAdmin"));
            user.setGender(rs.getString("gender"));
            user.setTel(rs.getString("tel"));
            user.setEmployment(rs.getString("employment"));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }

        return user;
    }

    @Override
    public void makeAdmin(User user) {
        Logger logger = LogService.getApplicationLogger();
        logger.log(Level.ALL,user.toString());

        try {

            String sql = "UPDATE user SET isAdmin = 1 WHERE email = ?";

            // Preparo la query sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Completo la query con i relativi valori
            int i=1;
            ps.setString(i++, user.getEmail());

            int update = ps.executeUpdate();
            //resultSet.close();
            ps.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Persona create error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void revokeAdmin(User user) {
        Logger logger = LogService.getApplicationLogger();
        logger.log(Level.ALL,user.toString());

        try {

            String sql = "UPDATE user SET isAdmin = 0 WHERE email = ?";

            // Preparo la query sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Completo la query con i relativi valori
            int i=1;
            ps.setString(i++, user.getEmail());

            int update = ps.executeUpdate();
            //resultSet.close();
            ps.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Persona create error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(User user) {
        Logger logger = LogService.getApplicationLogger();
        logger.log(Level.ALL,user.toString());

        try {

            String sql = "UPDATE user SET deleted = 'Y' WHERE email = ?";

            // Preparo la query sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Completo la query con i relativi valori
            int i=1;
            ps.setString(i++, user.getEmail());

            int update = ps.executeUpdate();
            //resultSet.close();
            ps.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Persona create error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        Logger logger = LogService.getApplicationLogger();
        logger.log(Level.ALL,user.toString());

        try {

            String sql = "UPDATE user SET deleted = 'ToDelete' WHERE email = ?";

            // Preparo la query sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Completo la query con i relativi valori
            int i=1;
            ps.setString(i++, user.getEmail());

            int update = ps.executeUpdate();
            //resultSet.close();
            ps.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Persona create error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*

    @Override
    public void findUsers(ArrayList<User> userList) {

        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM user ";

            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                user = read(resultSet);
                userList.add(user);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void logUser(User u) {

    }

    @Override
    public void logout() {

    }*/


}
