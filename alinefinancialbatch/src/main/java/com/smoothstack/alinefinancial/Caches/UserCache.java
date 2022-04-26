package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.UserGenerator;
import com.smoothstack.alinefinancial.Models.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserCache {

    private final HashMap<Long, User> generatedUsers = new HashMap<>();
    private final Map<Long, User> syncGeneratedUsers = Collections.synchronizedMap(generatedUsers);
    private final Set<Long> seenUsers = new HashSet<>();
    private static UserCache userCacheInstance = null;

    public static UserCache getInstance() {
        if(userCacheInstance == null) userCacheInstance = new UserCache();
        return userCacheInstance;
    }

    public synchronized void addGeneratedUser(Long userId, User user){
        syncGeneratedUsers.put(userId, user);
    }

    public User getGeneratedUser(Long userId){
        return syncGeneratedUsers.get(userId);
    }

    public Map<Long, User> getGeneratedUsers(){
        return syncGeneratedUsers;
    }

    public Set<Long> getSeenUsers(){
        return seenUsers;
    }

    public void setSeenUser(Long id){
        seenUsers.add(id);
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