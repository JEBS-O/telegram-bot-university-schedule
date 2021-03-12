package com.nung.schedule.services;

import com.nung.schedule.entities.User;
import com.nung.schedule.entities.enums.BotStatus;
import com.nung.schedule.repositories.UserRepository;
import com.nung.schedule.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HTTPService httpService;

    public UserService(UserRepository userRepository, HTTPService httpService) {
        this.userRepository = userRepository;
        this.httpService = httpService;
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseGet(() -> addUser(id));
    }

    public User addUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setGroupName(null);
        user.setBotStatus(BotStatus.START);
        userRepository.save(user);
        return user;
    }

    public boolean checkGroup(String groupName) {
        DateUtil dateUtil = new DateUtil();
        Calendar calendar = dateUtil.getCalendar();
        String dateFrom = dateUtil.getFormat().format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 50);
        String dateTo = dateUtil.getFormat().format(calendar.getTime());
        String result = httpService.postRequestToURL(dateFrom, dateTo, groupName);
        return !(result.contains("За вашим запитом записів не знайдено.") || result.contains("У програмі виникла помилка. Зверніться до розробників."));
    }

    public void saveUserInfo(User user) {
        userRepository.save(user);
    }
}