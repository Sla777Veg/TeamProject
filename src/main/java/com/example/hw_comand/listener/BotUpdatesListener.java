package com.example.hw_comand.listener;

import com.example.hw_comand.menu_buttons.BotReplyMessage;
import com.example.hw_comand.menu_buttons.ButtonConstant;
import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.example.hw_comand.model.PersonCat;
import com.example.hw_comand.model.PersonDog;
import com.example.hw_comand.model.PetType;
import com.example.hw_comand.model.ReportData;
import com.example.hw_comand.repository.PersonCatRepository;
import com.example.hw_comand.repository.PersonDogRepository;
import com.example.hw_comand.repository.ReportDataRepository;
import com.example.hw_comand.service.ReportDataService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    private static final Pattern REPORT_PATTERN = Pattern.compile("""
            (Рацион:)(\\s)(\\W+)
            (Самочувствие:)(\\s)(\\W+)
            (Изменения в поведении:)(\\s)(\\W+)""");

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

                if (update.message() != null) {
                    Message message = update.message();
                    Long chatId = message.chat().id();
                    String text = message.text();

                    if ("/start".equals(text)) {
                        buttonMenu.choosePetMenu(chatId);
                    } else if (message.photo() != null) {
                        sendReport(update);
                    } else if (text != null) {
                        sendContact(update);
                    } else {
                        sendMessage(chatId, "Неверный формат сообщения");
                    }
                } else if (update.callbackQuery() != null) {
                    Long chatId = update.callbackQuery().message().chat().id();
                    CallbackQuery callbackQuery = update.callbackQuery();
                    String data = callbackQuery.data();
                    String name = update.callbackQuery().message().chat().firstName();

                    switch (data) {
                        case (ButtonConstant.CAT):
                            personCatRepository.save(new PersonCat(name, chatId, PetType.CAT));
                            buttonMenu.chooseMainMenu(chatId);
                            break;
                        case (ButtonConstant.DOG):
                            personDogRepository.save(new PersonDog(name, chatId, PetType.DOG));
                            buttonMenu.chooseMainMenu(chatId);
                            break;
                        case (ButtonConstant.GENERAL_INFO):
                            buttonMenu.chooseGeneralInfo(chatId);
                            break;
                        case (ButtonConstant.ABOUT_SHELTER):
                            sendMessage(chatId, botReplyMessage.getShelterInfo());
                            break;
                        case (ButtonConstant.SCHEDULE_ADDRESS_ROUTE):
                            sendMessage(chatId, botReplyMessage.getContactsInfo());
                            break;
                        case (ButtonConstant.PASS_REGISTRATION):
                            sendMessage(chatId, botReplyMessage.getPassRegistration());
                            break;
                        case (ButtonConstant.SAFETY_RULES):
                            sendMessage(chatId, botReplyMessage.getSafetyRules());
                            break;
                        case (ButtonConstant.CALLBACK):
                            sendMessage(chatId, botReplyMessage.callback());
                            break;
                        case (ButtonConstant.CALL_VOLUNTEER):
                            sendMessage(chatId, botReplyMessage.callVolunteer());
                            break;
                        case (ButtonConstant.ADOPTION_INFO):
                            if (personDogRepository.findByChatId(chatId).isPresent()) {
                                sendMessage(chatId, botReplyMessage.dogAdoptionInfo());
                            } else if (personCatRepository.findByChatId(chatId).isPresent()){
                                sendMessage(chatId, botReplyMessage.catAdoptionInfo());
                            }
                            break;
                        case (ButtonConstant.SEND_REPORT):
                            sendMessage(chatId, botReplyMessage.sendReport());
                            break;
                        default:
                            sendMessage(chatId, "Неверно выбран раздел");
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
    public void sendMessage(Long chatId, String textToSend){
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
    public void sendReport(Update update) {
        Long chatId = update.message().chat().id();
        Integer reportTime = update.message().date();
        Date reportDate = new Date(TimeUnit.MINUTES.toMillis(reportTime));
        PhotoSize telegramPhoto = update.message().photo()[update.message().photo().length - 1];
        String caption = update.message().caption();
        String text = update.message().text();
        if (caption != null) {
            Matcher matcher = REPORT_PATTERN.matcher(caption);
            if (matcher.matches()) {
                String description = matcher.group(0);
                savePhoto(chatId, reportDate, description, telegramPhoto);
            } else {
                sendMessage(chatId, "Некорректный формат описания");
            }
        } else if (text != null) {
            Matcher matcher = REPORT_PATTERN.matcher(text);
            if (matcher.matches()) {
                String description = matcher.group(0);
                savePhoto(chatId, reportDate, description, telegramPhoto);
            } else {
                sendMessage(chatId, "Некорректный формат описания");
            }
        } else {
            sendMessage(chatId, "Дополните отчёт описанием");
        }
    }

    /**
     * Метод, позволяющий пользователю отправить боту свои контакты.
     * @param update
     * @see BotUpdatesListener
     */
    public void sendContact(Update update) {
        String firstName = update.message().chat().firstName();
        String text = update.message().text();
        Long chatId = update.message().chat().id();
        Matcher matcher = PHONE_PATTERN.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(0);

            if (personCatRepository.findByChatId(chatId).isPresent()) {
                PersonCat personCat = personCatRepository.findByChatId(chatId).get();
                if (personCat.getPhone() != null) {
                    sendMessage(chatId, "Ваш номер уже есть в базе");
                } else {
                    personCat.setPhone(phone);
                    personCat.setName(firstName);
                    personCat.setChatId(chatId);
                    sendMessage(chatId, "Спасибо, что оставили контакты. Мы Вам перезвоним.");
                }
            } else if (personDogRepository.findByChatId(chatId).isPresent()) {
                PersonDog personDog = personDogRepository.findByChatId(chatId).get();
                if (personDog.getPhone() != null) {
                    sendMessage(chatId, "Ваш номер уже есть в базе");
                } else {
                    personDog.setPhone(phone);
                    personDog.setName(firstName);
                    personDog.setChatId(chatId);
                    sendMessage(chatId, "Спасибо, что оставили контакты. Мы Вам перезвоним.");
                }
            }

        } else {
            sendMessage(chatId, "Некорректный формат сообщения");
        }
    }

    /**
     * Метод, с помощью которого бот рассылает сообщения-напоминания пользователям, проходящим испытательный срок,
     * о необходимости написать отчёт.
     * @see BotUpdatesListener
     */
    @Scheduled(cron = "0 0 21 * * *")
    public void reportReminder() {
        LocalDateTime timeBoundary = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        Collection<ReportData> reports = reportDataRepository.findAll();
        reports.stream()
                .filter(i -> i.getReportDays() < 30)
                .filter(report ->
                        LocalDateTime.ofInstant(report.getLastMessage().toInstant(), ZoneId.systemDefault())
                                .isBefore(timeBoundary))
                .forEach(s -> sendMessage(s.getChatId(),
                        "Мы не получили Ваш сегодняшний отчёт. Пожалуйста, пришлите его"));
    }

    /**
     * Метод, с помощью которого бот рассылает сообщения-поздравления пользователям, прошедшим испытательный срок.
     * @see BotUpdatesListener
     */
    @Scheduled(cron = "0 0 10 * * *")
    public void congratulate() {
        Collection<ReportData> reports = reportDataRepository.findAll();
        reports.stream()
                .filter(i -> i.getReportDays() == 30)
                .forEach(s -> sendMessage(s.getChatId(),
                        "Поздравляем, вы прошли испытательный срок!"));
    }

    /**
     * Метод-шаблон для сохранения фотографии от пользователя.
     * @param chatId
     * @param reportDate
     * @param description
     * @param telegramPhoto
     * @see BotUpdatesListener
     */
    public void savePhoto(Long chatId, Date reportDate, String description, PhotoSize telegramPhoto) {
        GetFile getFile = new GetFile(telegramPhoto.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFile);
        if (getFileResponse.isOk()) {
            try {
                String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
                byte[] photo = telegramBot.getFileContent(getFileResponse.file());
                Path path = Files.write(Paths.get(UUID.randomUUID() + "." + extension), photo);

                reportDataService.uploadReportData(chatId, photo, path, description, reportDate);
                sendMessage(chatId, "Ваш отчёт принят");
            } catch (IOException e) {
                sendMessage(chatId, "Ошибка при загрузке фотографии");
            }
        }
        logger.error("Error during saving photo: {}", getFileResponse.description());
    }

}
