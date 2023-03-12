package com.example.hw_comand.service.impl;

import com.example.hw_comand.service.ShelterInfoService;
import org.springframework.stereotype.Service;

@Service
public class ShelterInfoServiceImpl implements ShelterInfoService {
    @Override
    public String getInfo() {
        return "Наш приют поможет Вам выбрать себе питомца, но прежде" +
                " мы познакомим Вас с собакой и научим ухаживать за ней!\n" +
                "Вам предстоит пройти испытательный срок, прежде чем окончательно забрать питомца в новый дом";
    }

    @Override
    public String getGreeting() {
        return "Привет!\nДобро пожаловать в наш приют для животных!\nВыбери необходимую информацию:";
    }

    @Override
    public String getContactsInfo() {
        return "Часы работы: Пн-Вс с 9:00 до 18:00\nАдрес: г. Астана, ул. Саргуль, 12\nСхема проезда";
    }

    @Override
    public String getSafetyRules() {
        return "Правила техники безопасности на территории приюта:";
    }

    @Override
    public String callback() {
        return "Спасибо, что оставили свой телефон, мы обязательно Вам перезвоним";
    }

    @Override
    public String callVolunteer() {
        return "Сейчас один из наших волонтёров придет к Вам на помощь";
    }

    @Override
    public String adoptionInfo() {
        return "Выбери необходимую информацию:\n" +
                "\n" +
                "/dogcommunicationrules - Правила знакомства с собакой при посещении приюта\n" +
                "/documentslist - Список документов, необходимых для того, чтобы взять собаку из приюта\n" +
                "/transportingrec - Список рекомендаций по транспортировке животного\n" +
                "/puppyadaptationrec - Список рекомендаций по обустройству дома для щенка\n" +
                "/adultdogadaptationrec - Список рекомендаций по обустройству дома для взрослой собаки\n" +
                "/disableddogadaptationrec - Список рекомендаций по обустройству дома для собаки с ограниченными возможностями\n" +
                "/cynologistadvice - Советы кинолога по первичному общению с собакой\n" +
                "/cynologistrec - Рекомендации по проверенным кинологам\n" +
                "/rejectionreasonslist - Причины отказа в \"усыновлении\" собаки\n" +
                "/callbackrequest - запросить обратный звонок\n" +
                "/callvolunteer - позвать волонтёра в чат";
    }

    @Override
    public String sendReport() {
        return "Пожалуйста, пришлите свой ежедневный отчёт\n" +
                "В него должны входить:\nФото животного\nРацион животного\nОбщее самочувствие и привыкание к новому месту\n" +
                "Изменение в поведении, т.е. отказ от старых привычек, приобретение новых";
    }
}
