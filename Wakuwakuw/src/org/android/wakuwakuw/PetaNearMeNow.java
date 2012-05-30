package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.CATEGORY_NAME;
import static org.android.wakuwakuw.Variabel.CITY_ID;
import static org.android.wakuwakuw.Variabel.CITY_NAME;
import static org.android.wakuwakuw.Variabel.COUNTRY_ID;
import static org.android.wakuwakuw.Variabel.COUNTRY_NAME;
import static org.android.wakuwakuw.Variabel.DATA;
import static org.android.wakuwakuw.Variabel.DATUM;
import static org.android.wakuwakuw.Variabel.DESCRIPTION;
import static org.android.wakuwakuw.Variabel.EVENT_CATEGORY_ID;
import static org.android.wakuwakuw.Variabel.ID;
import static org.android.wakuwakuw.Variabel.LATITUDE;
import static org.android.wakuwakuw.Variabel.LOCATION;
import static org.android.wakuwakuw.Variabel.LONGITUDE;
import static org.android.wakuwakuw.Variabel.SLUG;
import static org.android.wakuwakuw.Variabel.TIME_END;
import static org.android.wakuwakuw.Variabel.TIME_START;
import static org.android.wakuwakuw.Variabel.TITLE;
import static org.android.wakuwakuw.Variabel.TOTAL_COMMENTS;
import static org.android.wakuwakuw.Variabel.TOTAL_GUESTS;
import static org.android.wakuwakuw.Variabel.TOTAL_LIKES;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PetaNearMeNow extends MapActivity {

	public MapView mapView;
	public MapController mapController;
	public List<Overlay> listOverlays;
	public Drawable drawableMe, drawableLocation;
	public MarkerPeta markerMe;
	public MarkerPeta[] markerEvent;
	
	private ConnectivityManager connect;
	private NetworkInfo wifi, mobile;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latUser, longUser;
	
	private final String RESPONSE = "Response";
	private final String ADMINISTRATIVE_AREA_NAME = "AdministrativeAreaName";
	private final String LOCALITY_NAME = "LocalityName";
	
	private String administrativeAreaName, localityName;
	public String cityName, cityNameFromGPS, latitudeOverall, longitudeOverall;
	
	private Date waktuSkrng;
	private long bilWaktuSkrng;
	
	private String Url_City, URL_Events, URL_Meetups, xml, xmlCity;
	
	private ArrayList<String> isiId, isiIdLogo, isiJudul, isiPenjelasan, isiAlamat, isiTanggalStart, isiTanggalEnd, 
		isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMemberEvent, isiJmlhCommentEvent, isiJmlhLikeEvent;
	
	//status yang menunjukkan apakah peta near me untuk event ataukah meetup yg akan ditampilkan
	public static String mapStatusSelected;
	
	private DownloadTaskNearMe donlodTaskNearMe;
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.map_near_me_now);
		
		mapView = (MapView) findViewById(R.id.mapNearMeNow);
        mapView.setBuiltInZoomControls(true);
        
        mapController = mapView.getController();
        mapController.setZoom(15);
        
        listOverlays = mapView.getOverlays();
        drawableMe = this.getResources().getDrawable(R.drawable.me);
        
        if (mapStatusSelected.equals("event")) {
        	drawableLocation = this.getResources().getDrawable(R.drawable.event);
        }
        else {
        	drawableLocation = this.getResources().getDrawable(R.drawable.meetups);
        }
        
        markerMe = new MarkerPeta(drawableMe, this);
        
        checkInetConnection();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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
				}
			});
			
			gpsEnableDialog.show();
		}
	}
	
	//method untuk inisialisasi lokasi berdasarkan koordinat GPS
	public void initLocation() {
		//progressBarTimeline.setVisibility(View.VISIBLE);
		
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
					//menampilkan marker posisi user saat ini (current location)
					GeoPoint pointUser = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));	        
			        mapController.animateTo(pointUser);
			        
			        OverlayItem overlayitem = new OverlayItem(pointUser, "Info", "This is your current location");
			        markerMe.addOverlay(overlayitem);  
			        listOverlays.add(markerMe);
			        ///////////////////////////////////
					
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
							
							
							Toast.makeText(PetaNearMeNow.this, "Your Position Is Locked.\nPlease Wait While Retrieving Data.", Toast.LENGTH_SHORT).show();
							
							//ini pake method bantu
							//retrieveEverything(cityNameFromGPS.replaceAll(" ", "%20"), isiUsername, isiPassword);
							retrieveEverything(URLEncoder.encode(cityNameFromGPS.toString()), Timeline.isiUsername, Timeline.isiPassword, mapStatusSelected);
						}
						else {
							//seandainya user berada di wilayah antah berantah.. :D
							administrativeAreaName = ""; localityName = "";
						}
					}
					else {
						//tak ada koneksi
						Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
					} //*/
				}
			}
		};
		
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	
	//method untuk mendapatkan daftar item terbaru, entah itu Event, Meetup, ataupun Community
	public void retrieveEverything(String theCityName, String isiUsername, String isiPassword, String mapStatSelected) {
		//////////////////////////
		//Url_City = "http://api.wakuwakuw.com/rest/cities/?name=" + cityNameFromGPS.replaceAll(" ", "%20");
		//link dari Wakuwakuw untuk daftar nama kota
		Url_City = "http://api.wakuwakuw.com/rest/cities/?name=" + theCityName;
		
		//proses parsing
		XMLParser parser = new XMLParser();
		xmlCity = parser.getXmlFromUrl(Url_City, isiUsername, isiPassword);
		
		if (xmlCity != null || !xml.equals("")) {
			Document docCity = parser.getDomElement(xmlCity);
			NodeList nlCity = docCity.getElementsByTagName(DATUM);
			
			if (nlCity.getLength() == 0) {
				//adapter = null; adapter2 = null; customAdapterCommun = null;
				
				Timeline.listViewCategory.setAdapter(null);
				Timeline.txtCityTimeline.setText("--------------------------");
				cityName = "";
				
				Toast.makeText(PetaNearMeNow.this, "City Not Found.", Toast.LENGTH_LONG).show();
				//progressBarTimeline.setVisibility(View.GONE);
			}
			else if (nlCity.getLength() >= 1) {
				//Timeline.progressBarTimeline.setVisibility(View.VISIBLE);
				//statusKota = "Finished..";
				
				
				//ngambil nama kota (city)
				Element el = (Element) nlCity.item(0);
				// adding each child node to HashMap key => value
				Timeline.cityId = parser.getValue(el, CITY_ID);
				cityName = parser.getValue(el, CITY_NAME);
				
				
				
				//ini buat ngambil tanggal sekarang
				waktuSkrng = new Date();
				bilWaktuSkrng = waktuSkrng.getTime() / 1000;
				///////////////////////////////////
				
				if (mapStatSelected.equals("event")) {
					URL_Events = "http://api.wakuwakuw.com/rest/events/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng)+ "&order_by=time_start&ascending=true&take=20";
					
					donlodTaskNearMe = new DownloadTaskNearMe();
					donlodTaskNearMe.execute(URL_Events);
				}
				else if (mapStatSelected.equals("meetup")) {
					URL_Meetups = "http://api.wakuwakuw.com/rest/meetups/?city_id=" + Timeline.cityId + "&time_end%3E=" + Long.toString(bilWaktuSkrng)+ "&order_by=time_start&ascending=true&take=20";
					
					donlodTaskNearMe = new DownloadTaskNearMe();
					donlodTaskNearMe.execute(URL_Meetups);
				}
			}
			
		}
		else {
			//adapter = null; adapter2 = null; customAdapterCommun = null;
			
			Toast.makeText(PetaNearMeNow.this, "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		//finish();
		//sampai sini/////////////////////
	}
	
	
	//retrieve data melalui AsyncTask (sesuai dengan namanya masing-masing: Event, Meetup & Community///////
	public class DownloadTaskNearMe extends AsyncTask<String, Void, Void> {
		
		protected Void doInBackground(String... URL_Events) {
			// TODO Auto-generated method stub
			//return null;
			
			//ini buat events//
			/*isiId = new ArrayList<String>();
			isiIdLogo = new ArrayList<String>();
			isiLogo = new ArrayList<Drawable>();
			isiJudul = new ArrayList<String>();
			isiPenjelasan = new ArrayList<String>();
			isiAlamat = new ArrayList<String>();
			isiTanggalStart = new ArrayList<String>();
			isiTanggalEnd = new ArrayList<String>();
			isiJenis = new ArrayList<String>();
			isiLinkURL = new ArrayList<String>();*/
			
			isiJudul = new ArrayList<String>();
			isiAlamat = new ArrayList<String>();
			isiKoorLat = new ArrayList<String>();
			isiKoorLong = new ArrayList<String>();
			
			//adapter = new SeparatedListAdapter(Timeline.this);
			
			XMLParser parser = new XMLParser();
			xml = parser.getXmlFromUrl(URL_Events[0], Timeline.isiUsername, Timeline.isiPassword);
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				//percobaan kacau
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					//eventId = parser.getValue(e, ID);
					//isiLogo.add(LoadImageFromWeb("http://wakuwakuw.com/img/event/" + eventId + "?size=stream"));
					
					/*isiId.add(eventId);
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
						
						isiJmlhMemberEvent.add(parserEventStats.getValue(elEventStats, TOTAL_GUESTS));
						isiJmlhCommentEvent.add(parserEventStats.getValue(elEventStats, TOTAL_COMMENTS));
						isiJmlhLikeEvent.add(parserEventStats.getValue(elEventStats, TOTAL_LIKES));
					}
					else {
						isiJmlhMemberEvent.add("0"); isiJmlhCommentEvent.add("0"); isiJmlhLikeEvent.add("0"); 
					}
					///////////////////////////////////////////////
					
					isiLinkURL.add(parser.getValue(e, SLUG));*/
					
					isiJudul.add(parser.getValue(e, TITLE));
					isiAlamat.add(parser.getValue(e, LOCATION));
					isiKoorLat.add(parser.getValue(e, LATITUDE));
					isiKoorLong.add(parser.getValue(e, LONGITUDE));
				}
				
				//proses mengurutkan item sesuai dgn tanggalnya (sort ascending)
				/*dateArranger.arrangeDate(PetaNearMeNow.this, adapter, isiId, isiIdLogo, isiLogo, isiJudul, isiPenjelasan, isiAlamat, 
						isiTanggalStart, isiTanggalEnd, isiJenis, isiLinkURL, isiKoorLat, isiKoorLong, isiJmlhMemberEvent, isiJmlhCommentEvent, isiJmlhLikeEvent,
						isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKump, isiTanggalKumpEnd, isiJenisKump, 
						isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, isiJmlhMemberKumpEvent, isiJmlhCommentKumpEvent, isiJmlhLikeKumpEvent,
						customAdapter, "event");*/
			}
			else {
				//Timeline.listEvents = new ListView(ChangeCity.this);
				//adapter = null;
				//Timeline.listViewCategory.setAdapter(null);
			}
			////////////////////////////////////////////
				
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			markerEvent = new MarkerPeta[isiKoorLat.size()];
			
			for (int i=0; i<isiKoorLat.size(); i++) {
				//jika koordinat lat & long valid
				if ((!isiKoorLat.get(i).equalsIgnoreCase("") || !isiKoorLat.get(i).equalsIgnoreCase("")) && 
						//(isiKoorLat.get(i) != null || isiKoorLat.get(i) != null) ||
						(!isiKoorLat.get(i).equalsIgnoreCase("0.0000000") || !isiKoorLong.get(i).equalsIgnoreCase("0.0000000"))) {
					markerEvent[i] = new MarkerPeta(drawableLocation, PetaNearMeNow.this);
					
					GeoPoint point = new GeoPoint((int)(Float.parseFloat(isiKoorLat.get(i)) * 1E6), 
			        		(int)(Float.parseFloat(isiKoorLong.get(i)) * 1E6));
			        OverlayItem overlayitem = new OverlayItem(point, "Info", isiJudul.get(i) + "\n\n" + isiAlamat.get(i));
			        //mapController.animateTo(point);
			        
			        markerEvent[i].addOverlay(overlayitem);  
			        listOverlays.add(markerEvent[i]);
				}
				//jika koordinat lat & long tidak valid
				else {
					
				}
			}
	        
	        mapView.invalidate();
	        
	        Toast.makeText(PetaNearMeNow.this, "Finished", Toast.LENGTH_SHORT).show();
		}
	}
}
