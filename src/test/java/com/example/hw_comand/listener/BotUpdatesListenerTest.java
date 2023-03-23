package com.example.hw_comand.listener;

import com.example.hw_comand.menu_buttons.BotReplyMessage;
import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.example.hw_comand.repository.ReportDataRepository;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Класс, тестирующий функционал класса BotUpdatesListener (доработать)
 */
@ExtendWith(MockitoExtension.class)
public class BotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private BotReplyMessage botReplyMessage;

    @Mock
    private ButtonMenu buttonMenu;
    @Mock
    ReportDataRepository reportDataRepository;

    @InjectMocks
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
        assertThat(actual.getParameters().get("text")).isEqualTo(botReplyMessage.getGreeting());
        verify(buttonMenu).choosePetMenu(update.message().chat().id());
    }

    /**
     * Метод тестирует реакцию бота на нажатие пользователем кнопки меню
     */
    @Test
    public void someCommandReceived() throws URISyntaxException, IOException {//todo: доработать тесты
        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%data%", "ABOUT_SHELTER"), Update.class);
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

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.callbackQuery().id());
        assertThat(actual.getParameters().get("text")).isEqualTo(botReplyMessage.getShelterInfo());
    }

    @Test
    public void invokeTimer() {
        // тело метода
    }

}
