package ru.krey.games.dao;

import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.dao.service.Creator;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@NoArgsConstructor
@TestPropertySource("/application-test.properties")
public class MessageDaoTest {

    @Autowired
    private Creator creator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessageDao messageDao;

    private List<String> texts;

    private Integer messagesInitSize = 10;

    @Before
    public void fill() {
        jdbcTemplate.update("DELETE FROM message");
        jdbcTemplate.update("DELETE FROM player");
        texts = new ArrayList<>();
        for (int i = 0; i < messagesInitSize; i++) {
            String text = "text" + i;
            Message message = creator.createMessage(text,
                    creator.createPlayer("sender" + i, "sendermail" + i + "@mail.com"),
                    creator.createPlayer("recipient" + i, "recipientmail" + i + "@mail.com"));
            jdbcTemplate.update("INSERT INTO message (sender_id, recipient_id, sending_time, reading_time, message_text) " +
                            "VALUES (?,?,?,?,?)", message.getSender().getId(), message.getRecipient().getId(), message.getSendingTime(),
                    message.getReadingTime(), message.getMessageText());
            texts.add(text);
        }
    }


    @Test
    public void testGetAll() {
        List<Message> messages = messageDao.getAll();

        Assertions.assertEquals(messagesInitSize, messages.size());

        List<String> textsFromDB = messages.stream().map(Message::getMessageText).toList();

        boolean ok = true;
        for (String text : texts) {
            if (!textsFromDB.contains(text)) {
                ok = false;
                break;
            }
        }

        List<String> sendersNames = messages.stream().map(Message::getSender).map(Player::getLogin).toList();
        List<String> recipientNames = messages.stream().map(Message::getRecipient).map(Player::getLogin).toList();

        for (int i = 0; i < messagesInitSize; i++) {
            if (!sendersNames.contains("sender" + i)) {
                ok = false;
                break;
            }
            if (!recipientNames.contains("recipient" + i)) {
                ok = false;
                break;
            }
        }
        Assertions.assertTrue(ok);
    }

    @Test
    public void testSuccessSave() {
        Message message = creator.createMessage("Hello amigos", creator.createPlayer("save_sender1", "save_senderemail1@mail.com"), creator.createPlayer("save_recipient1", "save_recipienemail1@mail.com"));
        messageDao.saveOrUpdate(message);
        List<Message> messages = messageDao.getAll();
        boolean ok = false;
        for (Message m : messages) {
            if (m.getSender().getLogin().equals("save_sender1") && m.getRecipient().getLogin().equals("save_recipient1") && m.getMessageText().equals("Hello amigos")) {
                ok = true;
                break;
            }
        }
        Assertions.assertTrue(ok);
    }

    @Test
    public void testSuccessUpdate() {
        List<Message> messages = messageDao.getAll();
        Message message = messages.get(0);
        long id = message.getId();
        message.setMessageText("hi");
        message.setSender(creator.createPlayer("update_sender", "update_senderemail@mail.com"));
        message.setRecipient(creator.createPlayer("update_recipient", "update_recipientemail@mail.com"));
        message.setReadingTime(LocalDateTime.of(2023, 3, 3, 3, 3));
        messageDao.saveOrUpdate(message);

        messages = messageDao.getAll();
        Message messageFromDb = messages.stream().filter(message1 -> message1.getId().equals(id)).findAny().get();
        Assertions.assertEquals(messageFromDb, message);
    }

    @Test
    public void testSuccessGetAllByPlayer() {
        Player mainPlayer = creator.createPlayer("main", "main@mail.com");
        Player player1 = creator.createPlayer("player1", "player1@mail.com");
        Player player2 = creator.createPlayer("player2", "player2@mail.com");

        int size = 10;
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, mainPlayer, player1));
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, player2, mainPlayer));
            } else {
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, player1, mainPlayer));
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, mainPlayer, player2));
            }
        }
        List<Message> messages = messageDao.getAllMessagesByPlayerId(mainPlayer.getId());
        Assertions.assertEquals(size * 2, messages.size());

        long countSender = messages.stream().filter(message -> message.getSender().getId().equals(mainPlayer.getId())).count();
        long countRecipient = messages.stream().filter(message -> message.getRecipient().getId().equals(mainPlayer.getId())).count();

        Assertions.assertEquals(size,countSender);
        Assertions.assertEquals(size,countRecipient);
    }

    @Test
    public void testSuccessGetLastByPlayer() {
        Player mainPlayer = creator.createPlayer("main_1", "main_1@mail.com");
        Player player1 = creator.createPlayer("player1_1", "player1_1@mail.com");
        Player player2 = creator.createPlayer("player2_1", "player2_1@mail.com");

        int size = 10;
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, mainPlayer, player1));
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, player2, mainPlayer));
            } else {
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, player1, mainPlayer));
                messageDao.saveOrUpdate(creator.createMessage("Message" + i, mainPlayer, player2));
            }
        }
        List<Message> messages = messageDao.getLastMessagesByPlayerId(mainPlayer.getId());

        Assertions.assertEquals(2, messages.size());

        Message messageWithMainSender = messages.stream().filter(message -> message.getSender().getId().equals(mainPlayer.getId())).findAny().get();
        Message messageWithMainRecipient = messages.stream().filter(message -> message.getRecipient().getId().equals(mainPlayer.getId())).findAny().get();

        Assertions.assertEquals("Message"+(size-1),messageWithMainSender.getMessageText());
        Assertions.assertEquals("Message"+(size-1),messageWithMainRecipient.getMessageText());
    }
}
