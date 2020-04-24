package com.example.esexample.model.events;

import com.example.esexample.model.entities.BankAccount;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = AccountEvent.AccountEventBuilder.class)
@Builder(builderClassName = "AccountEventBuilder", toBuilder = true)
public class AccountEvent {

   String accountId;
   AccountEventType eventType;
   Long amount;
   BankAccount bankAccount;

   @JsonPOJOBuilder(withPrefix = "")
   public static class AccountEventBuilder {}

}
