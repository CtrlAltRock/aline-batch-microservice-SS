package com.smoothstack.alinefinancial.Writers;

import com.smoothstack.alinefinancial.Caches.CardCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileOutputStream;
import java.util.List;

public class ConsoleItemWriter extends AbstractItemStreamItemWriter {

    @Override
    public void write(List items) throws Exception {
        items.stream().forEach(System.out::println);
        System.out.println(" ************ writing each chunk ***********");
    }
}