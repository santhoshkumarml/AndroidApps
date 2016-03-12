package com.dev.smsbackuprestore.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

/**
 * @author Santhosh Kumar
 */
public class SMSBackupRestoreActivity extends Activity{

    public final static Logger LOGGER = Logger.getLogger(SMSBackupRestoreActivity.class.getCanonicalName());

    private static final String MESSAGE_FILE_PREFIX = "SMSMessageBackup";
    private static final String MESSAGE_BACKUP_FILE_SUFFIX = ".smsbac";
    private static final String[] fields = {"address","body","read","date"};
    private static String SDCARD_PATH = null;
    private SDCardStateChecker sdcardStateChecker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsbackup_restore);
        sdcardStateChecker = new SDCardStateChecker();
        sdcardStateChecker.start();
        while(!sdcardStateChecker.isAssignedOnce());
        if(sdcardStateChecker.ismExternalStorageAvailable()) {
            SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_smsbackup_restore, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sdcardStateChecker.stop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void backupMessages(View view) {
        if(!(sdcardStateChecker.ismExternalStorageAvailable() && sdcardStateChecker.ismExternalStorageWriteable())) {
            LOGGER.severe("SD card not usable");
            Toast.makeText(this, "SD card not usable", Toast.LENGTH_LONG).show();
            return;
        }
        MessageSummaries messages = new MessageSummaries();
        int messageCount = 0;
        for(MessageType  messageType : MessageType.values()) {
            Uri uri = Uri.parse(messageType.getURI());
            Cursor messageCursor = getContentResolver().query(uri, fields, null, null, null);
            while(messageCursor.moveToNext()) {
                messageCount++;
                MessageSummary messageStub = new MessageSummary();
                for(int i=0;i<messageCursor.getColumnCount();i++) {
                    String columnName = messageCursor.getColumnName(i);
                    messageStub.getColumnToValueMap().put(columnName, messageCursor.getString(i));
                }
                messages.getMessageStubs(messageType).add(messageStub);
            }

        }
        LOGGER.info("Messages found:"+messageCount);
        if(messageCount > 0) {
            AlertDialog.Builder builder = getBackupAlertDialog(messages);
            builder.show();
        } else {
            Toast.makeText(this, "No messages to backup", Toast.LENGTH_LONG).show();
        }
    }

    private String getFileNameForPersistence() {
        return SDCARD_PATH+File.separator+MESSAGE_FILE_PREFIX+
                "_"+System.currentTimeMillis()+MESSAGE_BACKUP_FILE_SUFFIX;
    }


    public void restoreMessages(View view) {
        if(!sdcardStateChecker.ismExternalStorageAvailable()) {
            LOGGER.severe("SD card not usable");
            Toast.makeText(this, "SD card not usable", Toast.LENGTH_LONG).show();
            return;
        }
        File[] files = findMessageBackupFiles();
        if(files.length == 0) {
            Toast.makeText(this, "No Backup files found in external storage", Toast.LENGTH_LONG).show();
            return;
        }

        String[] fileStrings = new String[files.length];
        for(int i=0;i<files.length;i++) {
            fileStrings[i] = new String(files[i].getName());
        }

        AlertDialog.Builder builder = getRestoreAlertDialog(files, fileStrings);
        builder.show();


    }

    private AlertDialog.Builder getRestoreAlertDialog(File[] files,
            String[] fileStrings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Backup File for Restore");

        ChoiceListener choiceListener = new ChoiceListener();
        builder.setSingleChoiceItems(fileStrings, 0, choiceListener);

        builder.setPositiveButton("Ok",new RestoreFileSelectListener(choiceListener, files));
        builder.setNegativeButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }


    private AlertDialog.Builder getBackupAlertDialog(MessageSummaries messages) {
        String filePath = getFileNameForPersistence();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Name for Backup File");

        EditText editText = new EditText(this);
        editText.setText(filePath);
        builder.setView(editText);

        builder.setPositiveButton("Ok", new BackupFileSelectListener(messages, editText, this));
        builder.setNegativeButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }




    /*  private void manipulateButton(View view, boolean enable) {
        Button button1 = (Button)view.findViewById(R.id.button1);
        button1.setClickable(enable);

        Button button2 = (Button)view.findViewById(R.id.button2);
        button2.setClickable(enable);
    }*/


    public static File[] findMessageBackupFiles() {
        File file = new File(SDCARD_PATH);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getName().endsWith(MESSAGE_BACKUP_FILE_SUFFIX)) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    private  class ChoiceListener implements DialogInterface.OnClickListener {

        private Integer choiceIndex = 0;

        public Integer getChoiceIndex() {
            return choiceIndex;
        }

        public void setChoiceIndex(Integer result) {
            this.choiceIndex = result;
        }

        public void onClick(DialogInterface dialog, int item) {
            setChoiceIndex(item);
        }
    }

    private  class RestoreFileSelectListener implements DialogInterface.OnClickListener {

        private ChoiceListener choice;
        File[] files;

        public RestoreFileSelectListener(ChoiceListener choice, File[] files) {
            this.choice = choice;
            this.files = files;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            MessageSummaries messages = MessageSummaries.deserializeMessages(this.files[choice.getChoiceIndex().intValue()]);
            for(MessageType  messageType : messages.getMessageStubs().keySet()) {
                Uri uri = Uri.parse(messageType.getURI());
                ContentValues messageContent = new ContentValues();
                for(MessageSummary messageStub : messages.getMessageStubs(messageType)) {
                    messageContent.clear();
                    for(String field : fields) {
                        messageContent.put(field,messageStub.getColumnToValueMap().get(field));
                    }
                    getContentResolver().insert(uri, messageContent);
                }
            }
            Toast.makeText(getApplicationContext(), "Messages Restored", Toast.LENGTH_LONG).show();
            LOGGER.info("Messages Restored:"+messages.getMessageStubs().size());
            dialog.dismiss();
        }

    }

    private  class BackupFileSelectListener implements DialogInterface.OnClickListener {
        private MessageSummaries messages;
        private EditText editText;
        private Activity activity;


        public BackupFileSelectListener(MessageSummaries messages, EditText editText, Activity activity) {
            this.messages = messages;
            this.editText = editText;
            this.activity = activity;
        }


        @Override
        public void onClick(DialogInterface dialog, int which) {
            Editable fileName = this.editText.getText();
            if(fileName == null || fileName.length() == 0)  {
                Toast.makeText(getApplicationContext(), "File Name Invalid.Give a valid name", Toast.LENGTH_LONG).show();
                return;
            } else {
                File file = new File(fileName.toString());
                if(file.isDirectory()) {
                    Toast.makeText(getApplicationContext(), "File Name Invalid.Give a valid name", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!file.getName().endsWith(MESSAGE_BACKUP_FILE_SUFFIX)) {
                    Toast.makeText(getApplicationContext(),
                            "File Name Invalid.File should end with "+MESSAGE_BACKUP_FILE_SUFFIX+ " suffix"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                if(file.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
                    builder.setTitle("Overwrite Alert");
                    builder.setMessage("Warning: File with name: "+fileName+" already exists."
                            + "Please press Ok to overwrite the file else press Cancel");
                    builder.setPositiveButton("Ok", new AlertListener(messages, fileName.toString()));
                    builder.setNegativeButton("Cancel", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    backupListenerHelper(dialog, fileName.toString(), messages);
                }
            }
            dialog.dismiss();
        }
    }


    private void backupListenerHelper(DialogInterface dialog,
            String fileName, MessageSummaries messages) {
        MessageSummaries.serializeMessages(messages, fileName.toString());
        Toast.makeText(getApplicationContext(), "Messages Backed up in: "+fileName, Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }


    private  class AlertListener implements DialogInterface.OnClickListener {
        private MessageSummaries messages;
        private String fileName;


        public AlertListener(MessageSummaries messages, String fileName) {
            this.messages = messages;
            this.fileName = fileName;
        }


        @Override
        public void onClick(DialogInterface dialog, int which) {
            backupListenerHelper(dialog, fileName, messages);
        }

    }
    private final class SDCardStateChecker extends Thread {
        private boolean mExternalStorageAvailable = false;
        private boolean mExternalStorageWriteable = false;
        private Object monitor = new Object();
        private volatile boolean  assignedOnce = false;


        public boolean ismExternalStorageAvailable() {
            synchronized (monitor) {
                return mExternalStorageAvailable;
            }
        }


        public SDCardStateChecker() {
            super.setName(SDCardStateChecker.class.getCanonicalName());
        }


        public boolean ismExternalStorageWriteable() {
            synchronized (monitor) {
                return mExternalStorageWriteable;
            }
        }

        public boolean isAssignedOnce() {
            return assignedOnce;
        }


        public void run() {
            synchronized (monitor) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    mExternalStorageAvailable = mExternalStorageWriteable = true;
                } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    mExternalStorageAvailable = true;
                    mExternalStorageWriteable = false;
                } else {
                    mExternalStorageAvailable = mExternalStorageWriteable = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                assignedOnce = true;
            }
        }
    }
}
