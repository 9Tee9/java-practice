package org.polina.practice;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
    private static final int ACCOUNT_NUMBER_LENGTH = 20;
    private final Random random = new Random();

    public BankAccount createAccount(BigDecimal initialBalance) {
        String accountNumber = generateAccountNumber(random);
        BankAccount account = new BankAccount(accountNumber, initialBalance);
        accounts.put(accountNumber, account);
        System.out.println("Created account: " + accountNumber + " with initial balance: " + initialBalance);
        return account;
    }

    public void transfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.withdraw(amount)) {
                    toAccount.deposit(amount);
                    System.out.println("Transferred " + amount + " from account " + fromAccount.getAccountNumber() +
                            " to account " + toAccount.getAccountNumber());
                } else {
                    System.out.println("Transfer failed: Insufficient funds in account " + fromAccount.getAccountNumber());
                }
            }
        }
    }

    public BigDecimal getTotalBalance() {
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (BankAccount account : accounts.values()) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }


    private String generateAccountNumber(Random random) {
            StringBuilder accNumber = new StringBuilder();
            for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
                accNumber.append(random.nextInt(10));
            }
            return accNumber.toString();
        }
    }


