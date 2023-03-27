package com.example.hw_comand.listener;


import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.example.hw_comand.repository.ReportDataRepository;
import com.example.hw_comand.service.ReportDataService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

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


    @Autowired
    private ButtonMenu buttonMenu;

    @MockBean
    private ReportDataService reportDataService;

    @MockBean
    private ReportDataRepository reportDataRepository;

    Message mockMessage = mock(Message.class);
    CallbackQuery mockCallbackQuery = mock(CallbackQuery.class);


    @Autowired
    private BotUpdatesListener botUpdatesListener;


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
        assertThat(actual.getParameters().get("chat_id")).isEqualTo((update.callbackQuery().message().chat().id()));
        assertThat(actual.getParameters().get("text")).isEqualTo("Наш приют поможет Вам выбрать себе питомца, но прежде" +
                " мы познакомим Вас и научим ухаживать за ним!\n" +
                "Вам предстоит пройти испытательный срок, прежде чем окончательно забрать питомца в новый дом.");

    }

    /**
     * Тест на соообщение (Некорректный формат)
     * @throws URISyntaxException
     * @throws IOException
     */

    @Test
    public void IncorrectMessageFormat() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(BotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = getUpdate(json, "hello world");
        botUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Некорректный формат сообщения");
    }

    @Test
    public void handleInvalidSwitchCommand() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("button.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%data%", "INVALID_COMMAND"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(mockCallbackQuery.data()).thenReturn("INVALID_COMMAND");

        botUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo((update.callbackQuery().message().chat().id()));
        assertThat(actual.getParameters().get("text")).isEqualTo("Неверно выбран раздел");
    }

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
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
    }

    @Test
    public void testSavingReport() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(BotUpdatesListenerTest.class.getResource("photo.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", """
            Рацион: разнообразный
            Самочувствие: в порядке
            Изменения в поведении: нет"""), Update.class);
        GetFileResponse getFileResponse = BotUtils.fromJson("""
                {
                "ok": true,
                "result": {
                    "file_path": "tmp"
                  }
                }
                """, GetFileResponse.class);
        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """, SendResponse.class);
        byte[] photo = new byte[0];
        when(telegramBot.execute(any(GetFile.class))).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(photo);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);

        botUpdatesListener.process(Collections.singletonList(update));
        verify(reportDataService).uploadReportData(any(), eq(photo), any(), any(), any());

        verify(telegramBot, times(2)).execute(any());

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, atLeastOnce()).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        assertThat(actual.getParameters().get("text")).isEqualTo("Ваш отчёт принят");
    }



    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

}
