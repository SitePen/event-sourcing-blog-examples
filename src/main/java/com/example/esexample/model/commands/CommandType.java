package com.example.esexample.model.commands;

import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.model.events.AccountEvent;
import com.example.esexample.model.events.AccountEventType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CommandType {

   DEPOSIT {
       @Override
       protected AccountEvent processForAccount(String accountId, Long amount, BankAccount bankAccount) {
           validateAccountPreconditions(accountId, amount, bankAccount);

           log.info("Processing {} (amount: {}) for account {}", this, amount, bankAccount);

           final BankAccount newAccountState = BankAccount.builder().accountId(bankAccount.getAccountId())
                   .balance(bankAccount.getBalance() + amount).build();

           return AccountEvent.builder().accountId(bankAccount.getAccountId())
                   .eventType(AccountEventType.ACCOUNT_CREDITED).amount(amount).bankAccount(newAccountState).build();
       }

       @Override
       protected void validateAccountPreconditions(String accountId, Long amount, BankAccount bankAccount) {
           if (!accountId.equals(bankAccount.getAccountId())) {
               throw new IllegalStateException("Command request is not for the specified account");
           }
       }
   }, WITHDRAW {
       @Override
       protected AccountEvent processForAccount(String accountId, Long amount, BankAccount bankAccount) {
           validateAccountPreconditions(accountId, amount, bankAccount);

           log.info("Processing {} (amount: {}) for account {}", this, amount, bankAccount);

           final BankAccount newAccountState = BankAccount.builder().accountId(bankAccount.getAccountId())
                   .balance(bankAccount.getBalance() - amount).build();

           return AccountEvent.builder().accountId(bankAccount.getAccountId())
                   .eventType(AccountEventType.ACCOUNT_DEBITED).amount(amount).bankAccount(newAccountState).build();
       }

       @Override
       protected void validateAccountPreconditions(String accountId, Long amount, BankAccount bankAccount) {
           if (!accountId.equals(bankAccount.getAccountId())) {
               throw new IllegalStateException("Withdrawal request is not for the specified account");
           }

           if (bankAccount.getBalance() < amount) {
               throw new IllegalStateException("Insufficient funds to process withdrawal request");
           }
       }

   };

   abstract protected AccountEvent processForAccount(String accountId, Long amount, BankAccount bankAccount);

   abstract protected void validateAccountPreconditions(String accountId, Long amount, BankAccount bankAccount);

   boolean canProcessForAccount(String accountId, Long amount, BankAccount bankAccount) {
       try {
           validateAccountPreconditions(accountId, amount, bankAccount);
           return true;
       } catch (IllegalStateException e) {
           return false;
       }
   }

}
