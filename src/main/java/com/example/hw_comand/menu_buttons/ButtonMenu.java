package com.example.hw_comand.menu_buttons;

import com.example.hw_comand.listener.BotUpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Класс для создания кнопок, в виде которых будут отображаться запросы пользователя в телеграм-боте.
 */
@RequiredArgsConstructor
@Service
public class ButtonMenu {
    private final TelegramBot telegramBot;

    private final BotReplyMessage botReplyMessage;
    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);


    /**
     * Метод, вызывающий начальное меню для выбора питомца.
     * @param chatId
     * @see ButtonMenu
     */
    public void choosePetMenu (Long chatId) {
        logger.info("The pet choice menu has been chosen: {}, {}", chatId, "Вызвано меню выбора питомца");


        InlineKeyboardButton catButton = new InlineKeyboardButton(ButtonConstant.CAT.getCommand());
        InlineKeyboardButton dogButton = new InlineKeyboardButton(ButtonConstant.DOG.getCommand());
        catButton.callbackData(ButtonConstant.CAT.getName());
        dogButton.callbackData(ButtonConstant.DOG.getName());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(catButton, dogButton);

        sendMessage(chatId, botReplyMessage.getPetChoice(), keyboardMarkup);
    }

    /**
     * Метод для вызова основного меню с кнопками.
     * @param chatId
     * @see ButtonMenu
     */
    public void chooseMainMenu(Long chatId) {
        logger.info("The main menu has been chosen: {},  {}", chatId, "Вызвано основное меню");

        InlineKeyboardButton button1 = new InlineKeyboardButton(ButtonConstant.GENERAL_INFO.getCommand());
        InlineKeyboardButton button2 = new InlineKeyboardButton(ButtonConstant.ADOPTION_INFO.getCommand());
        InlineKeyboardButton button3 = new InlineKeyboardButton(ButtonConstant.SEND_REPORT.getCommand());
        InlineKeyboardButton button4 = new InlineKeyboardButton(ButtonConstant.CALL_VOLUNTEER.getCommand());

        button1.callbackData(ButtonConstant.GENERAL_INFO.getName());
        button2.callbackData(ButtonConstant.ADOPTION_INFO.getName());
        button3.callbackData(ButtonConstant.SEND_REPORT.getName());
        button4.callbackData(ButtonConstant.CALL_VOLUNTEER.getName());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1, button2);
        keyboardMarkup.addRow(button3, button4);

        sendMessage(chatId, botReplyMessage.getChoice(), keyboardMarkup);
    }

    /**
     * Метод вызывает меню со всеми сведениями о приюте.
     * @param chatId
     * @see ButtonMenu
     */
    public void chooseGeneralInfo(Long chatId) {
        logger.info("The general information menu has been chosen: {},  {}", chatId, "Вызвано меню с информацией о приюте");

        InlineKeyboardButton button1 = new InlineKeyboardButton(ButtonConstant.ABOUT_SHELTER.getCommand());
        InlineKeyboardButton button2 = new InlineKeyboardButton(ButtonConstant.SCHEDULE_ADDRESS_ROUTE.getCommand());
        InlineKeyboardButton button3 = new InlineKeyboardButton(ButtonConstant.SAFETY_RULES.getCommand());
        InlineKeyboardButton button4 = new InlineKeyboardButton(ButtonConstant.CALLBACK.getCommand());
        InlineKeyboardButton button5 = new InlineKeyboardButton(ButtonConstant.CALL_VOLUNTEER.getCommand());

        button1.callbackData(ButtonConstant.ABOUT_SHELTER.getName());
        button2.callbackData(ButtonConstant.SCHEDULE_ADDRESS_ROUTE.getName());
        button3.callbackData(ButtonConstant.SAFETY_RULES.getName());
        button4.callbackData(ButtonConstant.CALLBACK.getName());
        button5.callbackData(ButtonConstant.CALL_VOLUNTEER.getName());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1, button2);
        keyboardMarkup.addRow(button3, button4);
        keyboardMarkup.addRow(button5);

        sendMessage(chatId, botReplyMessage.getChoice(), keyboardMarkup);
    }

    /**
     * Метод-шаблон для создания сообщения с кнопками от бота.
     * @param chatId
     * @param textToSend
     * @param keyboardMarkup
     * @see ButtonMenu
     */
    private void sendMessage(Long chatId, String textToSend, InlineKeyboardMarkup keyboardMarkup){
        SendMessage message = new SendMessage(chatId, textToSend);
        message.replyMarkup(keyboardMarkup);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Error during sending message: {}", response.description());
        }
    }


}
