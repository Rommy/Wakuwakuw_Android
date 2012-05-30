package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FormDetail extends FacebookActivity {
	
	private ScrollView scrl;
	private ImageView imgLogoAcara, imgMember, imgComment, imgLike, imgFacebook, imgTwitter, imgShare;
	private ImageButton imgBtnViewPhotos, imgButtonViewMap, imgButtonViewComment, imgButtonBack;
	private TextView txtNamaAcara, txtWaktuAcara, txtWaktuAkhirAcara, txtTempatAcara, txtJenisAcara, txtDeskripsiAcara,
					txtJmlhMember, txtJmlhComment, txtJmlhLike;
	private Button btnAttend, btnUploadPhoto, btnComment, btnLike, btnLiatPeta, btnSendComment, btnCancelComment;
	private static EditText editAddComment;
	public static Drawable logoAcara;
	public static String idAcara, idLogo, namaAcara, waktuAcara, waktuAkhirAcara, tmptAcara, jenisAcara, 
				deskripAcara, linkURL, koorLatAcara, koorLongAcara, jmlhMember, jmlhComment, jmlhLike, categoryStatus;
	
	private String pilihanGuest[] = {"Attend", "View guests"};
	private String pilihanLike[] = {"Like this!", "Who like this?"};
	public int statusSelected;
	
	public String name, caption, linkUrl, description, linkPic, linkUrlForShare;
	
	public CustomAdapterCustomDialog customAdapter;
	private ArrayList<Drawable> isiLogoUser;
	private ArrayList<String> isiIdUser, isiNamaUser;
	
	public AlertDialog alertDialog;
	
	private ProgressDialog progressDialog;
	private DownloadTaskJumlahMember donlodTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_detail);
		
		scrl = (ScrollView)findViewById(R.id.scrollViewFormDetail);
		imgLogoAcara = (ImageView)findViewById(R.id.imgDepan);
		txtNamaAcara = (TextView)findViewById(R.id.txtNamaAcara);
		txtTempatAcara = (TextView)findViewById(R.id.txtTempatAcara);
		txtWaktuAcara = (TextView)findViewById(R.id.txtWaktuAcara);
		txtWaktuAkhirAcara = (TextView)findViewById(R.id.txtWaktuAkhirAcara);
		txtJenisAcara = (TextView)findViewById(R.id.txtJenisAcara);
		txtDeskripsiAcara = (TextView)findViewById(R.id.txtDeskripsiAcara);
		txtJmlhMember = (TextView)findViewById(R.id.txtViewJumlahMemberDetail);
		txtJmlhComment = (TextView)findViewById(R.id.txtViewJumlahCommentDetail);
		txtJmlhLike = (TextView)findViewById(R.id.txtViewJumlahLikeDetail);
		/*btnComment = (Button)findViewById(R.id.buttonComment);
		btnComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//scrl.scrollTo(0, 0);
			}
		});*/
		
		scrl.scrollTo(0, 0);
		
		
		//ini buat bilangan heksa
		//btnComment.getBackground().setColorFilter(0xFF0089E1, Mode.MULTIPLY);
		//ini buat parse color
		//btnComment.getBackground().setColorFilter(Color.parseColor("#FF0089E1"), Mode.MULTIPLY);
		//ini buat mode argb
        //btnComment.getBackground().setColorFilter(Color.argb(255, 0, 137, 225), Mode.MULTIPLY);
		
		
		imgMember = (ImageView)findViewById(R.id.imgViewLogoMemberDetail);
		imgMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*AlertDialog.Builder optionGuest = new AlertDialog.Builder(FormDetail.this);
				optionGuest.setTitle("Option");
				optionGuest.setItems(pilihanGuest, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							
						}
						else if (position == 1) {
							progressDialog = ProgressDialog.show(FormDetail.this, null, "Please Wait!");
							
							if (categoryStatus.equalsIgnoreCase("event")) {
								statusSelected = 0;
								String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/event_guests/?event_id=" + idAcara;
								
								donlodTask = new DownloadTaskJumlahMember();
								donlodTask.execute(URL_Jumlah_Member);
							}
							else if (categoryStatus.equalsIgnoreCase("meetup")) {
								statusSelected = 0;
								String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/meetup_guests/?meetup_id=" + idAcara;
								
								donlodTask = new DownloadTaskJumlahMember();
								donlodTask.execute(URL_Jumlah_Member);
							}
						}
					}
				});
				
				AlertDialog alertGuest = optionGuest.create();
				alertGuest.show();*/
				
				
				progressDialog = ProgressDialog.show(FormDetail.this, null, "Please Wait!");
				
				if (categoryStatus.equalsIgnoreCase("event")) {
					statusSelected = 0;
					String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/event_guests/?event_id=" + idAcara;
					
					donlodTask = new DownloadTaskJumlahMember();
					donlodTask.execute(URL_Jumlah_Member);
				}
				else if (categoryStatus.equalsIgnoreCase("meetup")) {
					statusSelected = 0;
					String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/meetup_guests/?meetup_id=" + idAcara;
					
					donlodTask = new DownloadTaskJumlahMember();
					donlodTask.execute(URL_Jumlah_Member);
				}
			}
		});
		
		imgComment = (ImageView)findViewById(R.id.imgViewLogoCommentDetail);
		imgComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(), FormViewAllComment.class);
				startActivity(in);
			}
		});
		
		imgLike = (ImageView)findViewById(R.id.imgViewLogoLikeDetail);
		imgLike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				AlertDialog.Builder optionLike = new AlertDialog.Builder(FormDetail.this);
				optionLike.setTitle("Option");
				optionLike.setItems(pilihanLike, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							
						}
						else if (position == 1) {
							progressDialog = ProgressDialog.show(FormDetail.this, null, "Please Wait!");
							
							if (categoryStatus.equalsIgnoreCase("event")) {
								statusSelected = 1;
								String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/event_likes/?event_id=" + idAcara;
								
								donlodTask = new DownloadTaskJumlahMember();
								donlodTask.execute(URL_Jumlah_Member);
							}
							else if (categoryStatus.equalsIgnoreCase("meetup")) {
								statusSelected = 1;
								String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/meetup_likes/?meetup_id=" + idAcara;
								
								donlodTask = new DownloadTaskJumlahMember();
								donlodTask.execute(URL_Jumlah_Member);
							}
						}
					}
				});
				
				AlertDialog alertLike = optionLike.create();
				alertLike.show();
				*/
				
				progressDialog = ProgressDialog.show(FormDetail.this, null, "Please Wait!");
				
				if (categoryStatus.equalsIgnoreCase("event")) {
					statusSelected = 1;
					String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/event_likes/?event_id=" + idAcara;
					
					donlodTask = new DownloadTaskJumlahMember();
					donlodTask.execute(URL_Jumlah_Member);
				}
				else if (categoryStatus.equalsIgnoreCase("meetup")) {
					statusSelected = 1;
					String URL_Jumlah_Member = "http://api.wakuwakuw.com/rest/meetup_likes/?meetup_id=" + idAcara;
					
					donlodTask = new DownloadTaskJumlahMember();
					donlodTask.execute(URL_Jumlah_Member);
				}
			}
		});
		
		
		imgFacebook = (ImageView)findViewById(R.id.imgViewLogoFacebook);
		imgFacebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = "Wakuwakuw - " + namaAcara;
				caption = "https://www.wakuwakuw.com/";
				
				if (categoryStatus.equalsIgnoreCase("event")) {
					linkUrl = "https://www.wakuwakuw.com/event/" + linkURL;
					linkPic = "http://wakuwakuw.com/img/event/" + idLogo + "?size=stream";
				}
				else if (categoryStatus.equalsIgnoreCase("meetup")) {
					linkUrl = "https://www.wakuwakuw.com/meetup/" + linkURL;
					linkPic = "http://wakuwakuw.com/img/community/" + idLogo + "?size=stream";
				}
				
				description = deskripAcara + " Join Wakuwakuw to attend " + namaAcara + ".";
				
				//method dari FacebookActivity.java buat nge-post ke newsfeed
				setConnection();
		        getID(name, caption, linkUrl, description, linkPic);
			}
		});
		
		imgTwitter = (ImageView)findViewById(R.id.imgViewLogoTwitter);
		imgTwitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		imgShare = (ImageView)findViewById(R.id.imgViewLogoShare);
		imgShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (categoryStatus.equalsIgnoreCase("event")) {
					linkUrlForShare = "https://www.wakuwakuw.com/event/" + linkURL;
				}
				else if (categoryStatus.equalsIgnoreCase("meetup")) {
					linkUrlForShare = "https://www.wakuwakuw.com/meetup/" + linkURL;
				}
				
				String message = "Hi, I'm attending " + namaAcara + ".\n\nJust check this out: " + linkUrlForShare;
				
				Intent intent = new Intent(Intent.ACTION_SEND);
			    intent.setType("text/plain");
			    intent.putExtra(Intent.EXTRA_TEXT, message);
			    startActivity(Intent.createChooser(intent, "Share"));
			}
		});
		
		
		imgButtonViewMap = (ImageButton)findViewById(R.id.imgButtonViewMap);
		imgButtonViewMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (koorLatAcara.equalsIgnoreCase("") || koorLongAcara.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "The Location Is Not Available Yet..", Toast.LENGTH_LONG).show();
				}
				else {
					if (Float.parseFloat(koorLatAcara) == 0 || Float.parseFloat(koorLongAcara) == 0) {
						Toast.makeText(getApplicationContext(), "The Location Is Not Available Yet..", Toast.LENGTH_LONG).show();
					}
					else {
						//tampilkan petanya
						Intent in = new Intent(getApplicationContext(), PetaLokasi.class);
						startActivity(in);
					}
				}
			}
		});
		
		imgBtnViewPhotos = (ImageButton)findViewById(R.id.imgButtonViewPhoto);
		imgBtnViewPhotos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(FormDetail.this, FormPhotoGallery.class);
				startActivity(in);
			}
		});
		
		
		imgButtonBack = (ImageButton)findViewById(R.id.imgButtonBack);
		imgButtonBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		btnAttend = (Button)findViewById(R.id.btnAttend);
		btnAttend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before attending this " + categoryStatus, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhoto);
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
		
		btnComment = (Button)findViewById(R.id.btnComment);
		btnComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					AlertDialog.Builder optionAddComment = new AlertDialog.Builder(FormDetail.this);
					LayoutInflater inflater = (LayoutInflater) FormDetail.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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
								if (categoryStatus.equalsIgnoreCase("event")) {
									String URL_Put_Comment = "http://api.wakuwakuw.com/rest/event_comments";
									String URL_Body = "event_id=" + idAcara + "&user_id=" + Timeline.idUser + "&comment=" + 
										editAddComment.getText().toString();
									String URL_All_Comments = "http://api.wakuwakuw.com/rest/event_comments?event_id=" + idAcara + 
										"&order_by=time_created&ascending=false";
									
									XMLParser parser = new XMLParser();
									parser.postSomething(URL_Put_Comment, URL_Body);
									
									String URLStats = "http://api.wakuwakuw.com/rest/event_stats/?event_id=" + idAcara;
									retrieveEventOrMeetupStats(URLStats);
								}
								else if (categoryStatus.equalsIgnoreCase("meetup")) {
									String URL_Put_Comment = "http://api.wakuwakuw.com/rest/meetup_comments";
									String URL_Body = "meetup_id=" + idAcara + "&user_id=" + Timeline.idUser + "&comment=" + 
										editAddComment.getText().toString();
									String URL_All_Comments = "http://api.wakuwakuw.com/rest/meetup_comments?meetup_id=" + idAcara + 
										"&order_by=time_created&ascending=false";
									
									XMLParser parser = new XMLParser();
									parser.postSomething(URL_Put_Comment, URL_Body);
									
									String URLStats = "http://api.wakuwakuw.com/rest/meetup_stats/?meetup_id=" + idAcara;
									retrieveEventOrMeetupStats(URLStats);
								}
								
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
					
					/*
					optionAddComment.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (editAddComment.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert your comment.", Toast.LENGTH_SHORT).show();
							}
							else {
								
							}
						}
					});
					
					optionAddComment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					*/
					//optionAddComment.show();
					
					alertDialog = optionAddComment.create();
					alertDialog.show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before giving a comment.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnLike = (Button)findViewById(R.id.btnLike);
		btnLike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
					progressDialog = ProgressDialog.show(FormDetail.this, null, "Please Wait!");
					
					if (categoryStatus.equals("event")) {
						String URL_Like_Event = "http://api.wakuwakuw.com/rest/event_likes";
						String URL_Body = "event_id=" + idAcara + "&user_id=" + Timeline.idUser;
						
						XMLParser parserLike = new XMLParser();
						parserLike.postSomething(URL_Like_Event, URL_Body);
						
						String URLStats = "http://api.wakuwakuw.com/rest/event_stats/?event_id=" + idAcara;
						retrieveEventOrMeetupStats(URLStats);
						
						progressDialog.dismiss();
						Toast.makeText(getApplicationContext(), "You've liked this event.", Toast.LENGTH_SHORT).show();
					}
					else if (categoryStatus.equals("meetup")) {
						String URL_Like_Meetup = "http://api.wakuwakuw.com/rest/meetup_likes";
						String URL_Body = "meetup_id=" + idAcara + "&user_id=" + Timeline.idUser;
						
						XMLParser parserLike = new XMLParser();
						parserLike.postSomething(URL_Like_Meetup, URL_Body);
						
						String URLStats = "http://api.wakuwakuw.com/rest/meetup_stats/?meetup_id=" + idAcara;
						retrieveEventOrMeetupStats(URLStats);
						
						progressDialog.dismiss();
						Toast.makeText(getApplicationContext(), "You've liked this meetup.", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "Please log in first before liking this " + categoryStatus, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		
		SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
		String namaHari = outFormat.format(Date.parse(waktuAcara));
		String namaHariAkhir = outFormat.format(Date.parse(waktuAkhirAcara));
		
		imgLogoAcara.setImageDrawable(logoAcara);
		txtNamaAcara.setText(namaAcara); txtTempatAcara.setText(tmptAcara);
		txtWaktuAcara.setText(namaHari + ", " + waktuAcara); 
		txtWaktuAkhirAcara.setText(namaHariAkhir + ", " + waktuAkhirAcara);
		txtJenisAcara.setText(jenisAcara);
		txtDeskripsiAcara.setText(deskripAcara);
		txtJmlhMember.setText(jmlhMember);
		txtJmlhComment.setText(jmlhComment);
		txtJmlhLike.setText(jmlhLike);
		
		
		//Toast.makeText(getApplicationContext(), "Koor Lat: " + koorLatAcara + "\nKoor Long: " + koorLongAcara, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), idAcara, Toast.LENGTH_LONG).show();
	}
	
	
	public void retrieveEventOrMeetupStats(String URLStats) {
		//ini buat ngambil jumlah member, comment & like		
		XMLParser parser = new XMLParser();
		String xmlStats = parser.getXmlFromUrl(URLStats, Timeline.isiUsername, Timeline.isiPassword);
		Document docStats = parser.getDomElement(xmlStats);
		NodeList nlStats = docStats.getElementsByTagName(DATA);
		if (nlStats.getLength() != 0) {
			Element elStats = (Element) nlStats.item(0);
			
			txtJmlhMember.setText(parser.getValue(elStats, TOTAL_GUESTS));
			txtJmlhComment.setText(parser.getValue(elStats, TOTAL_COMMENTS));
			txtJmlhLike.setText(parser.getValue(elStats, TOTAL_LIKES));
		}
		else {
			 
		}
		///////////////////////////////////////////////
	}
	
	
	public class DownloadTaskJumlahMember extends AsyncTask<String, Void, CustomAdapterCustomDialog> {

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
				
				customAdapter = new CustomAdapterCustomDialog(FormDetail.this, isiIdUser, isiLogoUser, isiNamaUser);
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
			
			LayoutInflater inflater = (LayoutInflater) FormDetail.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
			
			ListView listCustomDialog = (ListView) layout.findViewById(R.id.listViewCustomDialog);
			listCustomDialog.setAdapter(result);
			listCustomDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> ad, View v, int pos, long id) {
					// TODO Auto-generated method stub
					FormUserInfo.statusOpenedFrom = "FormDetail";
					FormUserInfo.additionalParameter = result.theName.get(pos).toString();
					
					Intent in = new Intent(FormDetail.this, FormUserInfo.class);
					startActivity(in);					
				}
			});
			
			AlertDialog.Builder optionJmlhMember = new AlertDialog.Builder(FormDetail.this);
			
			if (statusSelected == 0) {
				optionJmlhMember.setTitle("Guests");
			}
			else if (statusSelected == 1) {
				optionJmlhMember.setTitle("People Who Like This");
			}
			
			optionJmlhMember.setView(layout);
			
			AlertDialog alertMember = optionJmlhMember.create();
			alertMember.show();
		}
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
