package org.polina.practice;

import java.math.BigDecimal;

public class BankAccount {
    private final String accountNumber;
    private BigDecimal balance;

    public BankAccount(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public synchronized void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            System.out.println("Deposited " + amount + " to account " + accountNumber);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public synchronized boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            System.out.println("Withdrew " + amount + " from account " + accountNumber);
            return true;
        }
        return false;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
