package com.example.hw_comand.listener;

import com.example.hw_comand.menu_buttons.BotReplyMessage;
import com.example.hw_comand.menu_buttons.ButtonMenu;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private int reportDays;
    private ReportDataService reportDataService;
    private ReportDataRepository reportRepository;
    private PersonDogRepository personDogRepository;
    private PersonCatRepository personCatRepository;

    private boolean isPetDog = false;

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
                Calendar calendar = new GregorianCalendar();
                reportDays = reportRepository.findAll().stream()
                        .filter(s -> s.getChatId() == chatId)
                        .count() + 1;
                int daysDifference = calendar.get(Calendar.DAY_OF_MONTH);
                Integer lastReportTime = reportRepository.findAll().stream()
                        .filter(s -> s.getChatId() == chatId)
                        .map(ReportData::getLastMessages)
                        .max(Integer::compare)
                        .orElseGet(() -> null);

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
                            break;
                        case ("CALL_VOLUNTEER"):
                            sendMessage(chatId1, botReplyMessage.callVolunteer());
                            break;
                        case ("ADOPTION_INFO"):
                            if (isPetDog) {
                                sendMessage(chatId1, botReplyMessage.dogAdoptionInfo());
                            } else {sendMessage(chatId, botReplyMessage.catAdoptionInfo());
                            }
                            break;
                        case ("SEND_REPORT"):
                            sendMessage(chatId1, botReplyMessage.sendReport());
                        default:
                            sendMessage(chatId1, "Неверно выбран раздел");
                    }
                } else if (lastReportTime != null) {
                    Date lastDateSendMessage = new Date(lastReportTime * 1000);
                    int numberOfDay = lastDateSendMessage.getDate();

                    if (reportDays < 30 ) {
                        if (daysDifference != numberOfDay) {
                            if (update.message() != null && update.message().photo() != null && update.message().caption() != null) {
                                getReport(update);
                            }
                        } else {
                            if (update.message() != null && update.message().photo() != null && update.message().caption() != null) {
                                sendMessage(chatId, "Вы уже отправляли отчет сегодня");
                            }
                        }
                        if (reportDays == 31) {
                            sendMessage(chatId, "Вы прошли испытательный срок по уходу за питомцем!");
                        }
                    } else if (update.message() != null && update.message().photo() != null && update.message().caption() != null) {
                        getReport(update);
                    } else if (update.message() != null && update.message().photo() != null && update.message().caption() == null) {
                        sendMessage(chatId, "Дополните отчет описанием!");
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
        try {
            telegramBot.execute(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Метод, позволяющий пользователю поделиться с ботом своими контактами.
     * @param update
     * @see BotUpdatesListener
     */
    public void shareContact(Update update) {
        if (update.message().contact() != null) {
            String firstName = update.message().contact().firstName();
            String phone = update.message().contact().phoneNumber();
            Long chatId = update.message().chat().id();
            List<Long> dogPersonsChatId = personDogRepository.findAll().stream()
                    .filter(i -> i.getChatId() == chatId)
                    .collect(Collectors.toList());
            List<Long> catPersonsChatId = personCatRepository.findAll().stream()
                    .filter(i -> i.getChatId() == chatId)
                    .collect(Collectors.toList());

            if (!dogPersonsChatId.isEmpty() || !catPersonsChatId.isEmpty()) {
                sendMessage(chatId, "Вы уже есть в базе!");
            }
            if (!isPetDog) {
                personCatRepository.save(new PersonCat(firstName, phone, chatId));
            } else {
                personDogRepository.save(new PersonDog(firstName, phone, chatId));
            }
            sendMessage(chatId, "Спасибо, что оставили контакты. Мы Вам перезвоним.");
        }
    }

    /**
     * Метод для принятия отчётов пользователей.
     * @param update
     * @see BotUpdatesListener
     */
    public void getReport(Update update) {
        Pattern pattern = Pattern.compile("(Рацион:)(\\s+([А-я\\d\\s.,!?:;]+))\n" +
                "(Самочувствие:)(\\s+([А-я\\d\\s.,!?:;]+))\n" +
                "(Изменения в поведении:)(\\s+([А-я\\d\\s.,!?:;]+))");
        Matcher matcher = pattern.matcher(update.message().caption());
        if (matcher.matches()) {
            String ration = matcher.group(3);
            String health = matcher.group(6);
            String habits = matcher.group(9);

            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();
                String fullPathPhoto = file.filePath();

                long timeDate = update.message().date();
                Date dateSendMessage = new Date(timeDate * 1000);
                byte[] fileContent = telegramBot.getFileContent(file);
                reportDataService.uploadReportData(update.message().chat().id(), fileContent, file,
                        ration, health, habits, fullPathPhoto, dateSendMessage, timeDate, reportDays);

                telegramBot.execute(new SendMessage(update.message().chat().id(), "Отчет успешно принят!"));

                System.out.println("Отчет успешно принят от: " + update.message().chat().id());
            } catch (IOException e) {
                System.out.println("Ошибка при загрузке фото");
            }
        } else {
            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();
                String fullPathPhoto = file.filePath();

                long timeDate = update.message().date();
                Date dateSendMessage = new Date(timeDate * 1000);
                byte[] fileContent = telegramBot.getFileContent(file);
                reportDataService.uploadReportData(update.message().chat().id(), fileContent, file, update.message().caption(),
                        fullPathPhoto, dateSendMessage, timeDate, reportDays);

                telegramBot.execute(new SendMessage(update.message().chat().id(), "Ваш отчёт принят!"));
            } catch (IOException e) {
                System.out.println("Ошибка при загрузке фото");
            }
        }
    }

}
