/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import entity.Card;
import entity.CardPK;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.jws.WebService;
import java.util.List;

/**
 *
 * @author jcvsa
 */
@WebService
@Stateless
@LocalBean
public class CardBean {

    @PersistenceContext
    private EntityManager entityManager;
    
    public Card addCard(String title, String description, Integer rating) {
        Card newCard = new Card(title, description, rating);
        entityManager.persist(newCard); // note already in transaction
        return newCard;
    }

    public Card getCard(Integer id) {
        CardPK primaryKey = new CardPK(id);
        Card card = entityManager.find(Card.class, primaryKey);
        
        return card;
    }

    public Card updateCard(String user, Integer id) {
        CardPK pk = new CardPK(id);
        Card cardEdit = (Card) entityManager.find(Card.class, pk);
        cardEdit.setUser(user);
        entityManager.merge(cardEdit);

        return cardEdit;
    }

    public List<Card> getUserCard(String user) {
        String jpqlCommand = "SELECT u FROM Card u WHERE u.user LIKE :user";
        Query query = entityManager.createQuery(jpqlCommand);
        query.setParameter("user", user);

        return query.getResultList();
    }

    public List<Card> getAllCard() {
        String jpqlCommand = "SELECT c FROM Card c";
        Query query = entityManager.createQuery(jpqlCommand);
        
        return query.getResultList();
    }

}
