package com.dev.maari.util;

import com.dev.maari.model.TransactionLogInfo;

abstract class TransactionLogInfoPollingHandler {
  public abstract boolean handleTransactionLogPollingAction(TransactionLogInfo logInfo);
}
