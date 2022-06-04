/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import entity.User;
import entity.UserPK;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

/**
 *
 * @author jcvsa
 */
@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext
    private EntityManager entityManager;

    public User addUser(String uname, String name, String email, String password) {
        User newUser = new User(uname, name, email, password);
        entityManager.persist(newUser);
        return newUser;
    }

    public List<User> getUser(String username) {
        String jpqlCommand = "SELECT u FROM User u WHERE u.username = :username";
        Query query = entityManager.createQuery(jpqlCommand);
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    public User updateUser(String uname, String name, String email, String password) {
        UserPK primaryKey = new UserPK(uname);
        User userEdit = (User) entityManager.find(User.class, primaryKey);       
        userEdit.setName(name);
        userEdit.setEmail(email);
        userEdit.setPassword(password);
        entityManager.merge(userEdit);

        return userEdit;
    }

    public List<User> getAllUsers() {
        String jpqlCommand = "SELECT u FROM User u";
        Query query = entityManager.createQuery(jpqlCommand);
        return query.getResultList();
    }

}
