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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.dev.maari.model.TransactionLogInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import android.util.Log;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper{
  private static final String DATABASE_NAME = "maari.db";
  private static final int DATABASE_VERSION = 4;
  private final String LOG_NAME = DBHelper.class.getCanonicalName();

  private Dao<TransactionLogInfo, Long> thingDao;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, TransactionLogInfo.class);
    } catch (SQLException e) {
      Log.e(LOG_NAME, "Could not create new table for Thing", e);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                        int newVersion) {
    //NOOP
//    try {
//      TableUtils.dropTable(connectionSource, TransactionLogInfo.class, true);
//      onCreate(sqLiteDatabase, connectionSource);
//
//    } catch (SQLException e) {
//      Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
//    }
  }

  public Dao<TransactionLogInfo, Long> getTransactionLogDao() throws SQLException {
    if (thingDao == null) {
      thingDao = getDao(TransactionLogInfo.class);
    }
    return thingDao;
  }
}
