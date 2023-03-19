package com.example.hw_comand.menu_buttons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum ButtonConstant содержит перечисление названий кнопок с соответствующими им командами для пользователя.
 */
@RequiredArgsConstructor
@Getter
public enum ButtonConstant {
    CAT("CAT", "Кошка"),
    DOG("DOG", "Собака"),
    GENERAL_INFO("GENERAL_INFO", "Узнать о приюте"),
    ADOPTION_INFO("ADOPTION_INFO", "Как взять питомца из приюта"),
    SEND_REPORT("SEND_REPORT", "Прислать отчёт о питомце"),
    CALL_VOLUNTEER("CALL_VOLUNTEER", "Позвать волонтёра"),
    ABOUT_SHELTER("ABOUT_SHELTER", "Подробнее о приюте"),
    SCHEDULE_ADDRESS_ROUTE("SCHEDULE_ADDRESS_ROUTE", "Расписание, адрес, маршрут"),
    SAFETY_RULES("SAFETY_RULES", "Техника безопасности"),
    CALLBACK("CALLBACK", "Оставить контакты для связи");

    private final String name;
    private final String command;

}
