package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserGenerator {

    private final Faker faker = new Faker();

    private static UserGenerator userGeneratorInstance = null;

    public static UserGenerator getInstance() {
        if(userGeneratorInstance == null) userGeneratorInstance= new UserGenerator();
        return userGeneratorInstance;
    }

    public synchronized User generateUser(Long userId, UserCache uc) {
        String firstName= faker.name().firstName();
        String lastName= faker.name().lastName();
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(firstName + "." + lastName + "@smoothceeplusplus.com");
        user.setCards(new ArrayList<>());
        uc.addGeneratedUser(userId, user);
        //System.out.println(user);
        return user;
    }

}