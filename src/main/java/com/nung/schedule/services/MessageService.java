package com.nung.schedule.services;

import com.nung.schedule.entities.User;
import com.nung.schedule.entities.enums.BotStatus;
import com.nung.schedule.utils.DateUtil;
import com.nung.schedule.utils.HttpParser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Calendar;

@Service
public class MessageService {
    private final UserService userService;
    private final HTTPService httpService;

    public MessageService(UserService userService, HTTPService httpService) {
        this.userService = userService;
        this.httpService = httpService;
    }

    public SendMessage getMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        User sender = userService.getUser(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if(messageText != null) {
            message.setText(getResponse(messageText, sender));
        }
        return message;
    }

    private String getResponse(String command, User sender) {
        BotStatus status = sender.getBotStatus();

        switch(status) {
            case START :
                return processCommandWhenBotStatusIsStart(sender);
            case ASK_GROUP :
                return processCommandWhenBotStatusIsAskGroup(command, sender);
            case ASK_SCHEDULE :
                return processCommandWhenBotStatusIsAskSchedule(command, sender);
        }
        return null;
    }

    private String processCommandWhenBotStatusIsStart(User sender) {
        sender.setBotStatus(BotStatus.ASK_GROUP);
        userService.saveUserInfo(sender);
        return "Введіть свою групу:";
    }

    private String processCommandWhenBotStatusIsAskGroup(String command, User sender) {
        if(userService.checkGroup(command)) {
            sender.setGroupName(command);
            sender.setBotStatus(BotStatus.ASK_SCHEDULE);
            userService.saveUserInfo(sender);
            return "Збережено";
        } else {
            return "Даної групи не знайдено. Спробуйте ще раз:";
        }
    }

    private String processCommandWhenBotStatusIsAskSchedule(String command, User sender) {
        String resultOfRequest;
        switch(command) {
            case "Сьогодні" :
            case "/today" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getTodayDate(), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Завтра" :
            case "/tomorrow" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getTomorrowDate(), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Понеділок" :
            case "/monday" :
                resultOfRequest =  httpService.postRequestToURL(new DateUtil().getDateForNext(Calendar.MONDAY), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Вівторок" :
            case "/tuesday" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getDateForNext(Calendar.TUESDAY), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Середа" :
            case "/wednesday" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getDateForNext(Calendar.WEDNESDAY), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Четвер" :
            case "/thursday" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getDateForNext(Calendar.THURSDAY), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "П'ятниця" :
            case "/friday" :
                resultOfRequest = httpService.postRequestToURL(new DateUtil().getDateForNext(Calendar.FRIDAY), sender.getGroupName());
                return new HttpParser(resultOfRequest).parse();
            case "Змінити групу" :
            case "/change_group" :
                return processCommandWhenBotStatusIsStart(sender);
            case "Допомога" :
            case "/help" :
                return getHelpMessage();
        }
        return null;
    }

    private String getHelpMessage() {
        return "Для роботи з ботом можна застосовувати дані команди:\n\n" +
                "- \"Сьогодні\" або \"/today\" для перегляду розкладу на сьогодні\n" +
                "- \"Завтра\" або \"/tomorrow\" для перегляду розкладу на завтра\n" +
                "- \"Понеділок\" або \"/monday\" для перегляду розкладу на наступний понеділок\n" +
                "- \"Вівторок\" або \"/tuesday\" для перегляду розкладу на наступний вівторок\n" +
                "- \"Середа\" або \"/wednesday\" для перегляду розкладу на наступну середу\n" +
                "- \"Четвер\" або \"/thursday\" для перегляду розкладу на наступний четвер\n" +
                "- \"П'ятниця\" або \"/friday\" для перегляду розкладу на наступну п'ятницю\n" +
                "- \"Змінити групу\" або \"/change_group\" для зміни групи\n" +
                "- \"Допомога\" або \"/help\" для виклику цього меню\n";
    }
}
