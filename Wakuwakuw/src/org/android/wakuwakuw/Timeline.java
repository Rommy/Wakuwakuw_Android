package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.android.wakuwakuw.ChangeCity.DownloadTaskCommun;
import org.android.wakuwakuw.ChangeCity.DownloadTaskEvent;
import org.android.wakuwakuw.ChangeCity.DownloadTaskMeetup;
import org.android.wakuwakuw.Search.BackgroundThread;
import org.android.wakuwakuw.service.ScheduleClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Timeline extends Activity {
	
	public static ImageView imgViewSeparator2, imgViewSeparator3, imgViewSeparatorUnder2;
	public static ImageButton imgBtnChangeCity, imgBtnSearch, imgBtnChangeCategory, imgBtnLogin, imgBtnToolBox;
	public static ListView listViewCategory;
	public static TextView txtCityTimeline;
	public static ProgressBar progressBarTimeline;
	
	private static final int ID_EVENT    = 1;
	private static final int ID_MEETUP   = 2;
	private static final int ID_COMMUNITY = 3;
	
	private static final int ID_NOTIFICATION = 1;
	private static final int ID_MESSAGE = 2;
	private static final int ID_USER_PROFILE = 3;
	//private static final int ID_CREATE_NEW = 4;
	
	private ConnectivityManager connect;
	private NetworkInfo wifi, mobile;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latUser, longUser;
	
	private final String RESPONSE = "Response";
	private final String ADMINISTRATIVE_AREA_NAME = "AdministrativeAreaName";
	private final String LOCALITY_NAME = "LocalityName";
	
	private String administrativeAreaName, localityName;
	public static String cityName, cityNameFromGPS, latitudeOverall, longitudeOverall;
	
	public static String cityId = "";
	
	private String Url_City, Url_Country, xml, xmlCity, countryId, countryName, xmlCountry;
	private String eventId, meetupId, communityId;
	
	public String URL_Events, URL_Meetups, URL_Communs;
	
	
	private Date waktuSkrng;
	private long bilWaktuSkrng;
	
	//punyanya event//
	private ArrayList<Drawable> isiLogo;
	private ArrayList<String> isiId, isiIdLogo, isiJudul, isiPenjelasan, isiAlamat, isiTanggalStart, isiTanggalEnd, 
			isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMember, isiJmlhComment, isiJmlhLike;
	
	private ArrayList<Drawable> isiLogoKump;
	private ArrayList<String> isiIdKump, isiIdLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
				isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, 
				isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump;
	///////////////////////////////////////////////////////////
	
	
	
	//punyanya meetup//////
	private ArrayList<Drawable> isiLogoMeetup;
	private ArrayList<String> isiIdMeetup, isiIdLogoMeetup, isiJudulMeetup, isiPenjelasanMeetup, isiAlamatMeetup, isiTanggalMeetupStart,
							isiTanggalMeetupEnd, isiJenisMeetup, isiLinkURLMeetup, isiKoorLatMeetup, isiKoorLongMeetup,
							isiJmlhMemberMeetup, isiJmlhCommentMeetup, isiJmlhLikeMeetup;
	
	private ArrayList<Drawable> isiLogoKumpMeetup;
	private ArrayList<String> isiIdKumpMeetup, isiIdLogoKumpMeetup, isiJudulKumpMeetup, isiPenjelasanKumpMeetup, isiAlamatKumpMeetup, isiTanggalKumpMeetup,
						isiTanggalKumpMeetupEnd, isiJenisKumpMeetup, isiLinkURLKumpMeetup, isiKoorLatKumpMeetup, isiKoorLongKumpMeetup,
						isiJmlhMemberKumpMeetup, isiJmlhCommentKumpMeetup, isiJmlhLikeKumpMeetup;
	private SimpleDateFormat outFormatMeetup;
	private String tanggalSamaMeetup;
	/////////////////////////////////////////////////////////////

	
	//ini buat Communities
	private ArrayList<Drawable> isiLogoCommun;
	private ArrayList<String> isiIdCommun, isiIdLogoCommun, isiNamaCommun, isiJenisCommun, isiDeskripsiCommun, 
			isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun;
	
	
	public int x;
	private SimpleDateFormat outFormat;
	private String tanggalSama;
	private String eventCategoryID, communityCategoryID;
	
	public static SeparatedListAdapter adapter, adapter2;
	private CustomAdapter customAdapter;
	public static CustomAdapterCommun customAdapterCommun;
	
	private DateArranger dateArranger;
	
	private boolean eventsLoadFinished, meetupsLoadFinished, communsLoadFinished;
	
	public static DownloadTaskEvent donlodTaskEvents;
	public static DownloadTaskMeetup donlodTaskMeetups;
	public static DownloadTaskCommun donlodTaskCommuns;
	
	private QuickActionLogin quickActionLogin;
	private QuickAction quickActionToolBox;
	
	private final String USER_ID = "User_Id";
	private final String USERNAME = "Username";
	private final String PASSWORD = "Password";
	private final String ID_USER = "id";
	private final String NAME_USER = "name";
	private final String CITY_ID_USER = "city_id";
	public static String isiUsername, isiPassword, namaUser, idUser, cityIdUser;
	public static Drawable logoUser;
	
	private String pilihanNearMe[] = {"Events Nearby", "Meetups Nearby"};
	
	private AlertDialog alert;
	
	private SharedPreferences myPrefs;
	
	
	//tes notifikasi
	private static final int MY_NOTIFICATION_ID = 1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	private final String myBlog = "http://android-er.blogspot.com/";
	
	public ArrayAdapter<String> adapterChangeCity;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private ContentValues cvEvent, cvMeetup, cvCommun, cvCitySearch;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		adapter = null; adapter2 = null; customAdapterCommun = null;
		dateArranger = new DateArranger();  

		
		//buat Action Item
		ActionItem eventItem = new ActionItem(ID_EVENT, "Event", getResources().getDrawable(R.drawable.event));
		ActionItem meetupItem = new ActionItem(ID_MEETUP, "Meetup", getResources().getDrawable(R.drawable.meetups));
        ActionItem communityItem = new ActionItem(ID_COMMUNITY, "Community", getResources().getDrawable(R.drawable.communities));
        
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout orientation
		final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(eventItem);
		quickAction.addActionItem(meetupItem);
        quickAction.addActionItem(communityItem);
        
        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				//ActionItem actionItem = quickAction.getActionItem(pos);
                
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_EVENT) {
					//Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
					imgBtnChangeCategory.setImageResource(R.drawable.event);
					imgBtnChangeCategory.setTag(R.drawable.event);
					
					if (adapter == null) {
						//listViewCategory.setBackgroundResource(R.drawable.background_none);
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					else {
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					listViewCategory.setAdapter(adapter);
				} else if (actionId == ID_MEETUP) {
					//Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
					imgBtnChangeCategory.setImageResource(R.drawable.meetups);
					imgBtnChangeCategory.setTag(R.drawable.meetups);
					
					if (adapter2 == null) {
						//listViewCategory.setBackgroundResource(R.drawable.background_none);
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					else {
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					listViewCategory.setAdapter(adapter2);
				} else {
					//Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
					imgBtnChangeCategory.setImageResource(R.drawable.communities);
					imgBtnChangeCategory.setTag(R.drawable.communities);
					
					if (customAdapterCommun == null) {
						//listViewCategory.setBackgroundResource(R.drawable.background_none);
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					else {
						listViewCategory.setBackgroundDrawable(null);
						listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
					}
					listViewCategory.setAdapter(customAdapterCommun);
				}
			}
		});
		
        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
			}
		});
        
        
        //buat Action Item untuk ToolBox
		ActionItem notificationItem = new ActionItem(ID_NOTIFICATION, "Notification", getResources().getDrawable(R.drawable.notification));
		ActionItem messageItem = new ActionItem(ID_MESSAGE, "Message", getResources().getDrawable(R.drawable.message_white));
		ActionItem userProfileItem = new ActionItem(ID_USER_PROFILE, "My Profile", getResources().getDrawable(R.drawable.user_info));
		//ActionItem createNewItem = new ActionItem(ID_CREATE_NEW, "Create New", getResources().getDrawable(R.drawable.notepad));
        
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout orientation
		quickActionToolBox = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickActionToolBox.addActionItem(notificationItem);
		quickActionToolBox.addActionItem(messageItem);
		quickActionToolBox.addActionItem(userProfileItem);
        //quickActionToolBox.addActionItem(createNewItem);
        
        //Set listener for action item clicked
        quickActionToolBox.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				//ActionItem actionItem = quickActionToolBox.getActionItem(pos);
				
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_NOTIFICATION) {
					
				} 
				else if (actionId == ID_MESSAGE) {
					
				}
				else if (actionId == ID_USER_PROFILE) {
					FormUserInfo.statusOpenedFrom = "Timeline";
					
					Intent in = new Intent(Timeline.this, FormUserInfo.class);
					startActivity(in);
				}
				/* ini untuk create new (Event, Meetup, Community)
				else if (actionId == ID_CREATE_NEW) {
					//Dialog dialog = new Dialog(Timeline.this);
					//dialog.setContentView(R.layout.custom_dialog_create_new);
					//dialog.setTitle("Create New");
					//dialog.show();
					//ListView listCreateNew = (ListView)findViewById(R.id.listViewCreateNew);
					
					AlertDialog.Builder optionCreateNew = new AlertDialog.Builder(Timeline.this);
					optionCreateNew.setTitle("Create New");
					optionCreateNew.setItems(R.array.pilihanCreateNew, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int position) {
							// TODO Auto-generated method stub
							if (position == 0) {
								Intent in = new Intent(Timeline.this, FormCreateNewEvent.class);
								startActivity(in);
							}
							else if (position == 1) {
								Intent in = new Intent(Timeline.this, FormCreateNewMeetup.class);
								startActivity(in);
							}
							else if (position == 2) {
								Intent in = new Intent(Timeline.this, FormCreateNewCommunity.class);
								startActivity(in);
							}
						}
					});
					
					AlertDialog alertCreateNew = optionCreateNew.create();
					alertCreateNew.show();
				}
				*/
			}
		});
        
        
        
		imgBtnChangeCategory = (ImageButton)findViewById(R.id.imgButtonChangeCategory);
		imgBtnChangeCategory.setTag(R.drawable.event);
		imgBtnChangeCategory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quickAction.show(v);
			}
		});
		
		imgBtnLogin = (ImageButton)findViewById(R.id.imgButtonLogIn);
		imgBtnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_WORLD_READABLE);
				
				isiUsername = myPrefs.getString(USERNAME, "");
		        isiPassword = myPrefs.getString(PASSWORD, "");
		        
		        if (isiUsername.equals("") && isiPassword.equals("")) {
		        	//ini QuickAction buat LogIn
		            quickActionLogin = new QuickActionLogin(getApplicationContext(), QuickActionLogin.NOT_LOGGED_IN);
		            quickActionLogin.addActionItem();
		            quickActionLogin.setOnDismissListener(new QuickActionLogin.OnDismissListener() {
		    			
		    			@Override
		    			public void onDismiss() {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    		});
		        }
		        else {
		        	//ini QuickAction buat LogIn
		            quickActionLogin = new QuickActionLogin(getApplicationContext(), QuickActionLogin.LOGGED_IN);
		            quickActionLogin.addActionItem();
		            quickActionLogin.setOnDismissListener(new QuickActionLogin.OnDismissListener() {
		    			
		    			@Override
		    			public void onDismiss() {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    		});
		        }  

				quickActionLogin.show(v);
			}
		});
		
		imgBtnChangeCity = (ImageButton)findViewById(R.id.imgButtonChangeCity);
		imgBtnChangeCity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent in = new Intent(Timeline.this, ChangeCity.class);
				//startActivity(in);
				
				eventsLoadFinished = false; meetupsLoadFinished = false; communsLoadFinished = false;
				
				//ini autocomplete untuk change city
				//adapterChangeCity = new ArrayAdapter<String>(Timeline.this, android.R.layout.simple_dropdown_item_1line);
				adapterChangeCity = dbHelper.getListAdapter(Timeline.this, database, "SELECT city_search.city FROM city_search");
				adapterChangeCity.setNotifyOnChange(true);
				
				AlertDialog.Builder changeCity = new AlertDialog.Builder(Timeline.this);
				LayoutInflater inflater = (LayoutInflater) Timeline.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.change_city, (ViewGroup) findViewById(R.id.containerChangeCity));
				
				final AutoCompleteTextView autoCompleteChangeCity = (AutoCompleteTextView)layout.findViewById(R.id.autoCompleteChangeCity);
				autoCompleteChangeCity.setAdapter(adapterChangeCity);
				
				Button btnChangeCity = (Button)layout.findViewById(R.id.btnChangeCity);
				btnChangeCity.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (autoCompleteChangeCity.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(), "Please Input Your City..", Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(getApplicationContext(), "Please Wait While Retrieving Data", Toast.LENGTH_SHORT).show();
							
							String Url_Change_City = "http://api.wakuwakuw.com/rest/cities/?name=" + URLEncoder.encode(autoCompleteChangeCity.getText().toString());				
							
							XMLParser parser = new XMLParser();
							String xmlCity = parser.getXmlFromUrl(Url_Change_City, isiUsername, isiPassword);
							
							if (xmlCity != null || !xml.equals("")) {
								Document doc = parser.getDomElement(xmlCity);
								NodeList nl = doc.getElementsByTagName(DATUM);
								
								if (nl.getLength() == 0) {
									adapter = null; adapter2 = null; customAdapterCommun = null;
									listViewCategory.setAdapter(null);
									
									txtCityTimeline.setText("No City Found");
									cityName = "";
									
									Toast.makeText(getApplicationContext(), "City Not Found.", Toast.LENGTH_LONG).show();
								}
								else if (nl.getLength() >= 1) {
									progressBarTimeline.setVisibility(View.VISIBLE);
									
									imgViewSeparator2.setVisibility(View.VISIBLE);
					        		imgBtnChangeCategory.setVisibility(View.VISIBLE);
					        		imgViewSeparatorUnder2.setVisibility(View.VISIBLE);
					        		imgBtnSearch.setVisibility(View.VISIBLE);
									
									//ngambil nama kota (city)
									Element el = (Element) nl.item(0);
									// adding each child node to HashMap key => value
									cityId = parser.getValue(el, CITY_ID);
									cityName = parser.getValue(el, CITY_NAME);
									countryId = parser.getValue(el, COUNTRY_ID);
									
									latitudeOverall = parser.getValue(el, "lat");
									longitudeOverall = parser.getValue(el, "lng");
									
									//mengambil nama negara (country)
									Url_Country = "http://api.wakuwakuw.com/rest/countries/?id=" + countryId;
									
									XMLParser parserCountry = new XMLParser();
									xmlCountry = parserCountry.getXmlFromUrl(Url_Country, Timeline.isiUsername, Timeline.isiPassword);
									Document docCountry = parserCountry.getDomElement(xmlCountry);
									NodeList nlCountry = docCountry.getElementsByTagName(DATA);
									Element elCountry = (Element) nlCountry.item(0);
									countryName = parserCountry.getValue(elCountry, COUNTRY_NAME);
									//sampai disini (mengambil nama negara)
									
									txtCityTimeline.setText(cityName + ", " + countryName);
									
									
									//proses menyimpan data dari Wakuwakuw web ke dalam database SQLite untuk dapat digunakan nanti
									//proses pencekan apakah City name sudah pernah dipakai oleh user sebelumnya
									Cursor c = database.rawQuery("SELECT city_search.city FROM city_search WHERE city_search.city = '" + cityName + "'", null); 
									//jika belum pernah (yg diindikasikan dengan c.getCount() == 0) maka city tsb disimpan ke database
									//jika tidak maka tidak melakukan apa-apa
									if (c.getCount() == 0) {
										//mengosongkan adapter karna city tsb blm pernah dipakai
										adapterChangeCity.clear();
										
										cvCitySearch = new ContentValues();
										cvCitySearch.put(DatabaseHelper.COLUMN_CITY_NAME, cityName);
										database.insert(DatabaseHelper.TABLE_CITY_SEARCH, null, cvCitySearch);
									}
									c.close();
									
									
									//ini buat mengambil tanggal sekarang
									waktuSkrng = new Date();
									bilWaktuSkrng = waktuSkrng.getTime() / 1000;
									///////////////////////////////////
									
									URL_Events = "http://api.wakuwakuw.com/rest/events/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng)+ "&order_by=time_start&ascending=true&take=20";
									URL_Meetups = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng) + "&order_by=time_start&ascending=true&take=20";
									URL_Communs = "http://api.wakuwakuw.com/rest/communities/?status=1";
									
									// menggunakan AsyncTask
									donlodTaskEvents = new DownloadTaskEvent(); donlodTaskEvents.execute(URL_Events);
									donlodTaskMeetups = new DownloadTaskMeetup(); donlodTaskMeetups.execute(URL_Meetups);
									donlodTaskCommuns = new DownloadTaskCommun(); donlodTaskCommuns.execute(URL_Communs);
									// menggunakan AsyncTask
								}
							}
							else {
								adapter = null; adapter2 = null; customAdapterCommun = null;
								txtCityTimeline.setText("No Connection");
								
								Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
							}
							
							alert.dismiss();
						}
					}
				});
				
				Button btnClose = (Button)layout.findViewById(R.id.btnClose);
				btnClose.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
				
				changeCity.setView(layout);
				alert = changeCity.create();
				alert.show();
			}
		});
		
		imgBtnSearch = (ImageButton)findViewById(R.id.imgButtonSearch);
		imgBtnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(), Search.class);
				startActivity(in);
			}
		});
		
		txtCityTimeline = (TextView)findViewById(R.id.txtViewCityTimeline);
		
		listViewCategory = (ListView)findViewById(R.id.listViewCategory);
		//listViewCategory.setBackgroundResource(R.drawable.background_none);
		
		progressBarTimeline = (ProgressBar)findViewById(R.id.progressBarTimeline);
		
		imgViewSeparator2 = (ImageView)findViewById(R.id.imgViewSeparator2);
		imgViewSeparator3 = (ImageView)findViewById(R.id.imgViewSeparator3);
		imgViewSeparatorUnder2 = (ImageView)findViewById(R.id.imgViewSeparatorUnder2);
		
		imgBtnToolBox = (ImageButton)findViewById(R.id.imgButtonToolBox);
		imgBtnToolBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quickActionToolBox.show(v);
			}
		});
		
		
		myPrefs = this.getSharedPreferences("MyPrefs", MODE_WORLD_READABLE);
		
        //Shared Preferences buat mencek apakah cityId, cityName dan countryName sudah ada sebelumnya
        cityId = myPrefs.getString("city_id", "");
        cityName = myPrefs.getString("city_name", "");
        countryName = myPrefs.getString("country_name", "");
        if (cityId.equals("") || cityName.equals("") || countryName.equals("")) {
        	txtCityTimeline.setText("Please Set Your City");
        }
        else {
        	txtCityTimeline.setText(cityName + ", " + countryName);
        }
        //Toast.makeText(this, idUser + ", " + isiUsername + ", " + isiPassword, Toast.LENGTH_SHORT).show();
        
        //Shared Preferences buat mencek apakah sudah login atau belum
        idUser = myPrefs.getString(USER_ID, "");
		isiUsername = myPrefs.getString(USERNAME, "");
        isiPassword = myPrefs.getString(PASSWORD, "");
        
        //ini saat belum login
        if (isiUsername.equals("") && isiPassword.equals("")) {
        	if (cityId.equals("")) {
        		imgViewSeparator2.setVisibility(View.GONE);
        		imgBtnChangeCategory.setVisibility(View.GONE);
        		imgViewSeparatorUnder2.setVisibility(View.GONE);
        		imgBtnSearch.setVisibility(View.GONE);
        	}
        	else {
        		imgViewSeparator2.setVisibility(View.VISIBLE);
        		imgBtnChangeCategory.setVisibility(View.VISIBLE);
        		imgViewSeparatorUnder2.setVisibility(View.VISIBLE);
        		imgBtnSearch.setVisibility(View.VISIBLE);
        	}
        	
        	
        	imgViewSeparator3.setVisibility(View.GONE);
			imgBtnToolBox.setVisibility(View.GONE);
        	
        	//ini QuickAction buat LogIn
            quickActionLogin = new QuickActionLogin(this, QuickActionLogin.NOT_LOGGED_IN);
            quickActionLogin.addActionItem();
            quickActionLogin.setOnDismissListener(new QuickActionLogin.OnDismissListener() {
    			
    			@Override
    			public void onDismiss() {
    				// TODO Auto-generated method stub
    				
    			}
    		});
        }
        //ini saat sudah / masih login
        else {
        	imgViewSeparator3.setVisibility(View.VISIBLE);
			imgBtnToolBox.setVisibility(View.VISIBLE);
			
        	//ini QuickAction buat LogIn
            quickActionLogin = new QuickActionLogin(this, QuickActionLogin.LOGGED_IN);
            quickActionLogin.addActionItem();
            quickActionLogin.setOnDismissListener(new QuickActionLogin.OnDismissListener() {
    			
    			@Override
    			public void onDismiss() {
    				// TODO Auto-generated method stub
    				
    			}
    		});
        }
		
		//checkEnableGPS(); 
		checkInetConnection();
		
		
		//tes database
		dbHelper = new DatabaseHelper(this);
		database = dbHelper.getWritableDatabase();
		
		//mencoba untuk mendapatkan data yg sudah tersimpan hasil dari request Wakuwakuw API sebelumnya & menampilkannya di Timeline
		retrieveFromDatabase();		
	}
	
	
	//saat tombol Back ditekan
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Back", Toast.LENGTH_SHORT).show();
		//berfungsi men-null-kan semua adapter saat aplikasi exit
		if (donlodTaskEvents != null || donlodTaskMeetups != null || donlodTaskCommuns != null) {
			donlodTaskEvents.cancel(true); donlodTaskMeetups.cancel(true); donlodTaskCommuns.cancel(true);
			adapter = null; adapter2 = null; customAdapterCommun = null;
		}
		
		if (ChangeCity.donlodTaskEvents != null || ChangeCity.donlodTaskMeetups != null || ChangeCity.donlodTaskCommuns != null) {
			ChangeCity.donlodTaskEvents.cancel(true); ChangeCity.donlodTaskMeetups.cancel(true); ChangeCity.donlodTaskCommuns.cancel(true);
			adapter = null; adapter2 = null; customAdapterCommun = null;
		}
		
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString("city_id", cityId);
        prefsEditor.putString("city_name", cityName);
        prefsEditor.putString("country_name", countryName);
        prefsEditor.commit();
		
		super.onBackPressed();
	}
	
	
	//event saat tombol hardware option menu dipencet
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_timeline, menu);
		return true;
	}
	
	//event saat item dari tombol option dipilih
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		
		if (item.getItemId() == R.id.jumpToTop) {
			//membuat posisi fokus pada ListView berada di paling atas 
			listViewCategory.setSelection(0);

			//testing notification
			notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			myNotification = new Notification(R.drawable.icon, "Notification!", System.currentTimeMillis());
			Context context = getApplicationContext();
			String notificationTitle = "Exercise of Notification!";
			String notificationText = "http://android-er.blogspot.com/";
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
			PendingIntent pendingIntent = PendingIntent.getActivity(Timeline.this, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
				   
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
			
			return true;
		}
		else if (item.getItemId() == R.id.refreshTimeline) {
			//merefresh kembali Timeline
			connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
			wifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    mobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    
		    if (wifi.isConnected() || mobile.isConnected()) {
		    	if (cityName == null || cityName.equalsIgnoreCase("")) {
		    		//Toast.makeText(getApplicationContext(), "No City", Toast.LENGTH_LONG).show();
		    		Toast.makeText(getApplicationContext(), "Can't reload the timeline.\nMake sure to select the city first.", Toast.LENGTH_LONG).show();
		    	}
		    	else {
		    		Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_LONG).show();
		    		
		    		eventsLoadFinished = false; meetupsLoadFinished = false; communsLoadFinished = false;
		    		progressBarTimeline.setVisibility(View.VISIBLE);
		    		retrieveEverything(cityName, isiUsername, isiPassword);
		    	}
		    }
		    else {
		    	Toast.makeText(getApplicationContext(), "There is no connection.", Toast.LENGTH_LONG).show();
		    }
			
			return true;
		}
		else if (item.getItemId() == R.id.viewNearMeNow) {
			//melihat Event / Meetup disekitar posisi user saat itu
			AlertDialog.Builder optionViewNearMe = new AlertDialog.Builder(Timeline.this);
			//optionViewNearMe.setView(layout);
			//optionViewNearMe.setTitle("Option");
			optionViewNearMe.setItems(pilihanNearMe, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int position) {
					// TODO Auto-generated method stub
					if (position == 0) {
						PetaNearMeNow.mapStatusSelected = "event";
						
						Intent in = new Intent(Timeline.this, PetaNearMeNow.class);
						startActivity(in);
					}
					else {
						PetaNearMeNow.mapStatusSelected = "meetup";
						
						Intent in = new Intent(Timeline.this, PetaNearMeNow.class);
						startActivity(in);
					}
				}
			});
			
			AlertDialog alertViewNearMe = optionViewNearMe.create();
			alertViewNearMe.show();
		}
		
		return false;
	}
	
	
	//method untuk mencek koneksi internet apakah aplikasi terhubung pada WiFi, mobile network atau tidak
	public void checkInetConnection() {
		connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		wifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    mobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    
	    //proses pencekan koneksi dimulai disini
	    //jika tidak terhubung sama sekali entah itu WiFi ataupun mobile, maka Dialogbox akan muncul
	    //untuk memastikan koneksi tersedia
	    if (!wifi.isConnected() && !mobile.isConnected()) {
	    	AlertDialog.Builder inetEnableDialog = new AlertDialog.Builder(this);
			inetEnableDialog.setTitle("Internet Connection Problem");
			inetEnableDialog.setMessage("It seems that you are not connected to WiFi or Mobile network.\n\n" +
					"Do you want to enable one?");
			inetEnableDialog.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent in = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
					startActivity(in);
					
					checkEnableGPS();
				}
			});
			
			inetEnableDialog.setNegativeButton("No!", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					checkEnableGPS();
				}
			});
			
			inetEnableDialog.show();
	    } 
	    else {
	    	//mungkin langsung request REST APInya
	    	//initLocationManager();
	    	
	    	//langsung menjalankan method ini, jika koneksi tersedia
	    	checkEnableGPS();
	    }
	}
	
	
	//method untuk pengecekan apakah user telah mengaktifkan GPSnya atau tidak
	public void checkEnableGPS() {
		//another alternative way (from Vogella)
		/*
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to go to the settings
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		*/

		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
		//proses pencekan GPS
		if (!provider.equals("") && !provider.equals("network")) {
			//GPSnya udah hidup, jadi lakukan pencarian lokasi disini
			//Toast.makeText(getApplicationContext(), "GPS Enabled: " + provider, Toast.LENGTH_LONG).show();
			//checkInetConnection();
			
			if (wifi.isConnected() || mobile.isConnected()) {
				initLocation();
			}
			else {
				//initLocationManager();
			}
		}
		else {
			//confirm apa GPSnya mau dinyalain atau tidak
			AlertDialog.Builder gpsEnableDialog = new AlertDialog.Builder(this);
			gpsEnableDialog.setTitle("GPS Problem");
			gpsEnableDialog.setMessage("We can't determine your current location so we can't load the timeline based on your location.\n\n" +
					"Do you want to enable the GPS?");
			gpsEnableDialog.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Intent in = new Intent(Settings.ACTION_SECURITY_SETTINGS);
					
					//proses pencarian lokasi berdasarkan koordinat GPS dimulai, saat ada koneksi internet
					Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(in);
					
					if (wifi.isConnected() || mobile.isConnected()) {
						initLocation();
					}
					else {
						//initLocationManager();
					}
					
					//checkInetConnection();
				}
			});
			
			gpsEnableDialog.setNegativeButton("No!", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//checkInetConnection();
					
					//proses pencarian lokasi berdasarkan kota yg user pilih pada Wakuwakuw website dimulai, 
					//saat ada koneksi internet. Jika kota yg user pilih adalah Surabaya, maka otomatis akan dipilih.
					if (wifi.isConnected() || mobile.isConnected()) {
						if (!isiUsername.equals("") || !isiPassword.equals("")) {
							progressBarTimeline.setVisibility(View.VISIBLE);
							
							String URLUser = "http://api.wakuwakuw.com/rest/users/?username=" + URLEncoder.encode(isiUsername.toString());
							XMLParser parserUser = new XMLParser();
							String xmlUser = parserUser.getXmlFromUrl(URLUser, isiUsername, isiPassword);
							Document docUser = parserUser.getDomElement(xmlUser);
							NodeList nlUser = docUser.getElementsByTagName(DATA);
							if (nlUser.getLength() != 0) {
								Element elUser = (Element) nlUser.item(0);
								
								idUser = parserUser.getValue(elUser, ID_USER);
								namaUser = parserUser.getValue(elUser, NAME_USER);
								cityIdUser = parserUser.getValue(elUser, CITY_ID_USER);
								cityId = cityIdUser;
								
								String URLCityUser = "http://api.wakuwakuw.com/rest/cities/?id=" + cityIdUser;
								XMLParser parserCityUser = new XMLParser();
								String xmlCityUser = parserCityUser.getXmlFromUrl(URLCityUser, isiUsername, isiPassword);
								Document docCityUser = parserCityUser.getDomElement(xmlCityUser);
								NodeList nlCityUser = docCityUser.getElementsByTagName(DATUM);
								Element elCityUser = (Element) nlCityUser.item(0);
								
								String cityNameUser = parserCityUser.getValue(elCityUser, "name");
								
								latitudeOverall = parserCityUser.getValue(elCityUser, "lat");
								longitudeOverall = parserCityUser.getValue(elCityUser, "lng");
								
								logoUser = LoadImageFromWeb("http://wakuwakuw.com/img/user/" + idUser + "?size=feed");
								
								retrieveEverything(cityNameUser, isiUsername, isiPassword);
							}
							else {
								//
							}
						}
						else {
							
						}
					}
					else {
						//initLocationManager();
					}
				}
			});
			
			gpsEnableDialog.show();
		}
	}

	//method untuk inisialisasi lokasi berdasarkan koordinat GPS
	public void initLocation() {
		progressBarTimeline.setVisibility(View.VISIBLE);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				//pointUser = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
				//latUser = lat; longUser = lng;
				
				if (latUser == 0 && longUser == 0) {
					
					latUser = lat; longUser = lng;
					latitudeOverall = Double.toString(latUser); longitudeOverall = Double.toString(longUser);
					
					//Toast.makeText(getApplicationContext(), Double.toString(latUser) + ", " + Double.toString(longUser), Toast.LENGTH_SHORT).show();
					
					//link URL untuk mengambil nama kota, koordinat, dsb.
					String URL_Get_CityName = "http://maps.google.com/maps/geo?q=" + 
						latUser + "," + longUser + "&output=xml&oe=utf8&sensor=true";
			
					//proses parsing untuk mendapatkan nama kota setelah koordinat GPS (latitude & longitude) didapatkan
					XMLParser parser = new XMLParser();
					//String xml = parser.getXmlFromUrl(URL_Get_CityName, isiUsername, isiPassword);
					String xml = parser.getXmlFromUrlMap(URL_Get_CityName);
					
					if (xml != null) {
						Document doc = parser.getDomElement(xml);
						NodeList nl = doc.getElementsByTagName(RESPONSE);
						
						if (nl.getLength() != 0) {
							for (int i = 0; i < nl.getLength(); i++) {
								Element e = (Element) nl.item(i);
								
								administrativeAreaName = parser.getValue(e, ADMINISTRATIVE_AREA_NAME);
								localityName = parser.getValue(e, LOCALITY_NAME);
								
							}
							
							if (localityName == null || localityName.equals("")) {
								if (administrativeAreaName.contains("Jakarta")) {
									administrativeAreaName = "Jakarta";
									cityNameFromGPS = administrativeAreaName;
									//Toast.makeText(getApplicationContext(), administrativeAreaName, Toast.LENGTH_SHORT).show();
								}
							}
							else {
								cityNameFromGPS = localityName;
								//Toast.makeText(getApplicationContext(), "Ini untuk locality name: " + localityName, Toast.LENGTH_SHORT).show();
							}
							
							
							Toast.makeText(Timeline.this, "Your Position Is Locked.\nPlease Wait While Retrieving Data.", Toast.LENGTH_SHORT).show();
							
							//ini pake method bantu
							//retrieveEverything(cityNameFromGPS.replaceAll(" ", "%20"), isiUsername, isiPassword);
							retrieveEverything(URLEncoder.encode(cityNameFromGPS.toString()), isiUsername, isiPassword);
						}
						else {
							//seandainya user berada di wilayah antah berantah.. :D
							administrativeAreaName = ""; localityName = "";
							progressBarTimeline.setVisibility(View.GONE);
						}
						
						//progressBarTimeline.setVisibility(View.GONE);
					}
					else {
						//tak ada koneksi
						Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
					} //*/
				}
				
				//Toast.makeText(getApplicationContext(), Double.toString(latUser) + ", " + Double.toString(longUser), Toast.LENGTH_LONG).show();
			}
		};
		
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	
	//method untuk mendapatkan daftar item terbaru, entah itu Event, Meetup, ataupun Community
	public void retrieveEverything(String theCityName, String isiUsername, String isiPassword) {
		//////////////////////////
		//link dari Wakuwakuw untuk daftar nama kota
		Url_City = "http://api.wakuwakuw.com/rest/cities/?name=" + theCityName;
		
		//proses parsing
		XMLParser parser = new XMLParser();
		xmlCity = parser.getXmlFromUrl(Url_City, isiUsername, isiPassword);
		
		if (xmlCity != null || !xml.equals("")) {
			Document docCity = parser.getDomElement(xmlCity);
			NodeList nlCity = docCity.getElementsByTagName(DATUM);
			
			if (nlCity.getLength() == 0) {
				adapter = null; adapter2 = null; customAdapterCommun = null;
				
				Timeline.listViewCategory.setAdapter(null);
				Timeline.txtCityTimeline.setText("--------------------------");
				cityName = "";
				
				Toast.makeText(Timeline.this, "City Not Found.", Toast.LENGTH_LONG).show();
				progressBarTimeline.setVisibility(View.GONE);
			}
			else if (nlCity.getLength() >= 1) {
				//ngambil nama kota (city)
				Element el = (Element) nlCity.item(0);
				// adding each child node to HashMap key => value
				cityId = parser.getValue(el, CITY_ID);
				cityName = parser.getValue(el, CITY_NAME);
				countryId = parser.getValue(el, COUNTRY_ID);
				
				//link url untuk mengambil nama negara (country)
				Url_Country = "http://api.wakuwakuw.com/rest/countries/?id=" + countryId;
				
				XMLParser parserCountry = new XMLParser();
				xmlCountry = parserCountry.getXmlFromUrl(Url_Country, isiUsername, isiPassword);
				Document docCountry = parserCountry.getDomElement(xmlCountry);
				NodeList nlCountry = docCountry.getElementsByTagName(DATA);
				Element elCountry = (Element) nlCountry.item(0);
				countryName = parserCountry.getValue(elCountry, COUNTRY_NAME);
				//sampai disini (ngambil nama negara)
				
				txtCityTimeline.setText(cityName + ", " + countryName);
				imgViewSeparator2.setVisibility(View.VISIBLE);
        		imgBtnChangeCategory.setVisibility(View.VISIBLE);
        		imgViewSeparatorUnder2.setVisibility(View.VISIBLE);
        		imgBtnSearch.setVisibility(View.VISIBLE);
				
				//ini buat ngambil tanggal sekarang
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
				
				donlodTaskEvents = new DownloadTaskEvent();
				donlodTaskEvents.execute(URL_Events);
				donlodTaskMeetups = new DownloadTaskMeetup();
				donlodTaskMeetups.execute(URL_Meetups);
				donlodTaskCommuns = new DownloadTaskCommun();
				donlodTaskCommuns.execute(URL_Communs);
			}
			
		}
		else {
			//adapter = null; adapter2 = null; customAdapterCommun = null;
			//Timeline.txtCityTimeline.setText("-------------------------------------");
			
			Toast.makeText(Timeline.this, "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		//finish();
		//sampai sini/////////////////////
	}
	
	
	
	
	//retrieve data melalui AsyncTask (sesuai dengan namanya masing-masing: Event, Meetup & Community///////
	public class DownloadTaskEvent extends AsyncTask<String, Void, SeparatedListAdapter> {
		
		protected SeparatedListAdapter doInBackground(String... URL_Events) {
			// TODO Auto-generated method stub
			//return null;
			
			if (donlodTaskEvents.isCancelled()) {
                //break;
				adapter = null;
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
				
				isiJmlhMember = new ArrayList<String>();
				isiJmlhComment = new ArrayList<String>();
				isiJmlhLike = new ArrayList<String>();
				
				adapter = new SeparatedListAdapter(Timeline.this);
				
				XMLParser parser = new XMLParser();
				xml = parser.getXmlFromUrl(URL_Events[0], isiUsername, isiPassword);
				Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						eventId = parser.getValue(e, ID);
						isiLogo.add(LoadImageFromWeb("http://wakuwakuw.com/img/event/" + eventId + "?size=stream"));
						
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
						String xmlEventCategory = parser2.getXmlFromUrl(URLEventCategoryID, isiUsername, isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenis.add(parser2.getValue(ele, CATEGORY_NAME));
						//////////////////////////////////////////////////
						
						//ini buat ngambil jumlah member, comment & like
						String URLEventStats = "http://api.wakuwakuw.com/rest/event_stats/?event_id=" + eventId;
						XMLParser parserEventStats = new XMLParser();
						String xmlEventStats = parserEventStats.getXmlFromUrl(URLEventStats, isiUsername, isiPassword);
						Document docEventStats = parserEventStats.getDomElement(xmlEventStats);
						NodeList nlEventStats = docEventStats.getElementsByTagName(DATA);
						if (nlEventStats.getLength() != 0) {
							Element elEventStats = (Element) nlEventStats.item(0);
							
							isiJmlhMember.add(parserEventStats.getValue(elEventStats, TOTAL_GUESTS));
							isiJmlhComment.add(parserEventStats.getValue(elEventStats, TOTAL_COMMENTS));
							isiJmlhLike.add(parserEventStats.getValue(elEventStats, TOTAL_LIKES));
						}
						else {
							isiJmlhMember.add("0"); isiJmlhComment.add("0"); isiJmlhLike.add("0"); 
						}
						///////////////////////////////////////////////
						
						isiLinkURL.add(parser.getValue(e, SLUG));
						isiKoorLat.add(parser.getValue(e, LATITUDE));
						isiKoorLong.add(parser.getValue(e, LONGITUDE));
					}
					
					//proses mengurutkan item sesuai dgn tanggalnya (sort ascending)
					dateArranger.arrangeDate(Timeline.this, adapter, isiId, isiIdLogo, isiLogo, isiJudul, isiPenjelasan, isiAlamat, 
							isiTanggalStart, isiTanggalEnd, isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMember, isiJmlhComment, isiJmlhLike,
							isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, 
							isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump,
							customAdapter, "event");
				}
				else {
					adapter = null;
					//Timeline.listViewCategory.setAdapter(null);
				}
				////////////////////////////////////////////
				
				eventsLoadFinished = true;
			}
			
			return adapter;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskEvents.isCancelled()) {
                //break;
				adapter = null;
            }
			//super.onProgressUpdate(values);
		}
		
		protected void onPostExecute(SeparatedListAdapter adapter) {
			// TODO Auto-generated method stub
			//ini buat menset nama kota (jika ada) dan memberitahukannya (dgn Toast)
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(Timeline.this, "Events Loading Finished", Toast.LENGTH_SHORT).show();
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.event)) {
				if (adapter == null) {
					//Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			//super.onPostExecute(result);
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished) {
				Timeline.progressBarTimeline.setVisibility(View.GONE);
				insertToDatabase();
			}
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			adapter = null;
			super.onCancelled();
		}
	}
	
	public class DownloadTaskMeetup extends AsyncTask<String, Void, SeparatedListAdapter> {
		
		protected SeparatedListAdapter doInBackground(String... URL_Meetups) {
			// TODO Auto-generated method stub
			//return null;
			
			if (donlodTaskMeetups.isCancelled()) {
                //break;
				adapter2 = null;
            }
			else {
				//ini buat meetup
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
				
				adapter2 = new SeparatedListAdapter(Timeline.this);
				
				XMLParser parser = new XMLParser();
				xml = parser.getXmlFromUrl(URL_Meetups[0], isiUsername, isiPassword);
				Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						meetupId = parser.getValue(e, COMMUNITY_ID);
						isiLogoMeetup.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + meetupId + "?size=stream"));
						
						//isiLogo.add(getResources().getDrawable(R.drawable.meetups));
						//isiLogoMeetup.add(null);
						isiIdMeetup.add(parser.getValue(e, ID));
						isiIdLogoMeetup.add(meetupId);
						isiJudulMeetup.add(parser.getValue(e, TITLE));
						isiPenjelasanMeetup.add(parser.getValue(e, DESCRIPTION));
						isiAlamatMeetup.add(parser.getValue(e, LOCATION));
						
						Date time = new Date();
						time.setTime(Long.parseLong(parser.getValue(e, TIME_START)) * 1000);
						isiTanggalMeetupStart.add(time.toLocaleString());
						
						Date timeEnd = new Date();
						timeEnd.setTime(Long.parseLong(parser.getValue(e, TIME_END)) * 1000);
						isiTanggalMeetupEnd.add(timeEnd.toLocaleString());
						//isiTanggal.add(parser.getValue(e, TIME_START));
						
						//ini buat ngambil nama kategori (untuk meetup)
						communityCategoryID = parser.getValue(e, COMMUNITY_CATEGORY_ID);
						String URLCommunityCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + communityCategoryID;
						XMLParser parser2 = new XMLParser();
						String xmlEventCategory = parser2.getXmlFromUrl(URLCommunityCategoryID, isiUsername, isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenisMeetup.add(parser2.getValue(ele, CATEGORY_NAME));
						///////////////////////////////////////////////
						
						//ini buat ngambil jumlah member, comment & like
						String URLMeetupStats = "http://api.wakuwakuw.com/rest/meetup_stats/?meetup_id=" + isiIdMeetup.get(i);
						XMLParser parserMeetupStats = new XMLParser();
						String xmlMeetupStats = parserMeetupStats.getXmlFromUrl(URLMeetupStats, isiUsername, isiPassword);
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
						
						isiLinkURLMeetup.add(parser.getValue(e, SLUG));
						isiKoorLatMeetup.add(parser.getValue(e, LATITUDE));
						isiKoorLongMeetup.add(parser.getValue(e, LONGITUDE));
					}
					
					//proses mengurutkan item sesuai dgn tanggalnya (sort ascending)
					dateArranger.arrangeDate(Timeline.this, adapter2, isiIdMeetup, isiIdLogoMeetup, isiLogoMeetup, isiJudulMeetup, isiPenjelasanMeetup, isiAlamatMeetup, 
							isiTanggalMeetupStart, isiTanggalMeetupEnd, isiJenisMeetup, isiLinkURLMeetup, isiKoorLatMeetup, isiKoorLongMeetup, 
							isiJmlhMemberMeetup, isiJmlhCommentMeetup, isiJmlhLikeMeetup,
							isiIdKumpMeetup, isiIdLogoKumpMeetup, isiLogoKumpMeetup, isiJudulKumpMeetup, isiPenjelasanKumpMeetup, isiAlamatKumpMeetup, 
							isiTanggalKumpMeetup, isiTanggalKumpMeetupEnd, isiJenisKumpMeetup, isiLinkURLKumpMeetup, isiKoorLatKumpMeetup, isiKoorLongKumpMeetup, 
							isiJmlhMemberKumpMeetup, isiJmlhCommentKumpMeetup, isiJmlhLikeKumpMeetup,
							customAdapter, "meetup");
				}
				else {
					//Timeline.listMeetups = new ListView(ChangeCity.this);
					adapter2 = null;
					//Timeline.listViewCategory.setAdapter(null);
				}	
				////////////////////////////////////
				
				meetupsLoadFinished = true;
			}
			
			return adapter2;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskMeetups.isCancelled()) {
                //break;
				adapter2 = null;
            }
			super.onProgressUpdate(values);
		}
		
		protected void onPostExecute(SeparatedListAdapter adapter) {
			// TODO Auto-generated method stub
			//ini buat menset nama kota (jika ada) dan memberitahukannya (dgn Toast)
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(Timeline.this, "Meetups Loading Finished", Toast.LENGTH_SHORT).show();
			
			//Timeline.imgBtnChangeCategory.setImageResource(R.drawable.event);
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.meetups)) {
				if (adapter == null) {
					//Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			//finish();
			//super.onPostExecute(result);
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished) {
				Timeline.progressBarTimeline.setVisibility(View.GONE);
				insertToDatabase();
			}
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			adapter2 = null;
			super.onCancelled();
		}
	}
	
	public class DownloadTaskCommun extends AsyncTask<String, Void, CustomAdapterCommun> {

		@Override
		protected CustomAdapterCommun doInBackground(String... URL_Communs) {
			// TODO Auto-generated method stub
			if (donlodTaskCommuns.isCancelled()) {
                //break;
				customAdapterCommun = null;
            }
			else {
				//untuk community
				isiIdCommun = new ArrayList<String>();
				isiLogoCommun = new ArrayList<Drawable>();
				isiNamaCommun = new ArrayList<String>();
				isiJenisCommun = new ArrayList<String>();
				isiDeskripsiCommun = new ArrayList<String>();
				isiLinkURLCommun = new ArrayList<String>();
				
				isiJmlhMemberCommun = new ArrayList<String>();
				isiJmlhCommentCommun = new ArrayList<String>();
				isiJmlhLikeCommun = new ArrayList<String>();
				
				XMLParser parser = new XMLParser();
				xml = parser.getXmlFromUrl(URL_Communs[0], isiUsername, isiPassword);
				Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					//percobaan kacau
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						communityId = parser.getValue(e, ID);
						isiIdCommun.add(communityId);
						isiLogoCommun.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + communityId + "?size=stream"));
						//Timeline.isiLogoCommun.add(getResources().getDrawable(R.drawable.communities));
						isiNamaCommun.add(parser.getValue(e, NAME_COMMUN));
						
						//ini buat ngambil nama kategori (untuk community)
						communityCategoryID = parser.getValue(e, COMMUNITY_CATEGORY_ID);
						String URLCommunityCategoryID = "http://api.wakuwakuw.com/rest/community_categories/" + communityCategoryID;
						XMLParser parser2 = new XMLParser();
						String xmlEventCategory = parser2.getXmlFromUrl(URLCommunityCategoryID, isiUsername, isiPassword);
						Document doc2 = parser2.getDomElement(xmlEventCategory);
						NodeList nl2 = doc2.getElementsByTagName(DATA);
						Element ele = (Element) nl2.item(0);
						isiJenisCommun.add(parser2.getValue(ele, CATEGORY_NAME));
						///////////////////////////////////////////////
						
						//ini buat ngambil jumlah member, comment & like
						String URLCommunityStats = "http://api.wakuwakuw.com/rest/community_stats/?community_id=" + communityId;
						XMLParser parserCommunStats = new XMLParser();
						String xmlCommunStats = parserCommunStats.getXmlFromUrl(URLCommunityStats, isiUsername, isiPassword);
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
					
					customAdapterCommun = new CustomAdapterCommun(Timeline.this, isiIdCommun, isiLogoCommun, isiNamaCommun, 
							isiJenisCommun, isiDeskripsiCommun, isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun);
				}
				else {
					customAdapterCommun = null;
					//Timeline.listViewCategory.setAdapter(null);
				}
				//////////////////////////////////////////
				
				communsLoadFinished = true;
			}
			
			return customAdapterCommun;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskCommuns.isCancelled()) {
                //break;
				customAdapterCommun = null;
            }
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(CustomAdapterCommun adapter) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			//Timeline.txtCityTimeline.setText(cityName);
			Toast.makeText(Timeline.this, "Communities Loading Finished", Toast.LENGTH_SHORT).show();
			
			//Timeline.imgBtnChangeCategory.setImageResource(R.drawable.event);
			
			if (Timeline.imgBtnChangeCategory.getTag().equals(R.drawable.communities)) {
				if (adapter == null) {
					//Timeline.listViewCategory.setBackgroundResource(R.drawable.background_none);
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				else {
					Timeline.listViewCategory.setBackgroundDrawable(null);
					Timeline.listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
				}
				
				Timeline.listViewCategory.setAdapter(adapter);
			}
			
			if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished) {
				Timeline.progressBarTimeline.setVisibility(View.GONE);
				insertToDatabase();
			}
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			customAdapterCommun = null;
			super.onCancelled();
		}
	}
	//sampai sini (untuk AsyncTask)

	
	
	//cara ngeload image dari link URL yang diberikan
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
	
	//mengubah Image (Drawable) menjadi byte[]
	private byte[] convertImageToByte(Drawable drawable) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] logo = baos.toByteArray();
		
		return logo;
	}
	
	//method untuk membantu menyimpan data hasil request dari Wakuwakuw API ke database SQLite
	//sehingga seolah-olah aplikasi menyimpan cache
	public void insertToDatabase() {
		//untuk menset / menyimpan cityId, cityName dan countryName
		//hanya dimasukkan sebagai SharedPreference, bukan ke database SQLite
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString("city_id", cityId);
        prefsEditor.putString("city_name", cityName);
        prefsEditor.putString("country_name", countryName);
        prefsEditor.commit();
        
		//untuk event
		database.delete(DatabaseHelper.TABLE_EVENT, null, null);
		for (int i=0; i<isiId.size(); i++) {
			//membentuk Content Value untuk dapat disimpan ke dalam database SQLite
			cvEvent = new ContentValues();
			
			//proses memasukkan nilai ke tiap field pada table
			cvEvent.put(DatabaseHelper.COLUMN_LOGO, convertImageToByte(isiLogo.get(i)));
			cvEvent.put(DatabaseHelper.COLUMN_ID, isiId.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_ID_LOGO, isiIdLogo.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TITLE, isiJudul.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_DESCRIPTION, isiPenjelasan.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_LOCATION, isiAlamat.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TIME_START, isiTanggalStart.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TIME_END, isiTanggalEnd.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_CATEGORY_NAME, isiJenis.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_LAT, isiKoorLat.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_LNG, isiKoorLong.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_SLUG, isiLinkURL.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TOTAL_GUESTS, isiJmlhMember.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TOTAL_COMMENTS, isiJmlhComment.get(i));
			cvEvent.put(DatabaseHelper.COLUMN_TOTAL_LIKES, isiJmlhLike.get(i));
			
			//mengeksekusi perintah Insert sehingga data nilai pada content value tadi dimasukkan ke dalam tabel
			database.insert(DatabaseHelper.TABLE_EVENT, null, cvEvent);
		}
		
		//untuk meetup
		database.delete(DatabaseHelper.TABLE_MEETUP, null, null);
		for(int i=0; i<isiIdMeetup.size(); i++) {
			//membentuk Content Value untuk dapat disimpan ke dalam database SQLite
			cvMeetup = new ContentValues();
			
			//proses memasukkan nilai ke tiap field pada table
			cvMeetup.put(DatabaseHelper.COLUMN_LOGO, convertImageToByte(isiLogoMeetup.get(i)));
			cvMeetup.put(DatabaseHelper.COLUMN_ID, isiIdMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_ID_LOGO, isiIdLogoMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TITLE, isiJudulMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_DESCRIPTION, isiPenjelasanMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_LOCATION, isiAlamatMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TIME_START, isiTanggalMeetupStart.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TIME_END, isiTanggalMeetupEnd.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_CATEGORY_NAME, isiJenisMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_LAT, isiKoorLatMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_LNG, isiKoorLongMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_SLUG, isiLinkURLMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TOTAL_GUESTS, isiJmlhMemberMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TOTAL_COMMENTS, isiJmlhCommentMeetup.get(i));
			cvMeetup.put(DatabaseHelper.COLUMN_TOTAL_LIKES, isiJmlhLikeMeetup.get(i));
			
			//mengeksekusi perintah Insert sehingga data nilai pada content value tadi dimasukkan ke dalam tabel
			database.insert(DatabaseHelper.TABLE_MEETUP, null, cvMeetup);
		}
		
		//untuk communtiy
		database.delete(DatabaseHelper.TABLE_COMMUNITY, null, null);
		for (int i=0; i<isiIdCommun.size(); i++) {
			//membentuk Content Value untuk dapat disimpan ke dalam database SQLite
			cvCommun = new ContentValues();
			
			//proses memasukkan nilai ke tiap field pada table
			cvCommun.put(DatabaseHelper.COLUMN_LOGO, convertImageToByte(isiLogoCommun.get(i)));
			cvCommun.put(DatabaseHelper.COLUMN_ID, isiIdCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_NAME, isiNamaCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_DESCRIPTION, isiDeskripsiCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_CATEGORY_NAME, isiJenisCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_LINK_URL, isiLinkURLCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_TOTAL_MEMBERS, isiJmlhMemberCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_TOTAL_COMMENTS, isiJmlhCommentCommun.get(i));
			cvCommun.put(DatabaseHelper.COLUMN_TOTAL_LIKES, isiJmlhLikeCommun.get(i));
			
			//mengeksekusi perintah Insert sehingga data nilai pada content value tadi dimasukkan ke dalam tabel
			database.insert(DatabaseHelper.TABLE_COMMUNITY, null, cvCommun);
		}
	}
	
	public void retrieveFromDatabase() {
		//untuk Event
		String perintahSQLEvent = "SELECT * FROM event";
		
		isiLogo = dbHelper.getImageFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_LOGO);
		isiId = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_ID);
		isiIdLogo = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_ID_LOGO);
		isiJudul = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TITLE);
		isiPenjelasan = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_DESCRIPTION);
		isiAlamat = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_LOCATION);
		isiTanggalStart = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TIME_START);
		isiTanggalEnd = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TIME_END);
		isiJenis = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_CATEGORY_NAME);
		isiLinkURL = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_SLUG);
		isiKoorLat = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_LAT);
		isiKoorLong = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_LNG);
		isiJmlhMember = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TOTAL_GUESTS);
		isiJmlhComment = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TOTAL_COMMENTS);
		isiJmlhLike = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLEvent, DatabaseHelper.COLUMN_TOTAL_LIKES);
		
		if (isiId.size() > 0) {
			adapter = new SeparatedListAdapter(Timeline.this);
			//proses mengurutkan item sesuai dgn tanggalnya (sort ascending)
			dateArranger.arrangeDate(Timeline.this, adapter, isiId, isiIdLogo, isiLogo, isiJudul, isiPenjelasan, isiAlamat, 
					isiTanggalStart, isiTanggalEnd, isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMember, isiJmlhComment, isiJmlhLike,
					isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, 
					isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump,
					customAdapter, "event");
		}
		else {
			adapter = null;
		}
		
		
		//untuk Meetup
		String perintahSQLMeetup = "SELECT * FROM meetup";
		
		isiLogoMeetup = dbHelper.getImageFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_LOGO);
		isiIdMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_ID);
		isiIdLogoMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_ID_LOGO);
		isiJudulMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TITLE);
		isiPenjelasanMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_DESCRIPTION);
		isiAlamatMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_LOCATION);
		isiTanggalMeetupStart = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TIME_START);
		isiTanggalMeetupEnd = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TIME_END);
		isiJenisMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_CATEGORY_NAME);
		isiLinkURLMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_SLUG);
		isiKoorLatMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_LAT);
		isiKoorLongMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_LNG);
		isiJmlhMemberMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TOTAL_GUESTS);
		isiJmlhCommentMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TOTAL_COMMENTS);
		isiJmlhLikeMeetup = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLMeetup, DatabaseHelper.COLUMN_TOTAL_LIKES);
		
		if (isiIdMeetup.size() > 0) {
			adapter2 = new SeparatedListAdapter(Timeline.this);
			//proses mengurutkan item sesuai dgn tanggalnya (sort ascending)
			dateArranger.arrangeDate(Timeline.this, adapter2, isiIdMeetup, isiIdLogoMeetup, isiLogoMeetup, isiJudulMeetup, isiPenjelasanMeetup, isiAlamatMeetup, 
					isiTanggalMeetupStart, isiTanggalMeetupEnd, isiJenisMeetup, isiLinkURLMeetup, isiKoorLatMeetup, isiKoorLongMeetup, 
					isiJmlhMemberMeetup, isiJmlhCommentMeetup, isiJmlhLikeMeetup,
					isiIdKumpMeetup, isiIdLogoKumpMeetup, isiLogoKumpMeetup, isiJudulKumpMeetup, isiPenjelasanKumpMeetup, isiAlamatKumpMeetup, 
					isiTanggalKumpMeetup, isiTanggalKumpMeetupEnd, isiJenisKumpMeetup, isiLinkURLKumpMeetup, isiKoorLatKumpMeetup, isiKoorLongKumpMeetup, 
					isiJmlhMemberKumpMeetup, isiJmlhCommentKumpMeetup, isiJmlhLikeKumpMeetup,
					customAdapter, "meetup");
		}
		else {
			adapter2 = null;
		}
		
		
		//untuk Community
		String perintahSQLCommun = "SELECT * FROM community";
		
		isiLogoCommun = dbHelper.getImageFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_LOGO);
		isiIdCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_ID);
		isiNamaCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_NAME);
		isiJenisCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_CATEGORY_NAME);
		isiDeskripsiCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_DESCRIPTION);
		isiLinkURLCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_LINK_URL);
		isiJmlhMemberCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_TOTAL_MEMBERS);
		isiJmlhCommentCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_TOTAL_COMMENTS);
		isiJmlhLikeCommun = dbHelper.getItemFromDatabase(Timeline.this, database, perintahSQLCommun, DatabaseHelper.COLUMN_TOTAL_LIKES);
		
		if (isiIdCommun.size() > 0) {
			customAdapterCommun = new CustomAdapterCommun(Timeline.this, isiIdCommun, isiLogoCommun, isiNamaCommun, 
				isiJenisCommun, isiDeskripsiCommun, isiLinkURLCommun, isiJmlhMemberCommun, isiJmlhCommentCommun, isiJmlhLikeCommun);
		}
		else {
			customAdapter = null;
		}
		
		
		listViewCategory.setBackgroundColor(Color.parseColor("#EBEBEB"));
		listViewCategory.setAdapter(adapter);
	}
}
