package com.vojtechruzicka.javafxweaverexample.service;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    public void showAlert(AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertType.title);
        alert.setHeaderText(alertType.header);
        alert.setContentText(alertType.message);
        alert.show();
    }


    public enum AlertType {
        PASSWORD_REGEX_WARNING(
                "Некорректно задан пароль",
                "Обратите внимание на требования",
                "• Не менее 8 символов\n" +
                "• Содержит как минимум одну цифру\n" +
                "• Содержит по крайней мере один символ нижнего и один символ верхнего алфавита\n" +
                "• Содержит хотя бы один символ из набора специальных символов (@#%$^ и т.д.)\n" +
                "• Не содержит пробела, табуляции и т.д."),
        PASSWORD_OR_LOGIN_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "Поле ввода логина или пароля не должно быть пустым."),
        USER_NOT_FOUND(
                "Ошибка при входе",
                "Перейдите к регистрации",
                "Пользователя с таким логином не существует"),
        BRAND_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "• Поле ввода марки не должно быть пустым\n" +
                        "• Пример серийного номера 50-9627-6043."),
        ERROR_METHOD(
                "Ошибка при заполнении матрицы",
                "Повторите попытку",
                "Проверьте все графы."
        ),
        PARKING_ERROR(
                "Ошибка при добавлении машины на автостоянку",
                "Закончились места на автостоянке",
                "• Приносим свои извинения\n" +
                        "• Мы приложим все усилия для того,\n" +
                        "  чтобы в кратчайшие сроки расширить нашу автостоянку."
        ),
        PARKING_ERROR_BALANCE(
                "Ошибка при добавлении машины на автостоянку",
                "Недостаточно средств на счету",
                "• Пополните баланс."
        ),
        CAR_ALREADY_IN_PARK(
                "Ошибка при добавлении машины на автостоянку",
                        "Внимание",
                        "• Машину уже на стоянке."
        ),
        EXPAND_PARKING(
                "Выполненно",
                "Расширение автостоянки",
                "• Цель Z1 оказалась наулучшей альтернативой\n" +
                        "• Автостоянка расширена в 2 раза."
        ),
        COST_BY_TWO(
                "Выполненно",
                "Пользователи удалены",
                "• Цель Z2 оказалась наулучшей альтернативой\n" +
                        "• Стоимость парковки уменьшена в 2 раза."
        ),
        EXPAND_PARKING_N_SEATS(
                "Выполненно",
                "Пользователи удалены",
                "• Цель Z3 оказалась наулучшей альтернативой\n" +
                        "• Автостоянка расширена на 5 мест."
        ),
        SIMILAR_GOALS(
                "Выполненно",
                "Внимание",
                "• Две цели набрали одинаковое\n" +
                        "  кол-во баллов."
        ),
        ACCESS_ERROR(
                "Ошибка доступа",
                "Внимание",
                "Вы пытаетесь забрать не свою машину."
        ),
        NO_CONNECTED(
                "Ошибка соединения",
                "Внимание",
                "Отсутствует соединение с серверов."
        ),
        INCORRECT_DATA(
                "Некорректные данные",
                "Внимание",
                "Неправильно введёт пароль."
        ),
        LOGIN_IS_BUSY(
                "Некорректные данные",
                "Внимание",
                "Логин уже занят."
        );

        private final String title;
        private final String message;
        private final String header;

        AlertType(String title, String header, String message) {
            this.title = title;
            this.header = header;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getTitle() {
            return title;
        }
    }
}
