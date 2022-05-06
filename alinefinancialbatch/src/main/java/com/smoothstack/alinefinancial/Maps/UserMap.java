package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Generators.UserGenerator;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic="UserCache")
public class UserMap {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();
    private static UserMap userCacheInstance = null;

    public static UserMap getInstance() {
        if(userCacheInstance == null) userCacheInstance = new UserMap();
        return userCacheInstance;
    }

    public void addGeneratedUser(Long userId, User user){
        generatedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return generatedUsers.get(userId);
    }

    public HashMap<Long, User> getGeneratedUsers(){
        return generatedUsers;
    }

    public User findUserOrGenerate(Long userId){
        UserGenerator userGenerator = UserGenerator.getInstance();
        if(getGeneratedUser(userId) == null){
            synchronized (UserMap.class){
                if(getGeneratedUser(userId) == null){
                    userGenerator.generateUser(userId, this);
                }
            }
        }
        return getGeneratedUser(userId);
    }

}