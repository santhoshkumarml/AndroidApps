package com.dev.smsbackuprestore.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageSummaryContainer implements Serializable {

    private static final long serialVersionUID = -7225549992045380647L;

    Map<MessageType,List<MessageSummary>> messageTypeToSummaryMap = new HashMap<MessageType, List<MessageSummary>>();

    public Map<MessageType,List<MessageSummary>> getMessageTypeToSummaryMap() {
        return messageTypeToSummaryMap;
    }

    public List<MessageSummary>  getMessageStubs(MessageType type) {
        List<MessageSummary> messageSummaries = getMessageTypeToSummaryMap().get(type);
        if(messageSummaries == null) {
            messageSummaries = new ArrayList<MessageSummary>();
        }
        getMessageTypeToSummaryMap().put(type, messageSummaries);
        return messageSummaries;
    }

    public static void serializeMessages(MessageSummaryContainer messages, String filePath) {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            fileOut = new FileOutputStream(filePath);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(messages);
            SMSBackupRestoreActivity.LOGGER.info("Messages Serialized to file:"+filePath);
        } catch (IOException e) {
            SMSBackupRestoreActivity.LOGGER.severe(e.getMessage());
        }
        finally {
            try {
                if(out != null) {
                    out.close();
                }
                if(fileOut != null) {
                    fileOut.close();
                }

            } catch (Exception e) {
                SMSBackupRestoreActivity.LOGGER.severe(e.getMessage());
            }
        }
    }


    public static MessageSummaryContainer deserializeMessages(File file) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        MessageSummaryContainer messages = new MessageSummaryContainer();
        try {
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            messages = (MessageSummaryContainer) in.readObject();
            SMSBackupRestoreActivity.LOGGER.info("Messages DeSerialized from file:"+file.getAbsolutePath());
        } catch (IOException e) {
            SMSBackupRestoreActivity.LOGGER.severe(e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            SMSBackupRestoreActivity.LOGGER.severe(e.getMessage());
        }
        finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(fileIn != null) {
                    fileIn.close();
                }
            } catch (Exception e) {
                SMSBackupRestoreActivity.LOGGER.severe(e.getMessage());
            }
        }
        return messages;
    }

}
