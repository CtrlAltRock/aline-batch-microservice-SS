package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.UserGenerator;
import com.smoothstack.alinefinancial.Models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j(topic="UserCache")
public class UserCache {

    private final HashMap<Long, User> syncGeneratedUsers = new HashMap<>();

 //   private final Map<Long, User> syncGeneratedUsers = Collections.synchronizedMap(new HashMap<>());
    private static UserCache userCacheInstance = null;

    public static UserCache getInstance() {
        if(userCacheInstance == null) userCacheInstance = new UserCache();
        return userCacheInstance;
    }

    public void addGeneratedUser(Long userId, User user){
        syncGeneratedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return syncGeneratedUsers.get(userId);
    }

    public HashMap<Long, User> getGeneratedUsers(){
        return syncGeneratedUsers;
    }

    public User findUserOrGenerate(Long userId){
        UserGenerator userGenerator = UserGenerator.getInstance();
        if(getGeneratedUser(userId) == null){
            synchronized (UserGenerator.class){
                if(getGeneratedUser(userId) == null){
                    userGenerator.generateUser(userId, this);
                }
            }
        }
        return getGeneratedUser(userId);
    }

}