package com.dev.maari.util;

import android.app.PendingIntent;
import android.telephony.SmsManager;
import au.com.bytecode.opencsv.CSVReader;
import com.dev.maari.model.ActorInfo;
import com.dev.maari.model.ActorPeriodInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Utility {
  private static final char DELIMITER = '\t';
  private static final String OWNER_CSV_FILE = "owners.csv";
  private static final String AGENT_CSV_FILE = "agents.csv";
  private static final String ADMIN_CSV_FILE = "admin.csv";
  private static final Logger LOG = Logger.getLogger(Utility.class.getCanonicalName());

  private Utility() {}

  private static Map<String, ActorPeriodInfo> readData(String csvFile, ActorInfo.ActorType actorType) {
    Map<String, ActorPeriodInfo> actorPeriodInfos = new HashMap<String, ActorPeriodInfo>();
    CSVReader reader = null;
    try {
      reader = new CSVReader(getReaderForDriveFile(csvFile), DELIMITER);

      // these should be the header fields
      int idPosition = -1;
      String[] header = reader.readNext();
      for (int i = 0; i< header.length; i++) {
        if (header[i].equalsIgnoreCase("id")) {
          idPosition = i;
          break;
        }
      }

      if (idPosition == -1) {
        LOG.warning("No id row present");
        throw new RuntimeException("Cannot find id row");
      }

      String[] fields;
      while ((fields = reader.readNext()) != null) {
        ActorPeriodInfo aP = new ActorPeriodInfo();
        for (int i = 0; i < header.length; i++) {
          ActorPeriodInfo.set(aP, header[i], fields[i]);
        }
        aP.getActorInfo().setActorType(actorType);
        actorPeriodInfos.put(fields[idPosition], aP);
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

  private static Reader getReaderForDriveFile(String csvFileName) {
    //TODO
    return null;
  }

  public static Map<ActorInfo.ActorType, Map<String, ActorPeriodInfo>> initializeAndReadData() {
    Map<ActorInfo.ActorType, Map<String, ActorPeriodInfo>> actorInfos = new HashMap<ActorInfo.ActorType, Map<String, ActorPeriodInfo>>();
    actorInfos.put(ActorInfo.ActorType.ADMIN, readData(ADMIN_CSV_FILE, ActorInfo.ActorType.ADMIN));
    actorInfos.put(ActorInfo.ActorType.AGENT, readData(AGENT_CSV_FILE, ActorInfo.ActorType.AGENT));
    actorInfos.put(ActorInfo.ActorType.OWNER, readData(OWNER_CSV_FILE, ActorInfo.ActorType.OWNER));
    return actorInfos;
  }

  public static void sendSMS(ActorPeriodInfo actorPeriodInfo, PendingIntent si, PendingIntent di) {
    String message = "";
    SmsManager sms = SmsManager.getDefault();
    sms.sendTextMessage(actorPeriodInfo.getActorInfo().getPhoneNo(), null, message, si, di);
  }
}
