package com.smoothstack.alinefinancial.Writers;

import com.smoothstack.alinefinancial.Models.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Writers {

    @Bean
    @StepScope
    public static CompositeItemWriter<Object> compositeItemWriter() {
        CompositeItemWriter compWriter = new CompositeItemWriter();
        compWriter.setDelegates(Arrays.asList(new ConsoleItemWriter()));
        //compWriter.setDelegates(Arrays.asList(new ConsoleItemWriter(), Writers.XmlUserWriter("XmlUsers.xml")));
        return compWriter;
    }

    @Bean
    @StepScope
    public static ItemWriter<User> XmlUserWriter(String fileName) {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/" + fileName);
        XStreamMarshaller userMarshaller = new XStreamMarshaller();
        userMarshaller.setAliases(Collections.singletonMap(
                "user",
                User.class
        ));
        return new StaxEventItemWriterBuilder<User>()
                .name("XmlUserWriter")
                .resource(path)
                .marshaller(userMarshaller)
                .rootTagName("Users")
                .overwriteOutput(true)
                .build();
    }

    @Bean
    @StepScope
    public static synchronized ItemWriter<Card> XmlCardWriter(String fileName) {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/" + fileName);
        XStreamMarshaller cardMarshaller = new XStreamMarshaller();
        cardMarshaller.setAliases(Collections.singletonMap(
                "card",
                Card.class
        ));
        return new StaxEventItemWriterBuilder<Card>()
                .name("XmlCardWriter")
                .resource(path)
                .marshaller(cardMarshaller)
                .rootTagName("Cards")
                .overwriteOutput(true)
                .build();
    }

    @Bean
    @StepScope
    public static ItemWriter<Merchant> XmlMerchantWriter(String fileName) {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/" + fileName);
        XStreamMarshaller merchantMarshaller = new XStreamMarshaller();
        merchantMarshaller.setAliases(Collections.singletonMap(
                "merchant",
                Merchant.class
        ));
        return new StaxEventItemWriterBuilder<Merchant>()
                .name("XmlMerchantWriter")
                .resource(path)
                .marshaller(merchantMarshaller)
                .rootTagName("Merchants")
                .overwriteOutput(true)
                .build();
    }

    @Bean
    @StepScope
    public static ItemWriter<Transaction> XmlTransactionWriter() {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/xmlTransaction.xml");
        XStreamMarshaller transactionMarshaller = new XStreamMarshaller();
        transactionMarshaller.setAliases(Collections.singletonMap(
                "transaction",
                Transaction.class
        ));
        return new StaxEventItemWriterBuilder<Transaction>()
                .name("XmlTransactionWriter")
                .resource(path)
                .marshaller(transactionMarshaller)
                .rootTagName("Transactions")
                .build();
    }

    @Bean
    @StepScope
    public static ItemWriter<TransactionV2> XmlTransactionV2Writer() {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/xmlTransactionV2.xml");
        XStreamMarshaller transactionV2Marshaller = new XStreamMarshaller();
        transactionV2Marshaller.setAliases(Collections.singletonMap(
                "TransactionV2",
                TransactionV2.class
        ));
        return new StaxEventItemWriterBuilder<TransactionV2>()
                .name("XmlTransactionV2Writer")
                .resource(path)
                .marshaller(transactionV2Marshaller)
                .rootTagName("TransactionsV2")
                .build();
    }

    @Bean
    @StepScope
    public static FlatFileItemWriter<TransactionV2> FlatFileTransactionV2Writer() {
        FlatFileItemWriter<TransactionV2> writer = new FlatFileItemWriter<>();

        writer.setResource(new FileSystemResource("src/main/ProcessedOutFiles/TransactionV2.csv"));
        writer.setAppendAllowed(false);
        writer.setShouldDeleteIfEmpty(true);
        writer.setShouldDeleteIfExists(true);

        writer.setLineAggregator(new DelimitedLineAggregator<TransactionV2>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<TransactionV2>() {
                    {
                        setNames(new String[]{"user", "merchant", "year", "month", "day", "time", "amount", "method", "errors", "mcc", "fraud"});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public static StaxEventItemWriter<User> writer11() throws Exception {
        StaxEventItemWriter<User> staxEventItemWriter = new StaxEventItemWriter<>();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("user", User.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        staxEventItemWriter.setRootTagName("users");
        staxEventItemWriter.setMarshaller(marshaller);
        String outFilePath = "src/main/ProcessedOutFiles/XmlCards1.xml";
        staxEventItemWriter.setResource(new FileSystemResource(outFilePath));

        staxEventItemWriter.afterPropertiesSet();

        return staxEventItemWriter;
    }
    // Need State writer



}
