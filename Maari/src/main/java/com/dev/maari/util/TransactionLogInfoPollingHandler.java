package com.dev.maari.util;

import com.dev.maari.model.TransactionLogInfo;

public interface TransactionLogInfoPollingHandler {
  boolean handleTransactionLogPollingAction(TransactionLogInfo logInfo);
}
