package com.example.hw_comand.listener;

import com.example.hw_comand.menu_buttons.BotReplyMessage;
import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.example.hw_comand.model.ReportData;
import com.example.hw_comand.repository.ReportDataRepository;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Класс, тестирующий функционал класса BotUpdatesListener (доработать)
 */
@SpringBootTest
public class BotUpdatesListenerTest {

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private ButtonMenu buttonMenu;
    @MockBean
    ReportDataRepository reportDataRepository;

    private BotReplyMessage botReplyMessage;

    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);

    Message mockMessage = mock(Message.class);
    CallbackQuery mockCallbackQuery = mock(CallbackQuery.class);


    @Autowired
    private BotUpdatesListener botUpdatesListener;

    /**
     * Метод тестирует реакцию бота на команду "/start"
     */

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {//todo: доработать тесты
        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        botUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        assertThat(actual.getParameters().get("text")).isEqualTo("Привет!\nДобро пожаловать в наш приют для животных!\nВыберите питомца:");
        verify(buttonMenu).choosePetMenu(update.message().chat().id());
    }



    @Test
    public void invokeTimer() {
        // тело метода
    }

    /**
     * Метод тестирует реакцию бота на нажатие пользователем кнопки меню
     */

    @Test
    public void testInlineKeyboardListener() throws URISyntaxException, IOException {

        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("button.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%data%", "ABOUT_SHELTER"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(mockCallbackQuery.data()).thenReturn("ABOUT_SHELTER");

        botUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        // задание текста сообщения

        assertThat(actual.getParameters().get("chat_id")).isEqualTo((update.callbackQuery().message().chat().id()));
        assertThat(actual.getParameters().get("text")).isEqualTo("Наш приют поможет Вам выбрать себе питомца, но прежде" +
                " мы познакомим Вас и научим ухаживать за ним!\n" +
                "Вам предстоит пройти испытательный срок, прежде чем окончательно забрать питомца в новый дом.");


    }

    /**
     * Метод также тестирует реакцию бота на нажатие пользователем кнопки меню
щ     */

    @Test
    public void testInlineKeyboardListenerOnlyOne() throws URISyntaxException, IOException {//todo: доработать тесты
        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("button.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%data%", "SCHEDULE_ADDRESS_ROUTE"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(mockCallbackQuery.data()).thenReturn("SCHEDULE_ADDRESS_ROUTE");

        botUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.callbackQuery().message().chat().id());
        assertThat(actual.getParameters().get("text")).isEqualTo("Часы работы: Пн-Вс с 9:00 до 18:00\nАдрес: г. Астана, ул. Саргуль, 12");
    }



}


