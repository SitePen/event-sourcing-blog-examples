package com.example.esexample.model.commands;

import com.example.esexample.model.entities.BankAccount;
import com.example.esexample.model.events.AccountEvent;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = Command.CommandBuilder.class)
@Builder(builderClassName = "CommandBuilder", toBuilder = true)
public class Command {

   String accountId;
   CommandType commandType;
   Long amount;

   public boolean canProcessForAccount(BankAccount bankAccount) {
       return null != commandType && commandType.canProcessForAccount(accountId, amount, bankAccount);
   }

   public AccountEvent processForAccount(BankAccount bankAccount) {
       return null == commandType ? null : commandType.processForAccount(accountId, amount, bankAccount);
   }

   @JsonPOJOBuilder(withPrefix = "")
   public static class CommandBuilder {}

}
