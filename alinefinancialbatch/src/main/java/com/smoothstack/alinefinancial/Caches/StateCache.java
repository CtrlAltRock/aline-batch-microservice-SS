package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Models.State;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Map.entry;

@Component
public class StateCache {
    Map<String, State> stateCache = Map.ofEntries(
            entry( "AL", new State("Alabama", "AL", "Montgomery", new ArrayList<String>())),
            entry("AK", new State("Alaska", "AK", "Juneau", new ArrayList<String>())),
            entry("AZ", new State("Arizona", "AZ", "Phoenix", new ArrayList<String>())),
            entry("AR", new State("Arkansas", "AR", "Little Rock", new ArrayList<String>())),
            entry("CA", new State("California", "CA", "Sacramento", new ArrayList<String>())),
            entry("CO", new State("Colorado", "CO", "Denver", new ArrayList<String>())),
            entry("CT", new State("Connecticut", "CT", "Hartford", new ArrayList<String>())),
            entry("DE", new State("Delaware", "DE", "Dover", new ArrayList<String>())),
            entry("FL", new State("Florida", "FL", "Tallahassee", new ArrayList<String>())),
            entry("GA", new State("Georgia", "GA", "Atlanta", new ArrayList<String>())),
            entry("HI", new State("Hawaii", "HI", "Honolulu", new ArrayList<String>())),
            entry("ID", new State("Idaho", "ID", "Boise", new ArrayList<String>())),
            entry("IL", new State("Illinois", "IL", "Springfield", new ArrayList<String>())),
            entry("IN", new State("Indiana", "IN", "indianapolis", new ArrayList<String>())),
            entry("IA", new State("Iowa", "IA", "Des Moines", new ArrayList<String>())),
            entry("KS", new State("Kansas", "KS", "Topeka", new ArrayList<String>())),
            entry("KY", new State("Kentucky", "KY", "Frankfort", new ArrayList<String>())),
            entry("LA", new State("Louisiana", "LA", "Baton Rouge", new ArrayList<String>())),
            entry("ME", new State("Maine", "ME", "Augusta", new ArrayList<String>())),
            entry("MD", new State("Maryland", "MD", "Annapolis", new ArrayList<String>())),
            entry("MA", new State("Massachusetts", "MA", "Boston", new ArrayList<String>())),
            entry("MI", new State("Michigan", "MI", "Lansing", new ArrayList<String>())),
            entry("MN", new State("Minnesota", "MN", "Saint Paul", new ArrayList<String>())),
            entry("MS", new State("Mississippi", "MS", "Jackson", new ArrayList<String>())),
            entry("MO", new State("Missouri", "MO", "Jefferson City", new ArrayList<String>())),
            entry("MT", new State("Montana", "MT", "Helena", new ArrayList<String>())),
            entry("NE", new State("Nebraska", "NE", "Lincoln", new ArrayList<String>())),
            entry("NV", new State("Nevada", "NV", "Carson City", new ArrayList<String>())),
            entry("NH", new State("New Hampshire", "NH", "Concord", new ArrayList<String>())),
            entry("NJ", new State("New Jersey", "NJ", "Trenton", new ArrayList<String>())),
            entry("NM", new State("New Mexico", "NM", "Santa Fe", new ArrayList<String>())),
            entry("NY", new State("New York", "NY", "Albany", new ArrayList<String>())),
            entry("NC", new State("North Carolina", "NC", "Raleigh", new ArrayList<String>())),
            entry("ND", new State("North Dakota", "ND", "Bismarck", new ArrayList<String>())),
            entry("OH", new State("Ohio", "OH", "Columbus", new ArrayList<String>())),
            entry("OK", new State("Oklahoma", "OK", "Oklahoma City", new ArrayList<String>())),
            entry("OR", new State("Oregon", "OR", "Salem", new ArrayList<String>())),
            entry("PA", new State("Pennsylvania", "PA", "Harrisburg", new ArrayList<String>())),
            entry("RI", new State("Rhode Island", "RI", "Providence", new ArrayList<String>())),
            entry("SC", new State("South Carolina", "SC", "Columbia", new ArrayList<String>())),
            entry("SD", new State("South Dakota", "SD", "Pierre", new ArrayList<String>())),
            entry("TN", new State("Tennessee", "TN", "Nashville", new ArrayList<String>())),
            entry("TX", new State("Texas", "TX", "Austin", new ArrayList<String>())),
            entry("UT", new State("Utah", "UT", "Salt Lake City", new ArrayList<String>())),
            entry("VT", new State("Vermont", "VT", "Montpelier", new ArrayList<String>())),
            entry("VA", new State("Virginia", "VA", "Richmond", new ArrayList<String>())),
            entry("WA", new State("Washington", "WA", "Olympia", new ArrayList<String>())),
            entry("WV", new State("West Virginia", "WV", "Charleston", new ArrayList<String>())),
            entry("WI", new State("Wisconsin", "WI", "Madison", new ArrayList<String>())),
            entry("WY", new State("Wyoming", "WY", "Cheyenne", new ArrayList<String>())),
            entry("DC", new State("Washington D.C.", "D.C.", "Washington D.C.", new ArrayList<String>()))

    );

    private final HashMap<String, State> seenStates = new HashMap<String, State>();

    private static StateCache stateCacheInstance = null;

    public static StateCache getInstance() {
        if(stateCacheInstance == null) {
            stateCacheInstance = new StateCache();
        }
        return stateCacheInstance;
    }

    public HashMap<String, State> getSeenStates() {
        return seenStates;
    }

    public State addSeenStatesAndZip(String stateId, String zip) {
        State state = null;
        if(seenStates.containsKey(stateId)) {
            if(!seenStates.get(stateId).getZipCodes().contains(zip)) {
                seenStates.get(stateId).getZipCodes().add(zip);
                state = seenStates.get(stateId);
            }
        }
        else {
            seenStates.put(stateId, stateCache.get(stateId));
            seenStates.get(stateId).getZipCodes().add(zip);
            state = seenStates.get(stateId);
        }
        return state;
    }
}
