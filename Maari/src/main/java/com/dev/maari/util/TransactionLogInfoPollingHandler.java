package com.dev.maari.util;

import com.dev.maari.model.TransactionLogInfo;

public abstract class TransactionLogInfoPollingHandler {
  public abstract boolean handleTransactionLogPollingAction(TransactionLogInfo logInfo);
}
