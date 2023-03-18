package com.example.hw_comand.listener;

import com.example.hw_comand.menu_buttons.BotReplyMessage;
import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.example.hw_comand.model.PersonCat;
import com.example.hw_comand.model.PersonDog;
import com.example.hw_comand.model.ReportData;
import com.example.hw_comand.repository.PersonCatRepository;
import com.example.hw_comand.repository.PersonDogRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс BotUpdatesListener реализует интерфейс UpdatesListener.
 */
@Component
@RequiredArgsConstructor
public class BotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);
    private final TelegramBot telegramBot;

    private final BotReplyMessage botReplyMessage;
    private final ButtonMenu buttonMenu;
    private final ReportDataService reportDataService;
    private final ReportDataRepository reportDataRepository;
    private final PersonDogRepository personDogRepository;
    private final PersonCatRepository personCatRepository;

    private boolean isPetDog = false;
    private static final Pattern REPORT_PATTERN = Pattern.compile(("(Рацион:)(\\s+([А-я\\d\\s.,!?:;]+))\n" +
            "(Самочувствие:)(\\s+([А-я\\d\\s.,!?:;]+))\n" +
            "(Изменения в поведении:)(\\s+([А-я\\d\\s.,!?:;]+))"));

    private static final Pattern PHONE_PATTERN = Pattern.compile("[+7].\\d{10}");


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод помогает боту обрабатывать входящие команды/сообщения от пользователей
     * @param updates входящие команды/запросы/сообщения
     * @see BotUpdatesListener
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);

                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if (update.message() != null) {
                    if ("/start".equals(text)) {
                        sendMessage(chatId, botReplyMessage.getGreeting());
                        buttonMenu.choosePetMenu(chatId);
                    } else {
                        sendMessage(chatId, "Неверный формат сообщения");
                    }
                } else if (update.callbackQuery() != null) {
                    Long chatId1 = update.callbackQuery().message().chat().id();
                    CallbackQuery callbackQuery = update.callbackQuery();
                    String data = callbackQuery.data();

                    switch (data) {
                        case ("CAT"):
                            isPetDog = false;
                            buttonMenu.chooseMainMenu(chatId1);
                            break;
                        case ("DOG"):
                            isPetDog = true;
                            buttonMenu.chooseMainMenu(chatId1);
                            break;
                        case ("GENERAL_INFO"):
                            buttonMenu.chooseGeneralInfo(chatId1);
                            break;
                        case ("ABOUT_SHELTER"):
                            sendMessage(chatId1, botReplyMessage.getShelterInfo());
                            break;
                        case ("SCHEDULE_ADDRESS_ROUTE"):
                            sendMessage(chatId1, botReplyMessage.getContactsInfo());
                            break;
                        case ("SAFETY_RULES"):
                            sendMessage(chatId1, botReplyMessage.getSafetyRules());
                            break;
                        case ("CALLBACK"):
                            sendMessage(chatId1, botReplyMessage.callback());
                            sendContact(update);
                            break;
                        case ("CALL_VOLUNTEER"):
                            sendMessage(chatId1, botReplyMessage.callVolunteer());
                            break;
                        case ("ADOPTION_INFO"):
                            if (isPetDog) {
                                sendMessage(chatId1, botReplyMessage.dogAdoptionInfo());
                            } else {
                                sendMessage(chatId, botReplyMessage.catAdoptionInfo());
                            }
                            break;
                        case ("SEND_REPORT"):
                            sendMessage(chatId1, botReplyMessage.sendReport());
                            sendReport(update);
                        default:
                            sendMessage(chatId1, "Неверно выбран раздел");
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод-шаблон для создания сообщения от бота.
     * @param chatId
     * @param textToSend
     * @see BotUpdatesListener
     */
    private void sendMessage(Long chatId, String textToSend){
        SendMessage message = new SendMessage(chatId, textToSend);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Error during sending message: {}", response.description());
        }
    }

    /**
     * Метод для принятия отчётов пользователей.
     * @param update
     * @see BotUpdatesListener
     */
    private void sendReport(Update update){//todo: добработать метод
        Long chatId = update.message().chat().id();
        Matcher matcher = REPORT_PATTERN.matcher(update.message().caption());
        if (matcher.matches()) {
            String ration = matcher.group(3);
            String health = matcher.group(6);
            String habits = matcher.group(9);

            if (update.message().photo() != null) {
                GetFile fileId = new GetFile(update.message().photo()[update.message().photo().length - 1].fileId());
                GetFileResponse getFileResponse = telegramBot.execute(fileId);
                try {
                    String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
                    byte[] photo = telegramBot.getFileContent(getFileResponse.file());
                    Integer reportTime = update.message().date();
                    Date reportDate = new Date(reportTime * 1000);
                    reportDataService.uploadReportData(chatId, photo, ration, health, habits, reportDate, reportTime);
                    telegramBot.execute(sendMessage(chatId, "Ваш отчёт принят"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sendMessage(chatId, "Прикрепите к отчёту фотографию животного");
            }
        } else {
            sendMessage(chatId, "Неверный формат отчёта");
        }
    }

    /**
     * Метод, позволяющий пользователю отправить боту свои контакты.
     * @param update
     * @see BotUpdatesListener
     */
    private void sendContact(Update update) {
        String firstName = update.message().contact().firstName();
        String text = update.message().text();
        Long chatId = update.message().chat().id();
        if (text != null) {
            Matcher matcher = PHONE_PATTERN.matcher(text);
            if (matcher.matches()) {
                String phone = matcher.group(1);

                if (personDogRepository.existByChatId(chatId) || personCatRepository.existByChatId(chatId)) {
                    sendMessage(chatId, "Ваш номер уже есть в базе");
                }
                if (!isPetDog) {
                    personCatRepository.save(new PersonCat(firstName, phone, chatId));
                } else {
                    personDogRepository.save(new PersonDog(firstName, phone, chatId));
                }
                sendMessage(chatId, "Спасибо, что оставили контакты. Мы Вам перезвоним.");
            } else {
                sendMessage(chatId, "Некорректный формат номера телефона");
            }
        }
    }

    /**
     * Метод, с помощью которого бот рассылает сообщения-напоминания пользователям, проходящим испытательный срок,
     * о необходимости написать отчёт.
     * @see BotUpdatesListener
     */
    @Scheduled(cron = "0 00 21 * * *")
    public void reportReminder() {//todo: доработать метод
        LocalDateTime timeBoundary = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        List<ReportData> reports = reportDataRepository.findAll().stream()
                .filter(report ->
                    LocalDateTime.ofInstant(report.getLastMessage().toInstant(), ZoneId.systemDefault())
                            .isBefore(timeBoundary))
                .forEach(s -> sendMessage(s.getChatId(),
                        "Мы не получили Ваш сегодняшний отчёт. Пожалуйста, пришлите его"));
    }

}
