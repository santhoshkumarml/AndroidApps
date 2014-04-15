package com.devel.smsbackuprestore;

public enum MessageType {

    INBOX("content://sms/inbox"),
    FAILED("content://sms/failed"),
    QUEUED("content://sms/queued"),
    SENT("content://sms/sent"),
    DRAFTS("content://sms/draft"),
    OUTBOX("content://sms/outbox"),
    UNDELIVERED("content://sms/undelivered");
    //All = "content://sms/all"
   //Conversations = "content://sms/conversations"

    String URI;
    MessageType(String URI) {
        this.URI = URI;
    }
    public String getURI() {
        return this.URI;
    }

}
