package com.dev.maari.util;

import com.dev.maari.model.TransactionLogInfo;

interface  TransactionLogInfoPollingHandler {
  boolean handleTransactionLogPollingAction(TransactionLogInfo logInfo);
}
