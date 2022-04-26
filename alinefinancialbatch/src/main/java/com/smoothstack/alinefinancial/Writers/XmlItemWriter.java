package com.smoothstack.alinefinancial.Writers;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XmlItemWriter extends AbstractItemStreamItemWriter {

    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public void write(List items) throws FileNotFoundException {
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlUsers.xml", true);

        userCache.getGeneratedUsers().forEach((k, v) -> {
            if (!userCache.getSeenUsers().contains(k)) {
                synchronized (UserCache.class) {
                    if (!userCache.getSeenUsers().contains(k)) {
                        System.out.println(v);
                        userXStream.toXML(v, userFs);
                        userCache.setSeenUser(k);
                    }
                }
            }
        });


        XStream cardXStream = new XStream();
        userXStream.alias("card", Card.class);
        FileOutputStream cardFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlCards.xml", true);

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

        /*XStream merchantXStream = new XStream();
        merchantXStream.alias("merchant", Merchant.class);*/
    }
}
