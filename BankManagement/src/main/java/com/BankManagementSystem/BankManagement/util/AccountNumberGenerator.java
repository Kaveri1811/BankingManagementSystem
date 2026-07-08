package com.BankManagementSystem.BankManagement.util;

import java.util.Random;

public class AccountNumberGenerator {

    private AccountNumberGenerator() {
    }

    public static String generate() {

        Random random = new Random();

        long number =
                1000000000L +
                (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(number);
    }
}