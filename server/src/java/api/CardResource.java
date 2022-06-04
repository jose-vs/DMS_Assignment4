/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import entity.Card;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.ws.rs.Path;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Collection;

/**
 *
 * @author jcvsa
 */
@Named
@Path("/cards")
public class CardResource {

    @EJB
    private CardBean cardBean;

    private Connection conn;
    private Session session;
    private MessageProducer producer;

    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/MessageQueue")
    private Queue queue;

    public CardResource() {
    }

    @PostConstruct
    public void prepareToSendMessage() {
        try {
            // obtain a connection to the JMS provider
            conn = connectionFactory.createConnection();
            // obtain an untransacted context for producing messages
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // obtain a producer of messages to send to the queue
            producer = session.createProducer(queue);
        } catch (JMSException e) {
            System.err.println("Unable to open connection: " + e);
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCardsJSON() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Collection<Card> allCards = cardBean.getAllCard();
        for (Card card : allCards) {
            arrayBuilder.add(card.getJSONObject());
        }
        JsonObject json = jsonBuilder.add("cards", arrayBuilder).build();

        return json.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{user}")
    public String getUserCards(@PathParam("user") String user) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Collection<Card> cards = cardBean.getUserCard(user);
        for (Card card : cards) {
            arrayBuilder.add(card.getJSONObject());
        }
        JsonObject json = jsonBuilder.add("cards", arrayBuilder).build();

        return json.toString();
    }
    
    
    /**
     * used to add cards to the database
     * 
     * @param formParams 
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewCard(MultivaluedMap<String, String> formParams) {
        String name = formParams.getFirst("name");
        String description = formParams.getFirst("description");
        Integer rating = Integer.parseInt(formParams.getFirst("rating"));
        cardBean.addCard(name, description, rating);
    }

    private void sendMessage(String msg) {
        try {
            TextMessage message = session.createTextMessage();
            message.setText(msg);
            producer.send(message);
        } catch (JMSException e) {
            System.err.println("message failed to send: " + e);
        }

    }
}
