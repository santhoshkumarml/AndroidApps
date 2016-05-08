package com.dev.maari.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * @author Santhosh Kumar
 */
public class MaariActivity extends Activity {
  StateInfoManager stateInfoManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maari);
    this.stateInfoManager = new StateInfoManager();
    this.stateInfoManager.initialize();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.maari, menu);
    return true;
  }

  @Override
  protected void onDestroy() {
    this.stateInfoManager.destroy();
    super.onDestroy();
  }
}
