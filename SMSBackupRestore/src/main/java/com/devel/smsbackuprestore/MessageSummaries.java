package com.devel.smsbackuprestore;

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


public class MessageSummaries implements Serializable {

    private static final long serialVersionUID = -7225549992045380647L;

    Map<MessageType,List<MessageSummary>> messageStubs= null;

    public Map<MessageType,List<MessageSummary>> getMessageStubs() {
        if(messageStubs == null) {
            this.messageStubs =  new HashMap<MessageType, List<MessageSummary>>();
        }
        return messageStubs;
    }

    public List<MessageSummary>  getMessageStubs(MessageType type) {
        List<MessageSummary> messageStubs = getMessageStubs().get(type);
        if(messageStubs == null) {
            messageStubs = new ArrayList<MessageSummary>();
        }
        getMessageStubs().put(type, messageStubs);
        return messageStubs;
    }

    public static void serializeMessages(MessageSummaries messages, String filePath) {
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


    public static MessageSummaries deserializeMessages(File file) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        MessageSummaries messages = new MessageSummaries();
        try {
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            messages = (MessageSummaries) in.readObject();
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
