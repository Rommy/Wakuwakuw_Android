package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Search extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private Button btnSearch, btnClose;
	private Spinner spnKriteriaCari;
	private String kataSpinner, eventId, meetupId, communityId;
	private EditText editSearch;
	
	public CustomAdapter customAdapter;
	//public static CustomAdapterCommun customAdapterCommun;
	//public static SeparatedListAdapter adapter;
	
	public final String DATUM = "datum";
	public final String DATA = "data";
	public int x;
	//public static int statusAdapter;
	private Date waktuSkrng;
	private long bilWaktuSkrng;
	private String Url_Event, Url_Meetup, Url_Commun;
	
	public static String xml;
	
	public ArrayList<Drawable> isiLogo = null;
	public ArrayList<String> isiId, isiIdLogo, isiJudul, isiPenjelasan, isiAlamat, isiTanggalStart, isiTanggalEnd, 
						isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMember, isiJmlhComment, isiJmlhLike;
	
	//ini buat Communities
	public ArrayList<Drawable> isiLogoCommun;
	public ArrayList<String> isiIdCommun, isiIdLogoCommun, isiNamaCommun, isiJenisCommun, isiDeskripsiCommun,
			isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun;
	
	public ArrayList<Drawable> isiLogoKump;
	public ArrayList<String> isiIdKump, isiIdLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
			isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump,
			isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump;
	
	public SimpleDateFormat outFormat;
	public String tanggalSama;
	public String eventCategoryID;
	public String communityCategoryID;
	
	BackgroundThread backgroundThread;
	ProgressDialog progressDialog;
	
	private DateArranger dateArranger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		dateArranger = new DateArranger();
		
		btnSearch = (Button)findViewById(R.id.buttonSearch);
		btnSearch.setOnClickListener(this);
		
		btnClose = (Button)findViewById(R.id.buttonClose);
		btnClose.setOnClickListener(this);
		
		editSearch = (EditText)findViewById(R.id.editSearch);
		
		spnKriteriaCari = (Spinner)findViewById(R.id.spnKriteriaCari);
		spnKriteriaCari.setOnItemSelectedListener(this);
		//kataSpinner = spnKriteriaCari.getSelectedItem().toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.buttonSearch) {
			/*if (editSearch.getText().toString().equals("")) {
				//Toast.makeText(getApplicationContext(), "Input the search criteria first..", Toast.LENGTH_LONG).show();
			}*/
			
			if (Timeline.cityId == null || Timeline.cityId.equals("")) {
				Toast.makeText(getApplicationContext(), "Please decide your city, either automatically by GPS or manually by pressing the Change City button..", Toast.LENGTH_LONG).show();
			}
			else {
				// Check if the thread is already running
				backgroundThread = (BackgroundThread) getLastNonConfigurationInstance();
				if (backgroundThread != null && backgroundThread.isAlive()) {
					progressDialog = ProgressDialog.show(this, "Searching", "Please Wait!");
					progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								progressDialog.cancel();
								
								backgroundThread.interrupt();
								backgroundThread.stop();
								backgroundThread.setRunning(false);
								//handler.removeCallbacksAndMessages(null);
							}
							return false;
						}
					});
				}
				else {
					progressDialog = ProgressDialog.show(this, "Searching", "Please Wait!");
					progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								progressDialog.cancel();
								
								backgroundThread.interrupt();
								backgroundThread.stop();
								backgroundThread.setRunning(false);
								//handler.removeCallbacksAndMessages(null);
							}
							return false;
						}
					});
					
					backgroundThread = new BackgroundThread();
				    backgroundThread.setRunning(true);
				    backgroundThread.start();
				}
			}
		}
		else if (v.getId() == R.id.buttonClose) {
			finish();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> ad, View v, int pos, long id) {
		// TODO Auto-generated method stub
		//Object o = ad.getAdapter().getItem(pos);
		//kataSpinner = o.toString();
		if (pos == 0) {
			editSearch.setHint("Event Name..");
		}
		else if (pos == 1) {
			editSearch.setHint("Meetup Name..");
		}
		else {
			editSearch.setHint("Community Name..");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> ad) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		//return super.onRetainNonConfigurationInstance();
		return backgroundThread;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}
	
	
	//Background Thread untuk membantu proses 'Search'
	public class BackgroundThread extends Thread {
		volatile boolean running = false;
		int cnt;
		
		public void setRunning(boolean b) {
			this.running = b;
			cnt = 0;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(running){
				try {
					//intinya, event yg mau dilakukan ditaruh disini
					
					//mulai disini, pindah actionnya ke BackgroundThread
					////////////////////////////////////////////////////
					switch (spnKriteriaCari.getSelectedItemPosition()) {
					case 0:
						FormSearchResult.statusAdapter = 0;
						retrieveItem("event");
						
						break;
					case 1:
						FormSearchResult.statusAdapter = 1;
						retrieveItem("meetup");
						
						break;
					case 2:
						//buat menentukan adapter mana yg akan dipakai
						FormSearchResult.statusAdapter = 2;
						
						if (editSearch.getText().toString().equals("")) {
							Url_Commun = "http://api.wakuwakuw.com/rest/communities/?is_free=1";
						}
						else {
							Url_Commun = "http://api.wakuwakuw.com/rest/communities/?name=" + 
							URLEncoder.encode(editSearch.getText().toString());
						}
						
						
						XMLParser parser = new XMLParser();
						xml = parser.getXmlFromUrl(Url_Commun, Timeline.isiUsername, Timeline.isiPassword);
						
						if (xml != null) {
							isiIdCommun = new ArrayList<String>();
							isiLogoCommun = new ArrayList<Drawable>();
							isiNamaCommun = new ArrayList<String>();
							isiJenisCommun = new ArrayList<String>();
							isiDeskripsiCommun = new ArrayList<String>();
							isiLinkURLCommun = new ArrayList<String>();
							
							isiJmlhMemberCommun = new ArrayList<String>();
							isiJmlhCommentCommun = new ArrayList<String>();
							isiJmlhLikeCommun = new ArrayList<String>();
							
							Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
							NodeList nl = doc.getElementsByTagName(DATUM);
							
							if (nl.getLength() != 0) {
								for (int i = 0; i < nl.getLength(); i++) {
									Element e = (Element) nl.item(i);
									// adding each child node to HashMap key => value
									communityId = parser.getValue(e, ID);
									isiIdCommun.add(communityId);
									isiLogoCommun.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + communityId + "?size=stream"));
									
									isiNamaCommun.add(parser.getValue(e, NAME_COMMUN));
									
									//ini buat ngambil nama kategori (untuk community)
									communityCategoryID = parser.getValue(e, COMMUNITY_CATEGORY_ID);
									String URLCommunityCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + communityCategoryID;
									XMLParser parser2 = new XMLParser();
									String xmlEventCategory = parser2.getXmlFromUrl(URLCommunityCategoryID, Timeline.isiUsername, Timeline.isiPassword);
									Document doc2 = parser2.getDomElement(xmlEventCategory);
									NodeList nl2 = doc2.getElementsByTagName(DATA);
									Element el = (Element) nl2.item(0);
									isiJenisCommun.add(parser2.getValue(el, CATEGORY_NAME));
									///////////////////////////////////////////////
									
									//ini buat ngambil jumlah member, comment & like
									String URLCommunityStats = "http://api.wakuwakuw.com/rest/community_stats/?community_id=" + communityId;
									XMLParser parserCommunStats = new XMLParser();
									String xmlCommunStats = parserCommunStats.getXmlFromUrl(URLCommunityStats, Timeline.isiUsername, Timeline.isiPassword);
									Document docCommunStats = parserCommunStats.getDomElement(xmlCommunStats);
									NodeList nlCommunStats = docCommunStats.getElementsByTagName(DATA);
									if (nlCommunStats.getLength() != 0) {
										Element elCommunStats = (Element) nlCommunStats.item(0);
										
										isiJmlhMemberCommun.add(parserCommunStats.getValue(elCommunStats, TOTAL_MEMBERS));
										isiJmlhCommentCommun.add(parserCommunStats.getValue(elCommunStats, TOTAL_COMMENTS));
										isiJmlhLikeCommun.add(parserCommunStats.getValue(elCommunStats, TOTAL_LIKES));
									}
									else {
										isiJmlhMemberCommun.add("0"); isiJmlhCommentCommun.add("0"); isiJmlhLikeCommun.add("0");
									}
									///////////////////////////////////////////////
									
									isiDeskripsiCommun.add(parser.getValue(e, DESCRIP_COMMUN));
									isiLinkURLCommun.add(parser.getValue(e, NAME_URI));
								}
								
								FormSearchResult.customAdapterCommun = new CustomAdapterCommun(Search.this, isiIdCommun, isiLogoCommun, isiNamaCommun, 
										isiJenisCommun, isiDeskripsiCommun, isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun);
								
								FormSearchResult.itemFound = isiNamaCommun.size();
							}
							else {
								FormSearchResult.customAdapterCommun = null;
								Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_LONG).show();
							}
						}
						else {
							Toast.makeText(getApplicationContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
						}
						break;
					default:
						break;
					}
					//sampai disini
					
					sleep(100);
					////////////////////////////////////////////////
					
				    if(cnt-- == 0){
				    	running = false;
				    	handler.sendMessage(handler.obtainMessage());
				    }
				} 
				catch (InterruptedException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			}
			//handler.sendMessage(handler.obtainMessage());
		}
	}
	
	//saat Background Thread selesai menjalankan tugasnya
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
			
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
			Intent in = new Intent(getApplicationContext(), FormSearchResult.class);
			startActivity(in);
		};
	};
	
	
	//khusus untuk penggunaan event dan meetup
	public void retrieveItem(String status) {
		String UrlTarget = null, UrlCategoryID = null, UrlStats = null, categoryId = null; 
		
		//ini buat ngambil tanggal sekarang
		waktuSkrng = new Date();
		bilWaktuSkrng = waktuSkrng.getTime() / 1000;
		///////////////////////////////////
		
		//%3C = kurang dari (<) sedangkan %3E = lebih dari (>)
		//masih menggunakan basis time_start
		/*String Url_Event = "http://api.wakuwakuw.com/rest/events/?city_id=" + ChangeCity.cityId + "&title=" + editSearch.getText().toString().replaceAll(" ", "%20") + 
			"&time_start%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true"; */
		
		//menggunakan basis time_end
		if (editSearch.getText().toString().equals("")) {
			if (status.equals("event")) {
				UrlTarget = "http://api.wakuwakuw.com/rest/events/?city_id=" + Timeline.cityId + 
				"&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true";
			}
			else if (status.equals("meetup")) {
				UrlTarget = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + Timeline.cityId + 
				"&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true";
			}
		}
		else {
			if (status.equals("event")) {
				UrlTarget = "http://api.wakuwakuw.com/rest/events/?city_id=" + Timeline.cityId + "&title=" + URLEncoder.encode(editSearch.getText().toString()) + 
				"&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true";
			}
			else if (status.equals("meetup")) {
				UrlTarget = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + Timeline.cityId + "&title=" + URLEncoder.encode(editSearch.getText().toString()) + 
				"&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true";
			}
		}
		
		
		XMLParser parser = new XMLParser();
		xml = parser.getXmlFromUrl(UrlTarget, Timeline.isiUsername, Timeline.isiPassword);
		
		if (xml != null) {
			String Id = null;
			
			FormSearchResult.adapter = new SeparatedListAdapter(Search.this);
			
			isiId = new ArrayList<String>();
			isiIdLogo = new ArrayList<String>();
			isiLogo = new ArrayList<Drawable>();
			isiJudul = new ArrayList<String>();
			isiPenjelasan = new ArrayList<String>();
			isiAlamat = new ArrayList<String>();
			isiTanggalStart = new ArrayList<String>();
			isiTanggalEnd = new ArrayList<String>();
			isiJenis = new ArrayList<String>();
			isiLinkURL = new ArrayList<String>();
			isiKoorLat = new ArrayList<String>();
			isiKoorLong = new ArrayList<String>();
			
			isiJmlhMember = new ArrayList<String>();
			isiJmlhComment = new ArrayList<String>();
			isiJmlhLike = new ArrayList<String>();
			
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					
					Id = parser.getValue(e, ID);
					
					if (status.equals("event")) {
						isiLogo.add(LoadImageFromWeb("http://wakuwakuw.com/img/event/" + Id + "?size=stream"));
						isiIdLogo.add(Id);
						
						//ini buat ngambil nama kategori (untuk event)
						categoryId = parser.getValue(e, EVENT_CATEGORY_ID);
						UrlCategoryID = "http://api.wakuwakuw.com/rest/event_categories/" + categoryId;
						UrlStats = "http://api.wakuwakuw.com/rest/event_stats/?event_id=" + Id;
					}
					else if (status.equals("meetup")) {
						String communityId = parser.getValue(e, COMMUNITY_ID);
						isiIdLogo.add(communityId);
						isiLogo.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + communityId + "?size=stream"));
						
						//ini buat ngambil nama kategori (untuk meetup)
						categoryId = parser.getValue(e, COMMUNITY_CATEGORY_ID);
						UrlCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + categoryId;
						UrlStats = "http://api.wakuwakuw.com/rest/meetup_stats/?meetup_id=" + Id;
					}
					
					isiId.add(Id);
					isiJudul.add(parser.getValue(e, TITLE));
					isiPenjelasan.add(parser.getValue(e, DESCRIPTION));
					isiAlamat.add(parser.getValue(e, LOCATION));
					
					Date timeStart = new Date();
					timeStart.setTime(Long.parseLong(parser.getValue(e, TIME_START)) * 1000);
					isiTanggalStart.add(timeStart.toLocaleString());
					
					Date timeEnd = new Date();
					timeEnd.setTime(Long.parseLong(parser.getValue(e, TIME_END)) * 1000);
					isiTanggalEnd.add(timeEnd.toLocaleString());
					
					isiLinkURL.add(parser.getValue(e, SLUG));
					isiKoorLat.add(parser.getValue(e, LATITUDE));
					isiKoorLong.add(parser.getValue(e, LONGITUDE));
					
					
					//ini buat ngambil nama kategori
					XMLParser parser2 = new XMLParser();
					String xmlCategory = parser2.getXmlFromUrl(UrlCategoryID, Timeline.isiUsername, Timeline.isiPassword);
					Document doc2 = parser2.getDomElement(xmlCategory);
					NodeList nl2 = doc2.getElementsByTagName(DATA);
					Element ele = (Element) nl2.item(0);
					isiJenis.add(parser2.getValue(ele, CATEGORY_NAME));
					//////////////////////////////////////////////////
					
					//ini buat ngambil jumlah member, comment & like
					XMLParser parserStats = new XMLParser();
					String xmlStats = parserStats.getXmlFromUrl(UrlStats, Timeline.isiUsername, Timeline.isiPassword);
					Document docStats = parserStats.getDomElement(xmlStats);
					NodeList nlStats = docStats.getElementsByTagName(DATA);
					if (nlStats.getLength() != 0) {
						Element elMeetupStats = (Element) nlStats.item(0);
						
						isiJmlhMember.add(parserStats.getValue(elMeetupStats, TOTAL_GUESTS));
						isiJmlhComment.add(parserStats.getValue(elMeetupStats, TOTAL_COMMENTS));
						isiJmlhLike.add(parserStats.getValue(elMeetupStats, TOTAL_LIKES));
					}
					else {
						isiJmlhMember.add("0"); isiJmlhComment.add("0"); isiJmlhLike.add("0"); 
					}
					///////////////////////////////////////////////
				}
				
				//untuk menyusun daftar event/meetup sesuai dgn tanggalnya
				dateArranger.arrangeDate(Search.this, FormSearchResult.adapter, isiId, isiIdLogo, isiLogo, isiJudul, isiPenjelasan, isiAlamat, 
						isiTanggalStart, isiTanggalEnd, isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMember, isiJmlhComment, isiJmlhLike,
						isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKumpStart, isiTanggalKumpEnd, 
						isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump,
						customAdapter, status);
				
				FormSearchResult.itemFound = isiJudul.size();
			}
			else {
				FormSearchResult.adapter = null;
				Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_LONG).show();
			}
		}
		else {
			Toast.makeText(getApplicationContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
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
