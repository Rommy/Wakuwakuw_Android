package org.android.wakuwakuw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreen extends Activity {
	
	BackgroundThread backgroundThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		backgroundThread = new BackgroundThread();
	    backgroundThread.setRunning(true);
	    backgroundThread.start();
	}
	
	public class BackgroundThread extends Thread {
		volatile boolean running = false;
		int cnt;
		
		public void setRunning(boolean b) {
			this.running = b;
			cnt = 2;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(running){
				try {
					sleep(1000);
				    if(cnt-- == 0){
				    	running = false;
				    }
				   } 
				catch (InterruptedException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			}
			handler.sendMessage(handler.obtainMessage());
		}
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			boolean retry = true;
			while(retry){
			   try {
				   backgroundThread.join();
				   retry = false;
			   } catch (InterruptedException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
			}
			
			finish();
			Intent in = new Intent(getApplicationContext(), Timeline.class);
			startActivity(in); 
		};
	};
}
