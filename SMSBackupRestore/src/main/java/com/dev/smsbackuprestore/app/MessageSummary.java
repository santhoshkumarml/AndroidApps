package com.dev.smsbackuprestore.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MessageSummary implements Serializable {

    private static final long serialVersionUID = 2376312498560727516L;

    Map<String,String> columnToValueMap;

    public Map<String, String> getColumnToValueMap() {
        if(this.columnToValueMap == null) {
            this.columnToValueMap = new HashMap<String, String>();
        }
        return this.columnToValueMap;
    }

}
