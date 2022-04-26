package com.smoothstack.alinefinancial.Writers;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Caches.StateCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.State;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XmlItemWriter extends AbstractItemStreamItemWriter {


    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();
    private final MerchantCache merchantCache = MerchantCache.getInstance();
    private final StateCache stateCache = StateCache.getInstance();

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";


    @Override
    public void write(List items) throws FileNotFoundException {
        XStream statesXStream = new XStream();

        statesXStream.alias("state", State.class);
        FileOutputStream statesFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlProcessedStates.xml", true);
        statesXStream.toXML(XML_HEADER, statesFs);
        stateCache.getSeenStates().forEach((k, v) -> {
            statesXStream.toXML(v, statesFs);
        });

        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlUsers.xml", true);
        userXStream.toXML(XML_HEADER, userFs);
        userCache.getGeneratedUsers().forEach((k, v) -> {
            if (!userCache.getSeenUsers().contains(k)) {
                synchronized (UserCache.class) {
                    if (!userCache.getSeenUsers().contains(k)) {
                        userXStream.toXML(v, userFs);
                        userCache.setSeenUser(k);
                    }
                }
            }
        });


        XStream cardXStream = new XStream();
        userXStream.alias("card", Card.class);
        FileOutputStream cardFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlCards.xml", true);
        cardXStream.toXML(XML_HEADER, cardFs);
        cardCache.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                if (!cardCache.getSeenCards().contains(card.getId())) {
                    synchronized (CardCache.class) {
                        if (!cardCache.getSeenCards().contains(card.getId())) {
                            cardXStream.toXML(card, cardFs);
                            cardCache.setSeenCards(card.getId());
                        }
                    }
                }
            }
        });

        XStream merchantXStream = new XStream();
        userXStream.alias("merchant", Merchant.class);
        FileOutputStream merchantFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlMerchants.xml", true);
        merchantXStream.toXML(XML_HEADER, merchantFs);
        merchantCache.getGeneratedMerchants().forEach((k, v) -> {
            if (!merchantCache.getSeenMerchants().contains(k)) {
                synchronized (MerchantCache.class) {
                    if (!merchantCache.getSeenMerchants().contains(k)) {
                        merchantXStream.toXML(v, merchantFs);
                        merchantCache.setSeenMerchants(k);
                    }
                }
            }
        });
    }
}
