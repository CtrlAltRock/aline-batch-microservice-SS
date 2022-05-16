package com.smoothstack.alinefinancial.utils;

import com.smoothstack.alinefinancial.models.Transaction;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public List<Transaction> loadTransactions(String path) {
        List<Transaction> transactions = new ArrayList<>();
        FileSystemResource file = new FileSystemResource(path);

        return transactions;
    }


}
