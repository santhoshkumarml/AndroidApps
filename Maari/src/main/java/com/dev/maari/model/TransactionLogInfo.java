
package com.dev.maari.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Time;

@DatabaseTable(tableName = "AgentTransactionLog")
public class TransactionLogInfo implements Serializable{
  @DatabaseField(id = true, generatedId = true)
  long transactionLogId=0;

  @DatabaseField
  long amount;

  @DatabaseField
  String actorId;

  @DatabaseField
  Time time;

  public long getTransactionLogId() {
    return transactionLogId;
  }

  public long getAmount() {
    return amount;
  }

  public String getActorId() {
    return actorId;
  }

  public Time getTime() {
    return time;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }

  public void setTime(Time time) {
    this.time = time;
  }
}
