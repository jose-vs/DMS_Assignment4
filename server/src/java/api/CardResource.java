/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import entity.Card;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcvsa
 */
@Named
@Path("/cards")
public class CardResource {

    @EJB
    private CardBean cardBean;

    private Connection sendingConn, receivingConn;
    private Session sendSession, receivingSession;

    private Logger logger;

    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/MessageQueue")
    private Queue queue;

    public CardResource() {
        logger = Logger.getLogger(getClass().getName());
    }

    @PostConstruct
    public void setupJMSSessions() {
        if (connectionFactory == null) {
            logger.warning("Dependency injection of jms/ConnectionFactory failed");
        } else {
            try {
                // obtain a connection to the JMS provider
                sendingConn = connectionFactory.createConnection();
                // obtain an untransacted context for producing messages
                sendSession = sendingConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // obtain a connection to the JMS provider
                receivingConn = connectionFactory.createConnection();
                // obtain an untransacted context for producing messages
                receivingSession = receivingConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                logger.info("Dependency injection of jms/ConnectionFactory was successful");
            } catch (JMSException e) {
                logger.log(Level.WARNING, "Error while creating sessions: {0}", e);
            }
        }
        if (queue == null) {
            logger.warning("Dependency injection of jms/MessageQueue failed");
        } else {
            logger.info("Dependency injection of jms/MessageQueue was successful");
        }
    }

    @PreDestroy
    public void closeJMSSessions() {
        try {
            if (receivingSession != null) {
                receivingSession.close();
            }
            if (receivingConn != null) {
                receivingConn.close();
            }
            if (sendSession != null) {
                sendSession.close();
            }
            if (sendingConn != null) {
                sendingConn.close();
            }
        } catch (JMSException e) {
            logger.log(Level.WARNING, "Unable to close connection: {0}", e);
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
     * done while being sent to another user and stored in the queue
     *
     * @param cardPK
     * @param user
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{id}/{user}")
    public void resetCard(@PathParam("id") Integer cardPK, @PathParam("user") String user) {
        Card card = cardBean.resetCard(user, cardPK);
        sendMessage("[Card Sent]/" + cardPK + "/" + card.getName() + "/" + user);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{id}")
    public void updateCard(MultivaluedMap<String, String> formParams, @PathParam("id") Integer cardPK) {
        String user = formParams.getFirst("user");
        cardBean.updateCard(user, cardPK);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{user}")
    public void retrieveCard(@PathParam("user") String user) {
        List<Integer> cardPKList = retrieveMessage();
        
        for (Integer pk : cardPKList) { 
            cardBean.updateCard(user, pk);
        }
    }

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
            MessageProducer producer = sendSession.createProducer(queue);
            TextMessage message = sendSession.createTextMessage();
            message.setText(msg);
            producer.send(message);
        } catch (JMSException e) {
            System.err.println("message failed to send: " + e);
        }
    }

    private List<Integer> retrieveMessage() {
        
        List<Integer> cardPKList = new ArrayList<>(); 
        try {

            MessageConsumer consumer = receivingSession.createConsumer(queue);
            receivingConn.start();
            Message nextMessage = null;
            do {
                nextMessage = consumer.receive(1000); // 1000ms timeout
                if (nextMessage != null && nextMessage instanceof TextMessage) {
                    String textContent = ((TextMessage) nextMessage).getText();
                    logger.log(Level.INFO, "Received text message from queue: {0}", textContent);
                    
                    //parse message
                    String[] msg = textContent.split("/");
                    cardPKList.add(Integer.parseInt(msg[1]));
                }

            } while (nextMessage != null);

        } catch (JMSException e) {
            logger.log(Level.WARNING, "Error while receiving messages: {0}", e);
        }

        return cardPKList;
        
    }
}
