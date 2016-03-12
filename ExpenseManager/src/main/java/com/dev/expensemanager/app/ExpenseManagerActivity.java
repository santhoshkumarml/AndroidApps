package com.dev.expensemanager.app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.devel.expensemanager.R;

public class ExpenseManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_manager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_manager, menu);
		return true;
	}

}
