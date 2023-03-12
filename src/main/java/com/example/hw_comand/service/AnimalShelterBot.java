package com.example.hw_comand.service;

import com.example.hw_comand.config.AnimalShelterBotConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.example.hw_comand.service.ButtonsConstants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnimalShelterBot extends TelegramLongPollingBot {
    private final ShelterInfoService shelterInfoService;
    private final AnimalShelterBotConfig config;


    /**
     * Метод помогает боту обрабатывать входящие команды/сообщения от пользователей
     * @param update входящая команда/запрос/сообщение
     * @return соответствующее запросу меню на выбор/список команды/сообщение
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                chooseMenu(chatId);
            } else {
                sendMessage(chatId, "Неверный формат сообщения");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case ("GENERAL_INFO"):
                    chooseGeneralInfo(chatId);
                    break;
                case ("ABOUT_SHELTER"):
                    sendMessage(chatId, shelterInfoService.getInfo());
                    break;
                case ("SCHEDULE_ADDRESS_ROUTE"):
                    sendMessage(chatId, shelterInfoService.getContactsInfo());
                    break;
                case ("SAFETY_RULES"):
                    sendMessage(chatId, shelterInfoService.getSafetyRules());
                    break;
                case ("USERS_DATA"):
                    sendMessage(chatId, shelterInfoService.callback());
                    break;
                case ("CALL_VOLUNTEER"):
                    sendMessage(chatId, shelterInfoService.callVolunteer());
                    break;
                case ("ADOPTION_INFO"):
                    sendMessage(chatId, shelterInfoService.adoptionInfo());
                    break;
                case ("SEND_REPORT"):
                    sendMessage(chatId, shelterInfoService.sendReport());
                    break;
                default:
                    System.out.println("Неверно выбран раздео");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void chooseMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(shelterInfoService.getGreeting());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(GENERAL_INFO);
        button1.setCallbackData("GENERAL_INFO");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(ADOPTION_INFO);
        button2.setCallbackData("ADOPTION_INFO");
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(SEND_REPORT);
        button3.setCallbackData("SEND_REPORT");
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(CALL_VOLUNTEER);
        button4.setCallbackData("CALL_VOLUNTEER");

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        rows.add(row1);
        rows.add(row2);

        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void chooseGeneralInfo(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(CHOICE);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(ABOUT_SHELTER);
        button1.setCallbackData("ABOUT_SHELTER");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(SCHEDULE_ADDRESS_ROUTE);
        button2.setCallbackData("SCHEDULE_ADDRESS_ROUTE");
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(SAFETY_RULES);
        button3.setCallbackData("SAFETY_RULES");
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(USERS_DATA);
        button4.setCallbackData("USERS_DATA");
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText(CALL_VOLUNTEER);
        button5.setCallbackData("CALL_VOLUNTEER");

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        row3.add(button5);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
