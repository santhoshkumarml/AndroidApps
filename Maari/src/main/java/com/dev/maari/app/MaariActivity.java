package com.dev.maari.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.dev.maari.exceptions.MaariException;

/**
 * @author Santhosh Kumar
 */
public class MaariActivity extends Activity {
  AppStateManager appStateManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maari);
    appStateManager = new AppStateManager();
    try {
      appStateManager.initialize(getApplicationContext());
    } catch (MaariException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.maari, menu);
    return true;
  }

  @Override
  protected void onDestroy() {
    appStateManager.destroy();
    super.onDestroy();
  }
}
