package com.example.hw_comand.keyboard;

import com.example.hw_comand.listener.TelegramBotUpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A <b>custom</b> class that implements <b>buttons</b> in the telegram bot interface.
 * @author Tatiana Alekseev
 * @version 1.0.0
 */
@Service
public class KeyBoardShelter {

    @Autowired
    private TelegramBot telegramBot;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    /**
     * A method for displaying a menu with a basic set of buttons.
     *
     * @param chatId
     * @see KeyBoardShelter
     */
    public void chooseMenu(long chatId) {
        logger.info("Method sendMessage has been run: {}, {}", chatId, "Вызвано меню выбора ");

        String emoji_cat = EmojiParser.parseToUnicode(":cat:");
        String emoji_dog = EmojiParser.parseToUnicode(":dog:");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(emoji_cat + " CAT"));
        replyKeyboardMarkup.addRow(new KeyboardButton(emoji_dog + " DOG"));
  //      returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите, кого хотите приютить:");
    }

    /**
     * A method for displaying a menu with a basic set of buttons.
     *
     * @param chatId
     * @see KeyBoardShelter
     */
    public void sendMenu(long chatId) {
        logger.info("Method sendMessage has been run: {}, {}", chatId, "Вызвано основное меню ");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Информация о возможностях бота"),
                new KeyboardButton("Узнать информацию о приюте"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Как взять питомца из приюта"),
                new KeyboardButton("Прислать отчет о питомце"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"));

  //      returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Главное меню");
    }

    /**
     * The way to display the menu with information about the shelter.
     *
     * @param chatId
     * @see KeyBoardShelter
     */
    public void sendMenuInfoShelter(long chatId) {
        logger.info("Method sendMenuInfoShelter has been run: {}, {}", chatId, "Вызвали ~Информация о приюте~");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton("Информация о приюте"),
                new KeyboardButton("Оставить контактные данные").requestContact(true));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"),
                new KeyboardButton("Вернуться в меню"));
  //      returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о приюте");
    }

    //sendMenuTakeAnimal and returnResponseReplyKeyboardMarkup, не хватает, закомментировала
}