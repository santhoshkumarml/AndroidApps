package com.dev.maari.util;

import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;
import com.dev.maari.model.ActorInfo;
import com.dev.maari.model.ActorPeriodInfo;
import com.dev.maari.model.MaariException;
import com.dev.maari.model.TransactionLogInfo;

import java.io.IOException;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public final class Utility {
  private static final char DELIMITER = '\t';
  private static final String OWNER_CSV_FILE = "owners.csv";
  private static final String AGENT_CSV_FILE = "agents.csv";
  private static final String ADMIN_CSV_FILE = "admin.csv";
  private static final String LOG_TAG = Utility.class.getCanonicalName();

  private Utility() {}

  private static Map<String, ActorInfo> readData(String csvFile, ActorInfo.ActorType actorType) throws MaariException{
    Map<String, ActorInfo> actorPeriodInfos = new HashMap<String, ActorInfo>();
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
        Log.w(LOG_TAG, "No id row present");
        throw new RuntimeException("Cannot find id row");
      }

      String[] fields;
      while ((fields = reader.readNext()) != null) {
        ActorInfo aP = actorType == ActorInfo.ActorType.OWNER? new ActorPeriodInfo(): new ActorInfo();
        for (int i = 0; i < header.length; i++) {
          if (header[i].equalsIgnoreCase("id")) {
            fields[i] = actorType.name() + "_" + fields[i];
          }
          populateActorInfo(aP, header[i], fields[i]);
        }
        aP.setActorType(actorType);
        actorPeriodInfos.put(fields[idPosition], aP);
      }

    } catch (IOException  e) {
      Log.e(LOG_TAG, e.getLocalizedMessage());
      throw new MaariException(e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          Log.w(LOG_TAG, e.getLocalizedMessage());
        }
      }
    }
    return actorPeriodInfos;
  }

  public static void populateActorInfo(ActorInfo aP, String fieldName, Object val) {
    if (fieldName.equalsIgnoreCase("name")) {
      aP.setName((String) val);
    } else if (fieldName.equalsIgnoreCase("phoneno")) {
      aP.setPhoneNo((String)val);
    } else if (fieldName.equalsIgnoreCase("emailid")) {
      aP.setEmailId((String)val);
    } else if (fieldName.equalsIgnoreCase("org")) {
      aP.setOrg((String)val);
    } else if (fieldName.equalsIgnoreCase("username")) {
      aP.getAuthInfo().setUserName((String)val);
    } else if (fieldName.equalsIgnoreCase("password")) {
      aP.getAuthInfo().setPassword((String)val);
    } else if (fieldName.equalsIgnoreCase("weekday")) {
      ((ActorPeriodInfo)aP).setWeekday(ActorPeriodInfo.Weekday.valueOf((String)val));
    }
  }

  private static void accessPubliclySharedFolderUrl() throws GeneralSecurityException{
//    Drive drive = new Drive.Builder(
//        new NetHttpTransport.Builder()
//            .doNotValidateCertificate()
//            .build(),
//        JacksonFactory.getDefaultInstance(),
//        null
//    ).setApplicationName("Maari")
//        .setRootUrl("https://drive.google.com/folderview?id=0B9heMWveip8qRnlNcTZoekVQLVE&usp=sharing")
//        .build();
  }
  private static Reader getReaderForDriveFile(String folderUrl) {
    try {

    } catch (Exception e) {

    }
    return null;
  }

  public static Map<ActorInfo.ActorType, Map<String, ActorInfo>> initializeAndReadData() throws MaariException{
    Map<ActorInfo.ActorType, Map<String, ActorInfo>> actorInfos =
        new HashMap<ActorInfo.ActorType, Map<String, ActorInfo>>();
    actorInfos.put(ActorInfo.ActorType.ADMIN, readData(ADMIN_CSV_FILE, ActorInfo.ActorType.ADMIN));
    actorInfos.put(ActorInfo.ActorType.AGENT, readData(AGENT_CSV_FILE, ActorInfo.ActorType.AGENT));
    actorInfos.put(ActorInfo.ActorType.OWNER, readData(OWNER_CSV_FILE, ActorInfo.ActorType.OWNER));
    return actorInfos;
  }

  public static String constructMessage(
      TransactionLogInfo logInfo,
      ActorInfo ownerInfo,
      ActorInfo adminInfo,
      ActorInfo agentInfo
  ) {
    StringBuilder sb = new StringBuilder();
    sb.append("Agent: ");
    sb.append(agentInfo.getName());
    sb.append(" received Sum of Rs.");
    sb.append(logInfo.getAmount());
    sb.append(" on behalf of Distributor: ");
    sb.append(adminInfo.getName());
    sb.append(" at ");
    sb.append(logInfo.getTime().toString());
    sb.append(" from ");
    sb.append(ownerInfo.getName());
    return sb.toString();
  }

  public static void sendSMS(
      TransactionLogInfo logInfo,
      ActorInfo ownerInfo,
      ActorInfo adminInfo,
      ActorInfo agentInfo,
      PendingIntent si,
      PendingIntent di
  ) {
    String message = constructMessage(logInfo, ownerInfo, adminInfo, agentInfo);
    SmsManager sms = SmsManager.getDefault();
    sms.sendTextMessage(ownerInfo.getPhoneNo(), null, message, si, di);
  }
}
