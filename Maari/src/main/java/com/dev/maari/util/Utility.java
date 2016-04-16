/**
 * Copyright 2016 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dev.maari.util;

import android.telephony.SmsManager;
import au.com.bytecode.opencsv.CSVReader;
import com.dev.maari.model.ActorPeriodInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public final class Utility {
  private static final char DELIMITER = '\t';
  private static final String OWNER_CSV_FILE = "owners.csv";
  private static final String AGENT_CSV_FILE = "agents.csv";
  private static final String ADMIN_CSV_FILE = "admin.csv";
  private static final Logger LOG = Logger.getLogger(Utility.class.getCanonicalName());

  private Utility() {}

  private static Set<ActorPeriodInfo> readData(String csvFile, ActorPeriodInfo.ActorType actorType) {
    Set<ActorPeriodInfo> actorPeriodInfos = new HashSet<ActorPeriodInfo>();
    CSVReader reader = null;
    try {
      reader = new CSVReader(getReaderForDriveFile(csvFile), DELIMITER);
      // these should be the header fields
      String[] header = reader.readNext();
      String[] fields;
      while ((fields = reader.readNext()) != null) {
        ActorPeriodInfo aP = new ActorPeriodInfo();
        for (int i = 0; i < header.length; i++) {
          ActorPeriodInfo.set(aP, header[i], fields[i]);
        }
        aP.getActorInfo().setActorType(actorType);
        actorPeriodInfos.add(aP);
      }
    } catch (IOException  e) {
      LOG.severe(e.getLocalizedMessage());
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          LOG.severe(e.getLocalizedMessage());
        }
      }
    }
    return actorPeriodInfos;
  }

  private static Reader getReaderForDriveFile(String csvFile) {
    return null;
  }

  public static Set<ActorPeriodInfo> readAgentData() {
    return readData(AGENT_CSV_FILE, ActorPeriodInfo.ActorType.AGENT);
  }

  public static Set<ActorPeriodInfo> readOwnerData() {
    return readData(OWNER_CSV_FILE, ActorPeriodInfo.ActorType.OWNER);
  }

  public static Set<ActorPeriodInfo> readAdminData() {
    return readData(ADMIN_CSV_FILE, ActorPeriodInfo.ActorType.ADMIN);
  }

  private void sendSMS(String phoneNumber, String message) {
    SmsManager sms = SmsManager.getDefault();
    sms.sendTextMessage(phoneNumber, null, message, null, null);
  }

}
