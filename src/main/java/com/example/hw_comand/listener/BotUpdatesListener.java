package com.example.hw_comand.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.util.List;

@Component
public class BotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public BotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);

                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    SendMessage sendMessage = new SendMessage(chatId,
                            "Привет!\nДобро пожаловать в наш приют для животных!\nВыбери необходимую информацию:");
                    SendResponse sendResponse = telegramBot.execute(sendMessage);
                    if (!sendResponse.isOk()) {
                        logger.error("Error during sending message: {}", sendResponse.description());
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
