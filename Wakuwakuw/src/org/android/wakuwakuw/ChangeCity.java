package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeCity extends Activity implements OnClickListener {

	private AutoCompleteTextView editChangeCity;
	private Button btnChangeCity;
	
	private XMLParser parser, parserMeetup, parserCommun;
	//public static SeparatedListAdapter adapter, adapter2;
	private DateArranger dateArranger;
	private CustomAdapter customAdapter;
	//public static CustomAdapterCommun customAdapterCommun;
	
	private String Url_City, Url_Country, xml, xmlCity, cityName, countryId, countryName, xmlCountry, statusKota;
	//public static String cityId;
	private String eventId, meetupId, communityId;
	
	public String URL_Events;
	public String URL_Meetups;
	public String URL_Communs;
	
	
	//punyanya event
	private ArrayList<Drawable> isiLogo;
	private ArrayList<String> isiId, isiIdLogo, isiJudul, isiPenjelasan, isiAlamat, isiTanggalStart, isiTanggalEnd, 
			isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMemberEvent, isiJmlhCommentEvent, isiJmlhLikeEvent;
	
	private ArrayList<Drawable> isiLogoKump;
	private ArrayList<String> isiIdKump, isiIdLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKump, isiTanggalKumpEnd, 
					isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKumpEvent, isiJmlhCommentKumpEvent, isiJmlhLikeKumpEvent;
	///////////////////////////////////////////////////////////////////
	
	//punyanya meetup//////
	private ArrayList<Drawable> isiLogoMeetup;
	private ArrayList<String> isiIdMeetup, isiIdLogoMeetup, isiJudulMeetup, isiPenjelasanMeetup, isiAlamatMeetup, isiTanggalMeetupStart,
							isiTanggalMeetupEnd, isiJenisMeetup, isiLinkURLMeetup, isiKoorLatMeetup, isiKoorLongMeetup,
							isiJmlhMemberMeetup, isiJmlhCommentMeetup, isiJmlhLikeMeetup;
	
	private ArrayList<Drawable> isiLogoKumpMeetup;
	private ArrayList<String> isiIdKumpMeetup, isiIdLogoKumpMeetup, isiJudulKumpMeetup, isiPenjelasanKumpMeetup, isiAlamatKumpMeetup, 
				isiTanggalKumpMeetup, isiTanggalKumpMeetupEnd, isiJenisKumpMeetup, isiLinkURLKumpMeetup, isiKoorLatKumpMeetup, isiKoorLongKumpMeetup,
				isiJmlhMemberKumpMeetup, isiJmlhCommentKumpMeetup, isiJmlhLikeKumpMeetup;
	private SimpleDateFormat outFormatMeetup;
	private String tanggalSamaMeetup;
	//////////////////////////////////////////////////////////////////////////

	
	//ini buat Communities
	private ArrayList<Drawable> isiLogoCommun;
	private ArrayList<String> isiIdCommun, isiIdLogoCommun, isiNamaCommun, isiJenisCommun, isiDeskripsiCommun, 
			isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun;
	
	
	public int x;
	private SimpleDateFormat outFormat;
	private String tanggalSama;
	private String eventCategoryID, communityCategoryID;
	
	private Date waktuSkrng;
	private long bilWaktuSkrng;
	
	private boolean eventsLoadFinished, meetupsLoadFinished, communsLoadFinished;
	
	//BackgroundThread backgroundThread;
	ProgressDialog progressDialog;
	
	public static DownloadTaskEvent donlodTaskEvents;
	public static DownloadTaskMeetup donlodTaskMeetups;
	public static DownloadTaskCommun donlodTaskCommuns;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_city);
		
		//class bantu
		dateArranger = new DateArranger();
		
		editChangeCity = (AutoCompleteTextView)findViewById(R.id.autoCompleteChangeCity);
		btnChangeCity = (Button)findViewById(R.id.btnChangeCity);
		btnChangeCity.setOnClickListener(this);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		//return super.onRetainNonConfigurationInstance();
		//return backgroundThread;
		
		return null;
	}
	
	/*
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//if (progressDialog != null && progressDialog.isShowing()) {
		//	progressDialog.dismiss();
		//	progressDialog = null;
		//}
		
		this.donlodTaskEvents.cancel(true); this.donlodTaskMeetups.cancel(true); this.donlodTaskCommuns.cancel(true);
		adapter = null; adapter2 = null; customAdapter = null;
		super.onDestroy();
	}
	*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnChangeCity) {
			if (editChangeCity.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "Input Your City First..", Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(ChangeCity.this, "Please Wait While Retrieving Data", Toast.LENGTH_SHORT).show();
				
				//Url_City = "http://api.wakuwakuw.com/rest/cities/?name=" + editChangeCity.getText().toString().replaceAll(" ", "%20");
				Url_City = "http://api.wakuwakuw.com/rest/cities/?name=" + URLEncoder.encode(editChangeCity.getText().toString());				
				
				parser = new XMLParser();
				xmlCity = parser.getXmlFromUrl(Url_City, Timeline.isiUsername, Timeline.isiPassword);
				
				if (xmlCity != null || !xml.equals("")) {
					Document doc = parser.getDomElement(xmlCity);
					NodeList nl = doc.getElementsByTagName(DATUM);
					
					if (nl.getLength() == 0) {
						//Timeline.listEvents = new ListView(ChangeCity.this);
						//Timeline.listMeetups = new ListView(ChangeCity.this);
						//Timeline.listCommunities = new ListView(ChangeCity.this);
						
						Timeline.adapter = null; Timeline.adapter2 = null; Timeline.customAdapterCommun = null;
						Timeline.listViewCategory.setAdapter(null);
						
						//cityName = "-----------------";
						//statusKota = "Finished..";
						Timeline.txtCityTimeline.setText("--------------------------");
						Timeline.cityName = "";
						
						Toast.makeText(ChangeCity.this, "City Not Found.", Toast.LENGTH_LONG).show();
					}
					else if (nl.getLength() >= 1) {
						Timeline.progressBarTimeline.setVisibility(View.VISIBLE);
						
						Timeline.imgViewSeparator2.setVisibility(View.VISIBLE);
		        		Timeline.imgBtnChangeCategory.setVisibility(View.VISIBLE);
		        		Timeline.imgViewSeparatorUnder2.setVisibility(View.VISIBLE);
		        		Timeline.imgBtnSearch.setVisibility(View.VISIBLE);
						//statusKota = "Finished..";
						
						//ngambil nama kota (city)
						Element el = (Element) nl.item(0);
						// adding each child node to HashMap key => value
						Timeline.cityId = parser.getValue(el, CITY_ID);
						cityName = parser.getValue(el, CITY_NAME);
						countryId = parser.getValue(el, COUNTRY_ID);
						
						Timeline.latitudeOverall = parser.getValue(el, "lat");
						Timeline.longitudeOverall = parser.getValue(el, "lng");
						
						//mengambil nama negara (country)
						Url_Country = "http://api.wakuwakuw.com/rest/countries/?id=" + countryId;
						
						XMLParser parserCountry = new XMLParser();
						xmlCountry = parserCountry.getXmlFromUrl(Url_Country, Timeline.isiUsername, Timeline.isiPassword);
						Document docCountry = parserCountry.getDomElement(xmlCountry);
						NodeList nlCountry = docCountry.getElementsByTagName(DATA);
						Element elCountry = (Element) nlCountry.item(0);
						countryName = parserCountry.getValue(elCountry, COUNTRY_NAME);
						//sampai disini (mengambil nama negara)
						
						Timeline.txtCityTimeline.setText(cityName + ", " + countryName);
						Timeline.cityName = cityName;
						
						//ini buat mengambil tanggal sekarang
						waktuSkrng = new Date();
						bilWaktuSkrng = waktuSkrng.getTime() / 1000;
						///////////////////////////////////
						
						//ini masih menggunakan basis time start (time_start >= now), saat time start berakhir maka event yg masih berlangsung
						//dalam kurun waktu tertentu tidak ditampilkan (time_start s/d time_end)
						//URL_Events = "http://api.wakuwakuw.com/rest/events/?city_id=" + cityId + "&time_start%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true&take=20";
						
						//ini menggunaan basis time end (time_end >= now), jadi event yg berlangsung dalam kurun waktu tertentu dan masih lama tetap ditampilkan
						URL_Events = "http://api.wakuwakuw.com/rest/events/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng)+ "&order_by=time_start&ascending=true&take=20";
						
						//ini masih menggunakan basis time start (time_start > now), saat time start berakhir maka event yg masih berlangsung
						//dalam kurun waktu tertentu tidak ditampilkan (time_start s/d time_end)
						//URL_Meetups = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + cityId + "&time_start%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true&take=20";
						
						//ini menggunaan basis time end (time_end >= now), jadi event yg berlangsung dalam kurun waktu tertentu dan masih lama tetap ditampilkan
						URL_Meetups = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true&take=20";
						
						URL_Communs = "http://api.wakuwakuw.com/rest/communities/?status=1";
						
						
						// menggunakan AsyncTask
						donlodTaskEvents = new DownloadTaskEvent();
						donlodTaskEvents.execute(URL_Events);
						donlodTaskMeetups = new DownloadTaskMeetup();
						donlodTaskMeetups.execute(URL_Meetups);
						donlodTaskCommuns = new DownloadTaskCommun();
						donlodTaskCommuns.execute(URL_Communs);
						// menggunakan AsyncTask
					}
				}
				else {
					//Timeline.listEvents = new ListView(ChangeCity.this);
					//Timeline.listMeetups = new ListView(ChangeCity.this);
					//Timeline.listCommunities = new ListView(ChangeCity.this);
					//Timeline.txtCityTimeline.setText("------");
					
					//Timeline.listViewCategory.setAdapter(null);
					Timeline.adapter = null; Timeline.adapter2 = null; Timeline.customAdapterCommun = null;
					Timeline.txtCityTimeline.setText("-------------------------------------");
					
					Toast.makeText(ChangeCity.this, "No Connection", Toast.LENGTH_SHORT).show();
					
					//cityName = "-----------------";
					//statusKota = "No Connection.";
				}
				
				finish();
				//sampai sini/////////////////////
				
			}
		}
	}
	
	
	
	//retrieve data melalui AsyncTask///////////
	public class DownloadTaskEvent extends AsyncTask<String, Void, SeparatedListAdapter> {
		
		protected SeparatedListAdapter doInBackground(String... URL_Events) {
			// TODO Auto-generated method stub
			//return null;
			
			if (donlodTaskEvents.isCancelled()) {
                //break;
				Timeline.adapter = null;
            }
			else {
				//ini buat events//
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
				
				isiJmlhMemberEvent = new ArrayList<String>();
				isiJmlhCommentEvent = new ArrayList<String>();
				isiJmlhLikeEvent = new ArrayList<String>();
				
				Timeline.adapter = new SeparatedListAdapter(ChangeCity.this);
				
				XMLParser parser = new XMLParser();
				xml = parser.getXmlFromUrl(URL_Events[0], Timeline.isiUsername, Timeline.isiPassword);
				Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						eventId = parser.getValue(e, ID);
						isiLogo.add(LoadImageFromWeb("http://wakuwakuw.com/img/event/" + eventId + "?size=stream"));
						
						//isiLogo.add(getResources().getDrawable(R.drawable.event));
						//isiLogo.add(null);
						isiId.add(eventId);
						isiIdLogo.add(eventId);
						isiJudul.add(parser.getValue(e, TITLE));
						isiPenjelasan.add(parser.getValue(e, DESCRIPTION));
						isiAlamat.add(parser.getValue(e, LOCATION));
						
						Date time = new Date();
						time.setTime(Long.parseLong(parser.getValue(e, TIME_START)) * 1000);
						isiTanggalStart.add(time.toLocaleString());
						
						Date timeEnd = new Date();
						timeEnd.setTime(Long.parseLong(parser.getValue(e, TIME_END)) * 1000);
						isiTanggalEnd.add(timeEnd.toLocaleString());
						
						//isiTanggal.add(parser.getValue(e, TIME_START));
						
						//ini buat ngambil nama kategori (untuk event)
						eventCategoryID = parser.getValue(e, EVENT_CATEGORY_ID);
						String URLEventCategoryID = "http://api.wakuwakuw.com/rest/event_categories/" + eventCategoryID;
						XMLParser parser2 = new XMLParser();
						String xmlEventCategory = parser2.getXmlFromUrl(URLEventCategoryID, Timeline.isiUsername, Timeline.isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenis.add(parser2.getValue(ele, CATEGORY_NAME));
						//////////////////////////////////////////////////
						
						//ini buat ngambil jumlah member, comment & like
						String URLEventStats = "http://api.wakuwakuw.com/rest/event_stats/?event_id=" + eventId;
						XMLParser parserEventStats = new XMLParser();
						String xmlEventStats = parserEventStats.getXmlFromUrl(URLEventStats, Timeline.isiUsername, Timeline.isiPassword);
						Document docEventStats = parserEventStats.getDomElement(xmlEventStats);
						NodeList nlEventStats = docEventStats.getElementsByTagName(DATA);
						if (nlEventStats.getLength() != 0) {
							Element elEventStats = (Element) nlEventStats.item(0);
							
							isiJmlhMemberEvent.add(parserEventStats.getValue(elEventStats, TOTAL_GUESTS));
							isiJmlhCommentEvent.add(parserEventStats.getValue(elEventStats, TOTAL_COMMENTS));
							isiJmlhLikeEvent.add(parserEventStats.getValue(elEventStats, TOTAL_LIKES));
						}
						else {
							isiJmlhMemberEvent.add("0"); isiJmlhCommentEvent.add("0"); isiJmlhLikeEvent.add("0"); 
						}
						///////////////////////////////////////////////
						
						isiLinkURL.add(parser.getValue(e, SLUG));
						isiKoorLat.add(parser.getValue(e, LATITUDE));
						isiKoorLong.add(parser.getValue(e, LONGITUDE));
					}
					
					//yg ini pake method bantu tapi class laen
					dateArranger.arrangeDate(ChangeCity.this, Timeline.adapter, isiId, isiIdLogo, isiLogo, isiJudul, isiPenjelasan, isiAlamat, 
							isiTanggalStart, isiTanggalEnd, isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMemberEvent, isiJmlhCommentEvent, isiJmlhLikeEvent,
							isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKump, isiTanggalKumpEnd, 
							isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKumpEvent, isiJmlhCommentKumpEvent, isiJmlhLikeKumpEvent,
							customAdapter, "event");
					
					
					//Timeline.listEvents = new ListView(ChangeCity.this);
					//Timeline.listEvents.setAdapter(adapter);
				}
				else {
					//Timeline.listEvents = new ListView(ChangeCity.this);
					Timeline.adapter = null;
					//Timeline.listViewCategory.setAdapter(null);
				}
				////////////////////////////////////////////
				
				eventsLoadFinished = true;
			}
			
			return Timeline.adapter;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskEvents.isCancelled()) {
                //break;
				Timeline.adapter = null;
            }
			//super.onProgressUpdate(values);
		}
		
		protected void onPostExecute(SeparatedListAdapter adapter) {
			// TODO Auto-generated method stub
			//ini buat menset nama kota (jika ada) dan memberitahukannya (dgn Toast)
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(ChangeCity.this, "Events Loading Finished", Toast.LENGTH_SHORT).show();
			
			//Timeline.imgBtnChangeCategory.setImageResource(R.drawable.event);
			//Timeline.listViewCategory.setAdapter(adapter);
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.event)) {
				if (adapter == null) {
					Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			//finish();
			//super.onPostExecute(result);
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished)
				Timeline.progressBarTimeline.setVisibility(View.GONE);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			Timeline.adapter = null;
			super.onCancelled();
		}
	}
	
	public class DownloadTaskMeetup extends AsyncTask<String, Void, SeparatedListAdapter> {
		
		protected SeparatedListAdapter doInBackground(String... URL_Meetups) {
			// TODO Auto-generated method stub
			//return null;
			
			if (donlodTaskMeetups.isCancelled()) {
                //break;
				Timeline.adapter2 = null;
            }
			else {
				isiIdMeetup = new ArrayList<String>();
				isiIdLogoMeetup = new ArrayList<String>();
				isiLogoMeetup = new ArrayList<Drawable>();
				isiJudulMeetup = new ArrayList<String>();
				isiPenjelasanMeetup = new ArrayList<String>();
				isiAlamatMeetup = new ArrayList<String>();
				isiTanggalMeetupStart = new ArrayList<String>();
				isiTanggalMeetupEnd = new ArrayList<String>();
				isiJenisMeetup = new ArrayList<String>();
				isiLinkURLMeetup = new ArrayList<String>();
				isiKoorLatMeetup = new ArrayList<String>();
				isiKoorLongMeetup = new ArrayList<String>();
				
				isiJmlhMemberMeetup = new ArrayList<String>();
				isiJmlhCommentMeetup = new ArrayList<String>();
				isiJmlhLikeMeetup = new ArrayList<String>();				
				
				Timeline.adapter2 = new SeparatedListAdapter(ChangeCity.this);
				
				parserMeetup = new XMLParser();
				String xmlMeetup = parserMeetup.getXmlFromUrl(URL_Meetups[0], Timeline.isiUsername, Timeline.isiPassword);
				Document doc = parserMeetup.getDomElement(xmlMeetup.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						meetupId = parserMeetup.getValue(e, COMMUNITY_ID);
						isiLogoMeetup.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + meetupId + "?size=stream"));
						
						//isiLogo.add(getResources().getDrawable(R.drawable.meetups));
						//isiLogoMeetup.add(null);
						isiIdMeetup.add(parserMeetup.getValue(e, ID));
						isiIdLogoMeetup.add(meetupId);
						isiJudulMeetup.add(parserMeetup.getValue(e, TITLE));
						isiPenjelasanMeetup.add(parserMeetup.getValue(e, DESCRIPTION));
						isiAlamatMeetup.add(parserMeetup.getValue(e, LOCATION));
						
						Date time = new Date();
						time.setTime(Long.parseLong(parserMeetup.getValue(e, TIME_START)) * 1000);
						isiTanggalMeetupStart.add(time.toLocaleString());
						
						Date timeEnd = new Date();
						timeEnd.setTime(Long.parseLong(parserMeetup.getValue(e, TIME_END)) * 1000);
						isiTanggalMeetupEnd.add(timeEnd.toLocaleString());
						
						//isiTanggal.add(parser.getValue(e, TIME_START));
						
						//ini buat ngambil nama kategori (untuk meetup)
						communityCategoryID = parserMeetup.getValue(e, COMMUNITY_CATEGORY_ID);
						String URLCommunityCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + communityCategoryID;
						XMLParser parser2 = new XMLParser();
						String xmlEventCategory = parser2.getXmlFromUrl(URLCommunityCategoryID, Timeline.isiUsername, Timeline.isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenisMeetup.add(parser2.getValue(ele, CATEGORY_NAME));
						///////////////////////////////////////////////
						
						//ini buat ngambil jumlah member, comment & like
						String URLMeetupStats = "http://api.wakuwakuw.com/rest/meetup_stats/?meetup_id=" + isiIdMeetup.get(i);
						XMLParser parserMeetupStats = new XMLParser();
						String xmlMeetupStats = parserMeetupStats.getXmlFromUrl(URLMeetupStats, Timeline.isiUsername, Timeline.isiPassword);
						Document docMeetupStats = parserMeetupStats.getDomElement(xmlMeetupStats);
						NodeList nlMeetupStats = docMeetupStats.getElementsByTagName(DATA);
						if (nlMeetupStats.getLength() != 0) {
							Element elMeetupStats = (Element) nlMeetupStats.item(0);
							
							isiJmlhMemberMeetup.add(parserMeetupStats.getValue(elMeetupStats, TOTAL_GUESTS));
							isiJmlhCommentMeetup.add(parserMeetupStats.getValue(elMeetupStats, TOTAL_COMMENTS));
							isiJmlhLikeMeetup.add(parserMeetupStats.getValue(elMeetupStats, TOTAL_LIKES));
						}
						else {
							isiJmlhMemberMeetup.add("0"); isiJmlhCommentMeetup.add("0"); isiJmlhLikeMeetup.add("0"); 
						}
						///////////////////////////////////////////////
						
						isiLinkURLMeetup.add(parserMeetup.getValue(e, SLUG));
						isiKoorLatMeetup.add(parserMeetup.getValue(e, LATITUDE));
						isiKoorLongMeetup.add(parserMeetup.getValue(e, LONGITUDE));
					}
					
					//yg ini pake method bantu tapi class laen
					dateArranger.arrangeDate(ChangeCity.this, Timeline.adapter2, isiIdMeetup, isiIdLogoMeetup, isiLogoMeetup, isiJudulMeetup, isiPenjelasanMeetup, isiAlamatMeetup, 
							isiTanggalMeetupStart, isiTanggalMeetupEnd, isiJenisMeetup, isiLinkURLMeetup, isiKoorLatMeetup, isiKoorLongMeetup, isiJmlhMemberMeetup, isiJmlhCommentMeetup, isiJmlhLikeMeetup,
							isiIdKumpMeetup, isiIdLogoKumpMeetup, isiLogoKumpMeetup, isiJudulKumpMeetup, isiPenjelasanKumpMeetup, isiAlamatKumpMeetup, isiTanggalKumpMeetup, isiTanggalKumpMeetupEnd, 
							isiJenisKumpMeetup, isiLinkURLKumpMeetup, isiKoorLatKumpMeetup, isiKoorLongKumpMeetup, isiJmlhMemberKumpEvent, isiJmlhCommentKumpMeetup, isiJmlhLikeKumpMeetup,
							customAdapter, "meetup");
					
					
					//Timeline.listMeetups = new ListView(ChangeCity.this);
					//Timeline.listMeetups.setAdapter(adapter2);
				}
				else {
					//Timeline.listMeetups = new ListView(ChangeCity.this);
					Timeline.adapter2 = null;
					//Timeline.listViewCategory.setAdapter(null);
				}	
				////////////////////////////////////
				
				meetupsLoadFinished = true;
			}
			
			return Timeline.adapter2;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskMeetups.isCancelled()) {
                //break;
				Timeline.adapter2 = null;
            }
			super.onProgressUpdate(values);
		}
		
		protected void onPostExecute(SeparatedListAdapter adapter) {
			// TODO Auto-generated method stub
			//ini buat menset nama kota (jika ada) dan memberitahukannya (dgn Toast)
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(ChangeCity.this, "Meetups Loading Finished", Toast.LENGTH_SHORT).show();
			
			//Timeline.imgBtnChangeCategory.setImageResource(R.drawable.event);
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.meetups)) {
				if (adapter == null) {
					Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			//finish();
			//super.onPostExecute(result);
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished)
				Timeline.progressBarTimeline.setVisibility(View.GONE);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			Timeline.adapter2 = null;
			super.onCancelled();
		}
	}
	
	public class DownloadTaskCommun extends AsyncTask<String, Void, CustomAdapterCommun> {

		@Override
		protected CustomAdapterCommun doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (donlodTaskCommuns.isCancelled()) {
                //break;
				Timeline.customAdapterCommun = null;
            }
			else {
				isiIdCommun = new ArrayList<String>();
				isiLogoCommun = new ArrayList<Drawable>();
				isiNamaCommun = new ArrayList<String>();
				isiJenisCommun = new ArrayList<String>();
				isiDeskripsiCommun = new ArrayList<String>();
				isiLinkURLCommun = new ArrayList<String>();
				
				isiJmlhMemberCommun = new ArrayList<String>();
				isiJmlhCommentCommun = new ArrayList<String>();
				isiJmlhLikeCommun = new ArrayList<String>();
				
				parserCommun = new XMLParser();
				String xmlCommun = parserCommun.getXmlFromUrl(URL_Communs, Timeline.isiUsername, Timeline.isiPassword);
				Document doc = parserCommun.getDomElement(xmlCommun.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						communityId = parserCommun.getValue(e, ID);
						isiIdCommun.add(communityId);
						isiLogoCommun.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + communityId + "?size=stream"));
						//Timeline.isiLogoCommun.add(getResources().getDrawable(R.drawable.communities));
						isiNamaCommun.add(parserCommun.getValue(e, NAME_COMMUN));
						
						//ini buat ngambil nama kategori (untuk community)
						communityCategoryID = parserCommun.getValue(e, COMMUNITY_CATEGORY_ID);
						String URLCommunityCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + communityCategoryID;
						XMLParser parser2 = new XMLParser();
						String xmlEventCategory = parser2.getXmlFromUrl(URLCommunityCategoryID, Timeline.isiUsername, Timeline.isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenisCommun.add(parser2.getValue(ele, CATEGORY_NAME));
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
						
						isiDeskripsiCommun.add(parserCommun.getValue(e, DESCRIP_COMMUN));
						isiLinkURLCommun.add(parserCommun.getValue(e, NAME_URI));
					}
					
					Timeline.customAdapterCommun = new CustomAdapterCommun(ChangeCity.this, isiIdCommun, isiLogoCommun, isiNamaCommun, 
							isiJenisCommun, isiDeskripsiCommun, isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun);
					//Timeline.listCommunities = new ListView(ChangeCity.this);
					//Timeline.listCommunities.setAdapter(customAdapterCommun);
				}
				else {
					Timeline.customAdapterCommun = null;
					//Timeline.listViewCategory.setAdapter(null);
				}
				//////////////////////////////////////////
				
				communsLoadFinished = true;
			}
			
			return Timeline.customAdapterCommun;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskCommuns.isCancelled()) {
                //break;
				Timeline.customAdapterCommun = null;
            }
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(CustomAdapterCommun adapter) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(ChangeCity.this, "Communities Loading Finished", Toast.LENGTH_SHORT).show();
			
			//Timeline.imgBtnChangeCategory.setImageResource(R.drawable.event);
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.communities)) {
				if (adapter == null) {
					Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished)
				Timeline.progressBarTimeline.setVisibility(View.GONE);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			Timeline.customAdapterCommun = null;
			super.onCancelled();
		}
	}
	//sampai sini (AsyncTask)

	
	
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
