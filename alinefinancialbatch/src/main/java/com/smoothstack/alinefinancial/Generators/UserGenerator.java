package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Maps.UserMap;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j(topic = "UserGenerator")
public class UserGenerator {

    private final Faker faker = new Faker();

    private static UserGenerator userGeneratorInstance = null;

    public static UserGenerator getInstance() {
        try {
            if (userGeneratorInstance == null) {
                synchronized (UserGenerator.class) {
                    if (userGeneratorInstance == null) {
                        userGeneratorInstance = new UserGenerator();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return userGeneratorInstance;
    }

    public User generateUser(Long userId, UserMap uc) {
        User user = new User();

        try {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            user.setId(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(firstName + "." + lastName + "@smoothceeplusplus.com");
            user.setCards(new ArrayList<>());
            user.setInsufficientBalanceTransactions(0L);
            user.setDeposit(new ArrayList<>());
            uc.addGeneratedUser(userId, user);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: generateUser\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }

        return user;
    }

}
