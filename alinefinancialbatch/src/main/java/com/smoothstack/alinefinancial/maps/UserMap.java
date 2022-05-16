package com.smoothstack.alinefinancial.maps;

import com.smoothstack.alinefinancial.generators.UserGenerator;
import com.smoothstack.alinefinancial.models.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic="UserMap")
public class UserMap {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();
    private static UserMap userMapInstance = null;

    public static UserMap getInstance() {
        if(userMapInstance == null) userMapInstance = new UserMap();
        return userMapInstance;
    }

    public synchronized void addGeneratedUser(Long userId, User user){
        try {
            generatedUsers.put(userId, user);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addGeneratedUser\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }

    public synchronized User getGeneratedUser(Long userId){
        return generatedUsers.get(userId);
    }

    public synchronized HashMap<Long, User> getGeneratedUsers(){
        return generatedUsers;
    }

    public synchronized User findUserOrGenerate(Long userId){
        try {
            UserGenerator userGenerator = UserGenerator.getInstance();
            if (getGeneratedUser(userId) == null) {
                synchronized (UserMap.class) {
                    if (getGeneratedUser(userId) == null) {
                        userGenerator.generateUser(userId, this);
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: findUserOrGenerate\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }

        // shouldn't worry about any exceptions coming from here, would just return null
        return getGeneratedUser(userId);
    }

}