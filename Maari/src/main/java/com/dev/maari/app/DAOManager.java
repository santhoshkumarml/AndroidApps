package com.dev.maari.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dev.maari.model.TransactionLogInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

final class DAOManager extends OrmLiteSqliteOpenHelper{
  private static final String DATABASE_NAME = "maari.db";
  private static final int DATABASE_VERSION = 4;
  private static final String LOG_NAME = DAOManager.class.getCanonicalName();

  private Dao<TransactionLogInfo, Long> thingDao;

  public DAOManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, TransactionLogInfo.class);
    } catch (SQLException e) {
      Log.e(LOG_NAME, "Could not create new table for TransactionLogInfo", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void onUpgrade(
      SQLiteDatabase sqLiteDatabase,
      ConnectionSource connectionSource,
      int oldVersion,
      int newVersion
  ) {
//    NOOP
//      TableUtils.dropTable(connectionSource, TransactionLogInfo.class, true);
//      onCreate(sqLiteDatabase, connectionSource);
  }

  public Dao<TransactionLogInfo, Long> getTransactionLogDao() throws SQLException {
    if (thingDao == null) {
      thingDao = getDao(TransactionLogInfo.class);
    }
    return thingDao;
  }
}
