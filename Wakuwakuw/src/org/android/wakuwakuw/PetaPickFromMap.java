package org.android.wakuwakuw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PetaPickFromMap extends MapActivity {

	private final String RESPONSE = "Response";
	private final String POINT = "Point";
	private final String COORDINATES = "coordinates";
	private final String ADDRESS = "address";
	
	public static int statusPickFromMap;
	
	private ArrayAdapter<String> adapterGoogleAPI;
	
	public static String finalLatPoint, finalLongPoint, finalAddressName;
	
	private AutoCompleteTextView autoFindPlacePickFromMap;
	private Button btnSearchPlacePickFromMap, btnConfirmPickFromMap, btnCancelPickFromMap;
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> listOverlays;
	private Drawable drawable;
	private MarkerPetaPickFromMap penandaPeta;
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		setContentView(R.layout.map_pick_from_map);
		
		//ini autocomplete + Google API
		adapterGoogleAPI = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		adapterGoogleAPI.setNotifyOnChange(true);
		
		autoFindPlacePickFromMap = (AutoCompleteTextView)findViewById(R.id.autoFindPlacePickFromMap);
		autoFindPlacePickFromMap.setAdapter(adapterGoogleAPI);
		autoFindPlacePickFromMap.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (count%3 == 1) {
					adapterGoogleAPI.clear();
					
					try {
						URL googlePlaces = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ URLEncoder.encode(s.toString(), "UTF-8") +
								"&types=geocode&language=id&sensor=true&key=AIzaSyD4oU92SAmeteV4oD7OaeHklpXIE10vb2M");
						// URLEncoder.encode(url,"UTF-8");
						URLConnection tc = googlePlaces.openConnection();
						Log.d("GottaGo ", URLEncoder.encode(s.toString()));
						
						BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
						String line;
				        StringBuffer sb = new StringBuffer();
				        while ((line = in.readLine()) != null) {
				        	sb.append(line);
				        }
				        
				        JSONObject predictions = new JSONObject(sb.toString());            
				        JSONArray ja = new JSONArray(predictions.getString("predictions"));

				            for (int i = 0; i < ja.length(); i++) {
				                JSONObject jo = (JSONObject) ja.get(i);
				                adapterGoogleAPI.add(jo.getString("description"));
				            }
					}
					catch (MalformedURLException e) {
						// TODO Auto-generated catch block
				        e.printStackTrace();
				    } 
					catch (IOException e) {
				        // TODO Auto-generated catch block
				        e.printStackTrace();
				    } 
					catch (JSONException e) {
				        // TODO Auto-generated catch block
				        e.printStackTrace();
				    }
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		btnSearchPlacePickFromMap = (Button)findViewById(R.id.buttonSearchPlacePickFromMap);
		btnSearchPlacePickFromMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (autoFindPlacePickFromMap.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please input the place.", Toast.LENGTH_SHORT).show();
				}
				else {
					String thePlace = autoFindPlacePickFromMap.getText().toString();
					
					String longInput = ""; String latInput = "";
					
					String URL_Get_Coordinate = "http://maps.google.com/maps/geo?q=" + 
						URLEncoder.encode(thePlace.toString()) + "&output=xml&oe=utf8&sensor=true";
					
					XMLParser parser = new XMLParser();
					String xml = parser.getXmlFromUrl(URL_Get_Coordinate, Timeline.isiUsername, Timeline.isiPassword);
					
					if (xml != null) {
						Document doc = parser.getDomElement(xml);
						NodeList nl = doc.getElementsByTagName(POINT);
						
						if (nl.getLength() != 0) {
							for (int i = 0; i < nl.getLength(); i++) {
								Element e = (Element) nl.item(i);
								String fullCoordinate = parser.getValue(e, COORDINATES);
								
								String longLatCoordinate = fullCoordinate.substring(0, fullCoordinate.length()-2);
								longInput = longLatCoordinate.substring(0, longLatCoordinate.indexOf(","));
								latInput = longLatCoordinate.substring(longLatCoordinate.indexOf(",")+1, longLatCoordinate.length());
							}
							
							finalLatPoint = latInput; finalLongPoint = longInput;
							
							GeoPoint point = new GeoPoint((int)(Float.parseFloat(latInput) * 1E6), (int)(Float.parseFloat(longInput) * 1E6));
					        OverlayItem overlayitem = new OverlayItem(point, null, null);
					        mapController.animateTo(point);
					        
					        penandaPeta.removeAllOverlay();
					        penandaPeta.addOverlay(overlayitem);
					        listOverlays.add(penandaPeta);
							
							//Toast.makeText(getApplicationContext(), fullCoordinate, Toast.LENGTH_SHORT).show();
							//Toast.makeText(getApplicationContext(), longInput + " and " + latInput, Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(getApplicationContext(), "Not Found.", Toast.LENGTH_SHORT).show();
							latInput = "0.0000000"; longInput = "0.0000000";
							finalLatPoint = latInput; finalLongPoint = longInput;
						}
						
					}
					else {
						Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
						latInput = "0.0000000"; longInput = "0.0000000";
						finalLatPoint = latInput; finalLongPoint = longInput;
					}
				}
			}
		});
		
		btnConfirmPickFromMap = (Button)findViewById(R.id.buttonConfirmPickFromMap);
		btnConfirmPickFromMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), finalLatPoint + ", " + finalLongPoint, Toast.LENGTH_SHORT).show();
				
				finalAddressName = getTheAddressName(finalLatPoint, finalLongPoint);
				Toast.makeText(getApplicationContext(), finalAddressName, Toast.LENGTH_SHORT).show();
				
				if (statusPickFromMap == 1) {
					FormCreateNewEvent.autoCompleteEventPlaceDetail.setText(finalAddressName);
				}
				else if (statusPickFromMap == 2) {
					FormCreateNewMeetup.autoCompleteMeetupPlaceDetail.setText(finalAddressName);
				}
				
				finish();
			}
		});
		
		btnCancelPickFromMap = (Button)findViewById(R.id.buttonCancelPickFromMap);
		btnCancelPickFromMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		//finalLatPoint = "-7.2491665"; finalLongPoint = "112.7508316";
		if ((Timeline.latitudeOverall == null || Timeline.longitudeOverall == null) || 
				(Timeline.latitudeOverall.equals("") || Timeline.longitudeOverall.equals(""))) {
			finalLatPoint = "-7.2491665"; finalLongPoint = "112.7508316";
		}
		else {
			finalLatPoint = Timeline.latitudeOverall; finalLongPoint = Timeline.longitudeOverall;
		}
		
		
		mapView = (MapView)findViewById(R.id.mapPickFromMap);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
        mapController.setZoom(18);
        
        GeoPoint point = new GeoPoint((int)(Float.parseFloat(finalLatPoint) * 1E6), (int)(Float.parseFloat(finalLongPoint) * 1E6));
        OverlayItem overlayitem = new OverlayItem(point, null, null);
        mapController.animateTo(point);
        
        listOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.arrow);
        penandaPeta = new MarkerPetaPickFromMap(drawable, this);
        
        penandaPeta.addOverlay(overlayitem);
        listOverlays.add(penandaPeta);
        
        mapView.invalidate();
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private String getTheAddressName(String latInput, String longInput) {
		//untuk mendapatkan nama tempat dari koordinat
		//String thePlace = autoFindPlacePickFromMap.getText().toString();
		
		String addressName = "";
		String URL_Get_CityName = "http://maps.google.com/maps/geo?q=" + latInput + "," + longInput + "&output=xml&oe=utf8&sensor=true";
		
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL_Get_CityName, Timeline.isiUsername, Timeline.isiPassword);
		
		if (xml != null) {
			Document doc = parser.getDomElement(xml);
			NodeList nl = doc.getElementsByTagName(RESPONSE);
			
			if (nl.getLength() != 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					addressName = parser.getValue(e, ADDRESS);
				}
				
				//Toast.makeText(getApplicationContext(), addressName, Toast.LENGTH_LONG).show();
			}
			else {
				//Toast.makeText(getApplicationContext(), addressName, Toast.LENGTH_SHORT).show();
				//latInput = "0.0000000"; longInput = "0.0000000";
			}
			
		}
		else {
			//Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		return addressName;
		////////////////////////////////////
	}

}
