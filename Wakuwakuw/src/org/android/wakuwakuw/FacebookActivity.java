package org.android.wakuwakuw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class FacebookActivity extends Activity {
    public static final String TAG = "FACEBOOK";
    private Facebook mFacebook;
    public static final String APP_ID = "220855434611590"; //the API Key for your Facebook APPs
    private AsyncFacebookRunner mAsyncRunner;
    private static final String[] PERMS = new String[] { "publish_stream" };
    private SharedPreferences sharedPrefs;
    private Context mContext;
 
    /*private TextView username;
    private ProgressBar pb;*/
    
    public String name, caption, linkUrl, description, linkPic;
 
    public void setConnection() {
            mContext = this;
            mFacebook = new Facebook(APP_ID);
            mAsyncRunner = new AsyncFacebookRunner(mFacebook);
    }
 
    public void getID(String name, String caption, String linkUrl, String description, String linkPic) {
            //username = txtUserName;
            //pb = progbar;
            
            //sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            //sharedPrefs.edit().clear().commit();
    	
    	this.name = name;
    	this.caption = caption;
    	this.linkUrl = linkUrl;
    	this.description = description;
    	this.linkPic = linkPic;
            
        if (isSession()) {
        	Log.d(TAG, "sessionValid");
            mAsyncRunner.request("me", new IDRequestListener());
        } else {
            //no logged in, so relogin
        	Log.d(TAG, "sessionNOTValid, relogin");
            mFacebook.authorize(this, PERMS, new LoginDialogListener());
        }
    }
 
    public boolean isSession() {
    	
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String access_token = sharedPrefs.getString("access_token", null);
        Long expires = sharedPrefs.getLong("access_expires", -1);
 
        if (access_token != null && expires != -1) {
        	Log.d(TAG, access_token);
        	mFacebook.setAccessToken(access_token);
            mFacebook.setAccessExpires(expires);
        }
        
        return mFacebook.isSessionValid();
    }
    
    public void postInNewsFeed() {
    	Bundle params = new Bundle();
    	/*params.putString("name", name);
    	params.putString("caption", caption);
    	params.putString("link", linkUrl); 
    	params.putString("description", description);
        params.putString("picture", linkPic);
        params.putString("to", "");*/
    	
    	params.putString("name", this.name);
    	params.putString("caption", this.caption);
    	params.putString("link", this.linkUrl); 
    	params.putString("description", this.description);
        params.putString("picture", this.linkPic);
        //params.putString("to", "");
    	mFacebook.dialog(mContext, "feed", params, new DialogListener() {
			
			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				FacebookActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                    	Toast.makeText(FacebookActivity.this, "Succesfully posted to newsfeed", Toast.LENGTH_SHORT).show();
                    }
                });
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
    
    public void logOut() {
    	mAsyncRunner.logout(mContext, new LogOutListener());
    }
 
    private class LoginDialogListener implements DialogListener {
 
            @Override
            public void onComplete(Bundle values) {
                    Log.d(TAG, "LoginONComplete");
                    String token = mFacebook.getAccessToken();
                    long token_expires = mFacebook.getAccessExpires();
                    Log.d(TAG, "AccessToken: " + token);
                    Log.d(TAG, "AccessExpires: " + token_expires);
                    
                    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    sharedPrefs.edit().putLong("access_expires", token_expires).commit();
                    sharedPrefs.edit().putString("access_token", token).commit();
                    mAsyncRunner.request("me", new IDRequestListener());
            }
 
            @Override
            public void onFacebookError(FacebookError e) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }
 
            @Override
            public void onError(DialogError e) {
                    Log.d(TAG, "Error: " + e.getMessage());
            }
 
            @Override
            public void onCancel() {
                    Log.d(TAG, "OnCancel");
            }
    }
 
    private class IDRequestListener implements RequestListener {
 
            @Override
            public void onComplete(String response, Object state) {
                    try {
                    	Log.d(TAG, "IDRequestONComplete");
                        Log.d(TAG, "Response: " + response.toString());
                            
                        JSONObject json = Util.parseJson(response);
                            
                        final String id = json.getString("id");
                        final String name = json.getString("name");
                        
                        FacebookActivity.this.runOnUiThread(new Runnable() {
                        	public void run() {
                        		//username.setText("Welcome: " + name+"\n ID: "+id);
                                //pb.setVisibility(ProgressBar.GONE);
                                
                                postInNewsFeed();
                            }
                        });
                            
                    } catch (JSONException e) {
                    	Log.d(TAG, "JSONException: " + e.getMessage());
                    } catch (FacebookError e) {
                        Log.d(TAG, "FacebookError: " + e.getMessage());
                            
                        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(FacebookActivity.this);
                        sharedPrefs.edit().clear().commit();
                                    
                        FacebookActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                            	mFacebook.setAccessToken(null);
                                mFacebook.setAccessExpires(-1);
                                            
                                //mFacebook.authorize(FacebookActivity.this, PERMS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
                                    
                                mFacebook.authorize(FacebookActivity.this, PERMS, new LoginDialogListener());
                            }
                        });
                    }
            }
 
            @Override
            public void onIOException(IOException e, Object state) {
            	Log.d(TAG, "IOException: " + e.getMessage());
            }
 
            @Override
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
                Log.d(TAG, "FileNotFoundException: " + e.getMessage());
            }
 
            @Override
            public void onMalformedURLException(MalformedURLException e, Object state) {
                Log.d(TAG, "MalformedURLException: " + e.getMessage());
            }
 
            @Override
            public void onFacebookError(FacebookError e, Object state) {
                Log.d(TAG, "FacebookError: " + e.getMessage());
            }
 
    }
    
    private class LogOutListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            sharedPrefs.edit().clear().commit();
            
            FacebookActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                	//username.setText("Log Out");                        
                }
        	});
		}

		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			
		}
    	
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
     
    public void postOnWall(String msg) {
        Log.d("Tests graph API %%%%%$$$$%%%", msg);
         try {
                String response = mFacebook.request("me");
                Bundle parameters = new Bundle();
                parameters.putString("message", msg);
                parameters.putString("description", "test test test");
                response = mFacebook.request("me/feed", parameters, 
                        "POST");
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
                }
         } catch(Exception e) {
             e.printStackTrace();
         }
    }
}