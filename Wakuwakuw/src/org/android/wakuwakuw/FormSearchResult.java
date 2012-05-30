package org.android.wakuwakuw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FormSearchResult extends Activity {
	
	private ListView listResult;
	private TextView txtResult; 
	
	public static String xml;
	public static int statusAdapter, itemFound;
	public static SeparatedListAdapter adapter;
	public static CustomAdapterCommun customAdapterCommun;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_results);
		
		txtResult = (TextView)findViewById(R.id.txtViewResult);
		listResult = (ListView)findViewById(R.id.listViewResult);
		
		//Bundle extras = getIntent().getExtras();
		//int itemFound = extras.getInt("Item Found");
		
		//jika isi xml tidak ada
		if (Search.xml == null) {
			Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
			txtResult.setText("No Connection..");
		}
		else {
			//jika status adapter merupakan 'Event' atau 'Meetup'
			if (statusAdapter == 0 || statusAdapter == 1) {
				//jika adapter tersebut tidak ada isinya
				if (adapter == null) {
					Toast.makeText(getApplicationContext(), "Nothing Was Found..", Toast.LENGTH_LONG).show();
					txtResult.setText("Nothing Was Found..");
				}
				//jika adapter tersebut ada isinya
				else {
					Toast.makeText(getApplicationContext(), "Found..", Toast.LENGTH_LONG).show();
					//txtResult.setText(Integer.toString(Timeline.isiJudul.size()) + " Found..");
					txtResult.setText(Integer.toString(itemFound) + " Found..");
					listResult.setAdapter(adapter);
				}
			}
			//jika status adapter merupakan 'Community'
			else if (statusAdapter == 2) {
				//jika adapter tersebut tidak ada isinya
				if (customAdapterCommun == null) {
					Toast.makeText(getApplicationContext(), "Nothing Was Found..", Toast.LENGTH_LONG).show();
					txtResult.setText("Nothing Was Found..");
				}
				//jika adapter tersebut ada isinya
				else {
					Toast.makeText(getApplicationContext(), "Found..", Toast.LENGTH_LONG).show();
					//txtResult.setText(Integer.toString(Timeline.isiNamaCommun.size()) + " Found..");
					txtResult.setText(Integer.toString(itemFound) + " Found..");
					listResult.setAdapter(customAdapterCommun);
					//Search.customAdapterCommun = null;
				}
			}
		}
	}
}
