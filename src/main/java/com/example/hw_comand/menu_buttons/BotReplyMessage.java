package com.example.hw_comand.menu_buttons;

import org.springframework.stereotype.Service;

/**
 * Класс BotReplyMessage содержит методы для реакции бота на команды от пользователя.
 */
@Service
public class BotReplyMessage {

    public String getGreeting() {
        return "Привет!\nДобро пожаловать в наш приют для животных!\nВыберите питомца:";
    }

    public String getChoice() {
        return "Выберите необходимую информацию:";
    }

    public String getShelterInfo() {
        return "Наш приют поможет Вам выбрать себе питомца, но прежде" +
                " мы познакомим Вас и научим ухаживать за ним!\n" +
                "Вам предстоит пройти испытательный срок, прежде чем окончательно забрать питомца в новый дом.";
    }

    public String getContactsInfo() {
        return "Часы работы: Пн-Вс с 9:00 до 18:00\nАдрес: г. Астана, ул. Саргуль, 12";
    }

    public String getPassRegistration() {
        return "Для оформления пропуска на машину обратитесь к охране по телефону:\n" +
                "+79110000000";
    }

    public String getSafetyRules() {
        return "Правила техники безопасности на территории приюта:";
    }

    public String callback() {
        return "Пожалуйста, введите свой номер в формате: +79000000000";
    }

    public String callVolunteer() {
        return "Сейчас один из наших волонтёров придет к Вам на помощь";
    }

    public String dogAdoptionInfo() {
        return "Выбери необходимую информацию:\n" +
                "\n" +
                "/dogcommunicationrules - Правила знакомства с собакой при посещении приюта\n" +
                "/documentslist - Список документов, необходимых для того, чтобы взять животное из приюта\n" +
                "/dogtransportingrec - Список рекомендаций по транспортировке животного\n" +
                "/puppyadaptationrec - Список рекомендаций по обустройству дома для щенка\n" +
                "/adultdogadaptationrec - Список рекомендаций по обустройству дома для взрослой собаки\n" +
                "/disableddogadaptationrec - Список рекомендаций по обустройству дома для собаки с ограниченными возможностями\n" +
                "/cynologistadvice - Советы кинолога по первичному общению с собакой\n" +
                "/cynologistrec - Рекомендации по проверенным кинологам\n" +
                "/rejectionreasonslist - Причины отказа в \"усыновлении\" животного\n" +
                "/callbackrequest - Запросить обратный звонок\n" +
                "/callvolunteer - Позвать волонтёра в чат";
    }

    public String catAdoptionInfo() {
        return "Выбери необходимую информацию:\n" +
                "\n" +
                "/catcommunicationrules - Правила знакомства с кошкой при посещении приюта\n" +
                "/documentslist - Список документов, необходимых для того, чтобы взять животное из приюта\n" +
                "/cattransportingrec - Список рекомендаций по транспортировке животного\n" +
                "/kittyadaptationrec - Список рекомендаций по обустройству дома для котенка\n" +
                "/adultcatadaptationrec - Список рекомендаций по обустройству дома для взрослой кошки\n" +
                "/disabledcatadaptationrec - Список рекомендаций по обустройству дома для кошки с ограниченными возможностями\n" +
                "/felinologistadvice - Советы фелинолога по первичному общению с кошкой\n" +
                "/felinologistrec - Рекомендации по проверенным фелинологам\n" +
                "/rejectionreasonslist - Причины отказа в \"усыновлении\" животного\n" +
                "/callbackrequest - Запросить обратный звонок\n" +
                "/callvolunteer - Позвать волонтёра в чат";
    }

    public String sendReport() {
        return "Пожалуйста, пришлите свой ежедневный отчёт.\n" +
                "В него должны входить фото животного и текст в следующем формате:\n\n" +
                "Рацион:\n" +
                "Самочувствие:\n" +
                "Изменения в поведении:";
    }

}
