package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FormDetailCommun extends FacebookActivity {
	
	private ImageView imgLogoAcaraCommun, imgMemberCommun, imgCommentCommun, imgLikeCommun, 
		imgFacebookCommun, imgTwitterCommun, imgShareCommun;
	private ImageButton imgBtnViewPhotos, imgBtnBack;
	private Button btnJoin, btnUploadPhoto, btnComment, btnLike, btnSendComment, btnCancelComment;
	private TextView txtNamaCommun, txtJenisCommun, txtDeskripCommun, 
				txtJmlhMemberCommun, txtJmlhCommentCommun, txtJmlhLikeCommun;
	private EditText editAddComment;
	
	public static Drawable logoAcaraCommun;
	public int statusSelected;
	public static String idCommun, idLogoCommun, namaCommun, jenisCommun, deskripCommun, linkURL, jmlhMemberCommun, jmlhCommentCommun, jmlhLikeCommun;
	
	private String pilihanMember[] = {"Join", "View members"};
	private String pilihanLike[] = {"Like this!", "Who like this?"};
	private String pilihanPhoto[] = {"View Photos", "Upload Photo"};
	
	public String name, caption, linkUrl, description, linkPic, linkUrlForShare;
	
	public CustomAdapterCustomDialog customAdapter;
	private ArrayList<Drawable> isiLogoUser;
	private ArrayList<String> isiIdUser, isiNamaUser;
	
	public AlertDialog alertDialog;
	
	private ProgressDialog progressDialog;
	private DownloadTaskJumlahMemberCommunity donlodTask;
	
	
	//tes notifikasi
	private static final int MY_NOTIFICATION_ID=1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	private final String myBlog = "http://android-er.blogspot.com/";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_detail_commun);
		
		imgLogoAcaraCommun = (ImageView)findViewById(R.id.imgDepanCommun);
		txtNamaCommun = (TextView)findViewById(R.id.txtNamaCommun);
		txtJenisCommun = (TextView)findViewById(R.id.txtJenisCommun);
		txtDeskripCommun = (TextView)findViewById(R.id.txtDeskripsiCommun);
		txtJmlhMemberCommun = (TextView)findViewById(R.id.txtViewJumlahMemberDetailCommun);
		txtJmlhCommentCommun = (TextView)findViewById(R.id.txtViewJumlahCommentDetailCommun);
		txtJmlhLikeCommun = (TextView)findViewById(R.id.txtViewJumlahLikeDetailCommun);
		
		//Toast.makeText(getApplicationContext(), linkURL, Toast.LENGTH_LONG).show();
		
		imgMemberCommun = (ImageView)findViewById(R.id.imgViewLogoMemberDetailCommun);
		imgMemberCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//LayoutInflater inflater = (LayoutInflater) FormDetailCommun.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
				
				/*
				AlertDialog.Builder optionMember = new AlertDialog.Builder(FormDetailCommun.this);
				//optionMember.setView(layout);
				optionMember.setTitle("Option");
				optionMember.setItems(pilihanMember, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							
						}
						else if (position == 1) {
							progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
							
							statusSelected = 0;
							String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/community_members/?community_id=" + idCommun;
							
							donlodTask = new DownloadTaskJumlahMemberCommunity();
							donlodTask.execute(URL_Jumlah_Member);
						}
					}
				});
				
				AlertDialog alertMember = optionMember.create();
				alertMember.show();
				*/
				
				progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
				
				statusSelected = 0;
				String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/community_members/?community_id=" + idCommun;
				
				donlodTask = new DownloadTaskJumlahMemberCommunity();
				donlodTask.execute(URL_Jumlah_Member);
			}
		});
		
		imgCommentCommun = (ImageView)findViewById(R.id.imgViewLogoCommentDetailCommun);
		imgCommentCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(), FormViewAllComment.class);
				startActivity(in);
			}
		});
		
		imgLikeCommun = (ImageView)findViewById(R.id.imgViewLogoLikeDetailCommun);
		imgLikeCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				AlertDialog.Builder optionLike = new AlertDialog.Builder(FormDetailCommun.this);
				//optionLike.setView(layout);
				optionLike.setTitle("Option");
				optionLike.setItems(pilihanLike, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
							
							String URL_Like_Community = "http://api.wakuwakuw.com/rest/community_likes";
							String URL_Body = "community_id=" + idCommun + "&user_id=" + Timeline.idUser;
							
							XMLParser parserLike = new XMLParser();
							parserLike.postSomething(URL_Like_Community, URL_Body);
							
							//ini buat ngambil jumlah like
							String URLCommunityStats = "http://api.wakuwakuw.com/rest/community_stats/?community_id=" + idCommun;
							String xmlTotalLikes = parserLike.getXmlFromUrl(URLCommunityStats, Timeline.isiUsername, Timeline.isiPassword);
							Document docTotalLikes = parserLike.getDomElement(xmlTotalLikes);
							NodeList nlTotalLikes = docTotalLikes.getElementsByTagName(DATA);
							if (nlTotalLikes.getLength() != 0) {
								Element elTotalLikes = (Element) nlTotalLikes.item(0);
								
								txtJmlhLikeCommun.setText(parserLike.getValue(elTotalLikes, TOTAL_LIKES));
								
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), "You've liked this community.", Toast.LENGTH_SHORT).show();
							}
							else {
								//isiJmlhMemberCommun.add("0"); isiJmlhCommentCommun.add("0"); isiJmlhLikeCommun.add("0"); 
							}
							
						}
						else if (position == 1) {
							progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
							
							statusSelected = 1;
							String URL_Jumlah_Like = "http://api.wakuwakuw.com/rest/community_likes/?community_id=" + idCommun;
							
							donlodTask = new DownloadTaskJumlahMemberCommunity();
							donlodTask.execute(URL_Jumlah_Like);
						}
					}
				});
				
				AlertDialog alertLike = optionLike.create();
				alertLike.show();
				*/
				
				progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
				
				statusSelected = 1;
				String URL_Jumlah_Like = "http://api.wakuwakuw.com/rest/community_likes/?community_id=" + idCommun;
				
				donlodTask = new DownloadTaskJumlahMemberCommunity();
				donlodTask.execute(URL_Jumlah_Like);
			}
		});
		
		
		imgFacebookCommun = (ImageView)findViewById(R.id.imgViewLogoFacebookCommun);
		imgFacebookCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = "Wakuwakuw - " + namaCommun;
				caption = "https://www.wakuwakuw.com/";
				linkUrl = "https://www.wakuwakuw.com/" + linkURL;
				description = deskripCommun + " Join Wakuwakuw to keep up with " + namaCommun + ".";
				linkPic = "http://wakuwakuw.com/img/community/" + idLogoCommun + "?size=stream";
				
				//method dari FacebookActivity.java buat nge-post ke newsfeed
				setConnection();
		        getID(name, caption, linkUrl, description, linkPic);
			}
		});
		
		imgTwitterCommun = (ImageView)findViewById(R.id.imgViewLogoTwitterCommun);
		imgTwitterCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent intent = findTwitterClient("The status update text");
				if (intent != null) {
					//intent.putExtra(Intent.EXTRA_TEXT, "The status update text");
				    startActivity(Intent.createChooser(intent, "Dialog title text"));
				}
				else {
					Toast.makeText(FormDetailCommun.this, "Can't find your twitter client app.", Toast.LENGTH_SHORT).show();
				}*/
				
				//testing notification
				notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				myNotification = new Notification(R.drawable.icon, "Notification!", System.currentTimeMillis());
				Context context = getApplicationContext();
				String notificationTitle = "Exercise of Notification!";
				String notificationText = "http://android-er.blogspot.com/";
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
				PendingIntent pendingIntent = PendingIntent.getActivity(FormDetailCommun.this, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
					   
				//myNotification.defaults |= Notification.DEFAULT_SOUND;
				myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				
				//Uri path = Uri.parse("android.resource://" + getPackageName() + "/bells.ogg");
				Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bird2);
				myNotification.sound = path;
				
				myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
				notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
				
				//AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				//am.set(AlarmManager.RTC, 1338127200, pendingIntent);
				////////////////////////
			}
		});
		
		imgShareCommun = (ImageView)findViewById(R.id.imgViewLogoShareCommun);
		imgShareCommun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linkUrlForShare = "https://www.wakuwakuw.com/" + linkURL;
				String message = "Hi, this community seems interesting, " + namaCommun + 
					".\n\nJust check this out: " + linkUrlForShare;
				
				Intent intent = new Intent(Intent.ACTION_SEND);
			    intent.setType("text/plain");
			    intent.putExtra(Intent.EXTRA_TEXT, message);
			    startActivity(Intent.createChooser(intent, "Share"));
			}
		});
		
		
		imgBtnBack = (ImageButton)findViewById(R.id.imgButtonBack);
		imgBtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		imgBtnViewPhotos = (ImageButton)findViewById(R.id.imgButtonViewPhotoCommun);
		imgBtnViewPhotos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				AlertDialog.Builder optionPhotos = new AlertDialog.Builder(FormDetailCommun.this);
				//optionPhotos.setView(layout);
				//optionPhotos.setTitle("Option");
				optionPhotos.setItems(pilihanPhoto, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							Intent in = new Intent(FormDetailCommun.this, FormPhotoGallery.class);
							startActivity(in);
						}
						else if (position == 1) {
							
						}
					}
				});
				
				AlertDialog alertPhoto = optionPhotos.create();
				alertPhoto.show();
				*/
				
				Intent in = new Intent(FormDetailCommun.this, FormPhotoGallery.class);
				startActivity(in);
			}
		});
		
		
		//btnComment.getBackground().setColorFilter(Color.parseColor("#FF0089E1"), Mode.MULTIPLY);
		btnJoin = (Button)findViewById(R.id.btnJoinCommun);
		btnJoin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Calendar cal = Calendar.getInstance();              
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("title", "A Test Event from android app");
				intent.putExtra("beginTime", cal.getTimeInMillis());
				intent.putExtra("allDay", true);
				intent.putExtra("rrule", "FREQ=YEARLY");
				intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
				startActivity(intent);*/
				
			    
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before joining this community.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhotoCommun);
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before uploading photo.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnComment = (Button)findViewById(R.id.btnCommentCommun);
		btnComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					AlertDialog.Builder optionAddComment = new AlertDialog.Builder(FormDetailCommun.this);
					LayoutInflater inflater = (LayoutInflater) FormDetailCommun.this.getSystemService(LAYOUT_INFLATER_SERVICE);
					View layout = inflater.inflate(R.layout.popup_add_comment, (ViewGroup) findViewById(R.id.containerAddComment));
					
					editAddComment = (EditText)layout.findViewById(R.id.editAddComment);
					
					btnSendComment = (Button)layout.findViewById(R.id.btnSendComment);
					btnSendComment.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (editAddComment.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert your comment.", Toast.LENGTH_SHORT).show();
							}
							else {
								String URL_Put_Comment = "http://api.wakuwakuw.com/rest/community_comments";
								String URL_Body = "community_id=" + idCommun + "&user_id=" + Timeline.idUser + "&comment=" + 
									editAddComment.getText().toString();
								String URL_All_Comments = "http://api.wakuwakuw.com/rest/community_comments?community_id=" + idCommun + 
									"&order_by=time_created&ascending=false";
								
								XMLParser parser = new XMLParser();
								parser.postSomething(URL_Put_Comment, URL_Body);
								
								String URLStats = "http://api.wakuwakuw.com/rest/community_stats/?community_id=" + idCommun;
								retrieveCommunityStats(URLStats);
								
								alertDialog.dismiss();
								Toast.makeText(getApplicationContext(), "Comment sent successfully.", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					btnCancelComment = (Button)layout.findViewById(R.id.btnCancelSendComment);
					btnCancelComment.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialog.dismiss();
						}
					});
					
					optionAddComment.setView(layout);
					
					alertDialog = optionAddComment.create();
					alertDialog.show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before giving a comment.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnLike = (Button)findViewById(R.id.btnLikeCommun);
		btnLike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					progressDialog = ProgressDialog.show(FormDetailCommun.this, null, "Please Wait!");
					
					String URL_Like_Community = "http://api.wakuwakuw.com/rest/community_likes";
					String URL_Body = "community_id=" + idCommun + "&user_id=" + Timeline.idUser;
					
					XMLParser parserLike = new XMLParser();
					parserLike.postSomething(URL_Like_Community, URL_Body);
					
					String URLStats = "http://api.wakuwakuw.com/rest/community_stats/?community_id=" + idCommun;
					retrieveCommunityStats(URLStats);
					
					progressDialog.dismiss();
					Toast.makeText(getApplicationContext(), "You've liked this community.", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before liking this community.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		imgLogoAcaraCommun.setImageDrawable(logoAcaraCommun);
		txtNamaCommun.setText(namaCommun); txtJenisCommun.setText(jenisCommun); txtDeskripCommun.setText(deskripCommun);
		txtJmlhMemberCommun.setText(jmlhMemberCommun); txtJmlhCommentCommun.setText(jmlhCommentCommun); txtJmlhLikeCommun.setText(jmlhLikeCommun);
		
		//Toast.makeText(this, idCommun, Toast.LENGTH_SHORT).show();		
	}
	
	
	public void retrieveCommunityStats(String URLStats) {
		//ini buat ngambil jumlah member, comment & like		
		XMLParser parser = new XMLParser();
		String xmlStats = parser.getXmlFromUrl(URLStats, Timeline.isiUsername, Timeline.isiPassword);
		Document docStats = parser.getDomElement(xmlStats);
		NodeList nlStats = docStats.getElementsByTagName(DATA);
		if (nlStats.getLength() != 0) {
			Element elStats = (Element) nlStats.item(0);
			
			txtJmlhMemberCommun.setText(parser.getValue(elStats, TOTAL_MEMBERS));
			txtJmlhCommentCommun.setText(parser.getValue(elStats, TOTAL_COMMENTS));
			txtJmlhLikeCommun.setText(parser.getValue(elStats, TOTAL_LIKES));
		}
		else {
			 
		}
		///////////////////////////////////////////////
	}
	
	
	public class DownloadTaskJumlahMemberCommunity extends AsyncTask<String, Void, CustomAdapterCustomDialog> {

		@Override
		protected CustomAdapterCustomDialog doInBackground(String... params) {
			// TODO Auto-generated method stub
			isiIdUser = new ArrayList<String>();
			isiLogoUser = new ArrayList<Drawable>();
			isiNamaUser = new ArrayList<String>();
			
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(params[0], Timeline.isiUsername, Timeline.isiPassword);
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					
					
					String userId = parser.getValue(e, USER_ID);
					//isiIdUser.add(userId);
					
					String URLUser = "http://api.wakuwakuw.com/rest/users/" + userId;
					XMLParser parserUser = new XMLParser();
					String xmlUser = parserUser.getXmlFromUrl(URLUser, Timeline.isiUsername, Timeline.isiPassword);
					Document docUser = parserUser.getDomElement(xmlUser);
					NodeList nlUser = docUser.getElementsByTagName(DATA);
					if (nlUser.getLength() != 0) {
						isiIdUser.add(userId);
						
						Element elUser = (Element) nlUser.item(0);
						isiNamaUser.add(parserUser.getValue(elUser, NAME));
						
						isiLogoUser.add(LoadImageFromWeb("http://wakuwakuw.com/img/user/" + userId + "?size=feed"));
					}
				}
				
				customAdapter = new CustomAdapterCustomDialog(FormDetailCommun.this, isiIdUser, isiLogoUser, isiNamaUser);
			}
			else {
				customAdapter = null;
			}
			
			return customAdapter;
		}
		
		@Override
		protected void onPostExecute(final CustomAdapterCustomDialog result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			LayoutInflater inflater = (LayoutInflater) FormDetailCommun.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
			
			ListView listCustomDialog = (ListView) layout.findViewById(R.id.listViewCustomDialog);
			listCustomDialog.setAdapter(result);
			listCustomDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> ad, View v, int pos, long id) {
					// TODO Auto-generated method stub
					FormUserInfo.statusOpenedFrom = "FormDetailCommun";
					FormUserInfo.additionalParameter = result.theName.get(pos).toString();
					
					Intent in = new Intent(FormDetailCommun.this, FormUserInfo.class);
					startActivity(in);
					
					//Toast.makeText(FormDetailCommun.this, result.theName.get(pos).toString(), Toast.LENGTH_LONG).show();
				}
			});
			
			AlertDialog.Builder optionJmlhMember = new AlertDialog.Builder(FormDetailCommun.this);
			
			if (statusSelected == 0) {
				optionJmlhMember.setTitle("Member of This Group");
			}
			else if (statusSelected == 1) {
				optionJmlhMember.setTitle("People Who Like This");
			}
			
			optionJmlhMember.setView(layout);
			
			AlertDialog alertMember = optionJmlhMember.create();
			alertMember.show();
		}
	}
	
	public Intent findTwitterClient(String updateText) {
		final String[] twitterApps = {
				// package // name - nb installs (thousands)
				"com.twitter.android", // official - 10 000
				"com.twidroid", // twidroyd - 5 000
				"com.handmark.tweetcaster", // Tweecaster - 5 000
				"com.thedeck.android", 
				"com.facebook.katana",
				"com.example.mysqlsimple"}; // TweetDeck - 5 000
		Intent tweetIntent = new Intent(Intent.ACTION_SEND);
		tweetIntent.setType("text/plain");
		tweetIntent.putExtra(Intent.EXTRA_TEXT, updateText);
		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return null;
	}
	
	//cara ngeload image dari URL
	private Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} 
		catch (Exception e) {
		   System.out.println("Exc="+e);
		   return null;
		}
	}
}
