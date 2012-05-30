package org.android.wakuwakuw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.android.wakuwakuw.FormCreateNewEvent.DownloadTaskCommunityList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class FormCreateNewMeetup extends Activity {
	
	private final String RESPONSE = "Response";
	private final String POINT = "Point";
	private final String COORDINATES = "coordinates";
	
	private final String DATA = "data";
	private final String DATUM = "datum";
	private final String COMMUNITY_ID = "community_id";
	private final String NAME = "name";
	
	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 2;
	
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmapFromSDCard;
	
	private Calendar calendar;
	private int theYearStart, theMonthStart, theDayStart, theHourStart, theMinuteStart,
		theYearEnd, theMonthEnd, theDayEnd, theHourEnd, theMinuteEnd;
	private int dateIndicator, timeIndicator;
	
	private String theFullCoordinate, theLongLatCoordinate, latInput, longInput;
	
	private boolean isSpecifyTime;
	
	private ArrayAdapter<String> adapterSelectCommunity, adapterGoogleAPI, adapterWakuwakuwCitiesAPI;
	private ArrayList<String> communityList;
	
	private RelativeLayout containerMeetupTimeEnd, containerMeetupPickFromMap;
	private ImageView imgCalendarMeetupDateHeldStart, imgTimeMeetupDateHeldStart, imgCalendarMeetupDateHeldEnd, imgTimeMeetupDateHeldEnd,
				imgMeetupPickFromMap, imgPickFromSDCard;
	private EditText editMeetupTitle, editMeetupDescription, editMeetupDateHeldStart, editMeetupTimeHeldStart, 
				editMeetupDateHeldEnd, editMeetupTimeHeldEnd;
	public static AutoCompleteTextView autoCompleteMeetupPlaceDetail, autoCompleteMeetupPlaceCity;
	private Spinner spnSelectCommunity, spnRoutineMeetupType;
	private CheckBox chkSpecifyEndTime, chkMakeAsRoutineMeetup;
	private Button btnPickFromSDCard, btnNextCreateNewMeetup;
	
	private ProgressDialog progressDialog;
	
	public static DownloadTaskCommunityList donlodTaskCommunityList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_create_new_meetup);
		
		PetaPickFromMap.statusPickFromMap = 2;
		
		calendar = Calendar.getInstance();
		/*
		theYear = calendar.get(Calendar.YEAR);
		theMonth = calendar.get(Calendar.MONTH);
		theDay = calendar.get(Calendar.DAY_OF_MONTH);

		theHour = calendar.get(Calendar.HOUR_OF_DAY);
		theMinute = calendar.get(Calendar.MINUTE);
		*/
		
		isSpecifyTime = false;
		
		
		containerMeetupTimeEnd = (RelativeLayout)findViewById(R.id.containerMeetupTimeEnd);
		containerMeetupPickFromMap = (RelativeLayout)findViewById(R.id.containerMeetupPickFromMap);
		
		spnRoutineMeetupType = (Spinner)findViewById(R.id.spnRoutineMeetupType);
		spnSelectCommunity = (Spinner)findViewById(R.id.spnMeetupSelectCommunity);
		
		communityList = new ArrayList<String>();
		/*communityList.add("1"); communityList.add("2"); communityList.add("3"); communityList.add("4");
		
		adapterSelectCommunity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, communityList);
		adapterSelectCommunity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spnSelectCommunity.setAdapter(adapterSelectCommunity);*/
		
		
		editMeetupTitle = (EditText)findViewById(R.id.editMeetupTitle);
		editMeetupDescription = (EditText)findViewById(R.id.editMeetupDescription);
		editMeetupDateHeldStart = (EditText)findViewById(R.id.editMeetupDateHeldStart);
		editMeetupTimeHeldStart = (EditText)findViewById(R.id.editMeetupTimeHeldStart);
		editMeetupDateHeldEnd = (EditText)findViewById(R.id.editMeetupDateHeldEnd);
		editMeetupTimeHeldEnd = (EditText)findViewById(R.id.editMeetupTimeHeldEnd);
		
		
		//ini autocomplete + Google API
		adapterGoogleAPI = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		adapterGoogleAPI.setNotifyOnChange(true);
		
		autoCompleteMeetupPlaceDetail = (AutoCompleteTextView)findViewById(R.id.autoCompleteMeetupPlaceDetail);
		autoCompleteMeetupPlaceDetail.setAdapter(adapterGoogleAPI);
		autoCompleteMeetupPlaceDetail.addTextChangedListener(new TextWatcher() {
			
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
		
		
		//ini autocomplete + Wakuwakuw city API
		adapterWakuwakuwCitiesAPI = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		adapterWakuwakuwCitiesAPI.setNotifyOnChange(true);
		
		autoCompleteMeetupPlaceCity = (AutoCompleteTextView)findViewById(R.id.autoCompleteMeetupPlaceCity);
		autoCompleteMeetupPlaceCity.setText(Timeline.txtCityTimeline.getText().toString());
		autoCompleteMeetupPlaceCity.setAdapter(adapterWakuwakuwCitiesAPI);
		autoCompleteMeetupPlaceCity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (count%3 == 1) {
					adapterWakuwakuwCitiesAPI.clear();
					
					try {
						URL wakuwakuwCities = new URL("http://api.wakuwakuw.com/rest/cities/json?name=" 
								+ URLEncoder.encode(s.toString(), "UTF-8") + "&take=5");
						// URLEncoder.encode(url,"UTF-8");
						URLConnection tc = wakuwakuwCities.openConnection();
						Log.d("Wakuwakuw city ", URLEncoder.encode(s.toString()));
						
						BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
						String line;
				        StringBuffer sb = new StringBuffer();
				        while ((line = in.readLine()) != null) {
				        	sb.append(line);
				        }
				        
				        JSONObject predictions = new JSONObject(sb.toString());            
				        JSONArray ja = new JSONArray(predictions.getString("data"));

				            for (int i = 0; i < ja.length(); i++) {
				            	//ini yg sama nge-load nama negara juga
				            	JSONObject jo = (JSONObject) ja.get(i);
				                
				                String countryId = jo.get("country_id").toString();
				                URL wakuwakuwCountry = new URL("http://api.wakuwakuw.com/rest/countries/json?id=" + countryId);
				                URLConnection tcCountry = wakuwakuwCountry.openConnection();
				                
				                BufferedReader inCountry = new BufferedReader(new InputStreamReader(tcCountry.getInputStream()));
								String lineCountry;
						        StringBuffer sbCountry = new StringBuffer();
						        while ((lineCountry = inCountry.readLine()) != null) {
						        	sbCountry.append(lineCountry);
						        }
						        
						        JSONObject predictionsCountry = new JSONObject(sbCountry.toString());            
						        JSONArray jaCountry = new JSONArray(predictionsCountry.getString("data"));
						        JSONObject joCountry = (JSONObject) jaCountry.get(0);
						        String countryName = joCountry.getString("name").toString();
				                
						        adapterWakuwakuwCitiesAPI.add(jo.getString("name") + ", " + countryName);
				            	
						        
						        //ini yg default
				                //JSONObject jo = (JSONObject) ja.get(i);
				                //adapterWakuwakuwCitiesAPI.add(jo.getString("name"));
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
		
		
		
		chkSpecifyEndTime = (CheckBox)findViewById(R.id.chkMeetupSpecifyEndTime);
		chkSpecifyEndTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chkSpecifyEndTime.isChecked()) {
					isSpecifyTime = true;
					containerMeetupTimeEnd.setVisibility(View.VISIBLE);
				}
				else {
					isSpecifyTime = false;
					containerMeetupTimeEnd.setVisibility(View.GONE);
				}
			}
		});
		
		chkMakeAsRoutineMeetup = (CheckBox)findViewById(R.id.chkMakeAsRoutineMeetup);
		chkMakeAsRoutineMeetup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chkMakeAsRoutineMeetup.isChecked()) {
					spnRoutineMeetupType.setVisibility(View.VISIBLE);
				}
				else {
					spnRoutineMeetupType.setVisibility(View.GONE);
				}
			}
		});
		
		
		
		//time start
		imgCalendarMeetupDateHeldStart = (ImageView)findViewById(R.id.imgCalendarMeetupDateHeldStart);
		imgCalendarMeetupDateHeldStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateIndicator = 1;
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		imgTimeMeetupDateHeldStart = (ImageView)findViewById(R.id.imgTimeMeetupDateHeldStart);
		imgTimeMeetupDateHeldStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeIndicator = 1;
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		
		//time end
		imgCalendarMeetupDateHeldEnd = (ImageView)findViewById(R.id.imgCalendarMeetupDateHeldEnd);
		imgCalendarMeetupDateHeldEnd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateIndicator = 2;
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		imgTimeMeetupDateHeldEnd = (ImageView)findViewById(R.id.imgTimeMeetupDateHeldEnd);
		imgTimeMeetupDateHeldEnd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeIndicator = 2;
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		
		imgMeetupPickFromMap = (ImageView)findViewById(R.id.imgViewMeetupPickFromMap);
		imgMeetupPickFromMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(FormCreateNewMeetup.this, PetaPickFromMap.class);
				startActivity(in);
			}
		});
		
		
		imgPickFromSDCard = (ImageView)findViewById(R.id.imgViewPickFromSDCardMeetup);
		
		
		btnPickFromSDCard = (Button)findViewById(R.id.buttonPickImageMeetup);
		btnPickFromSDCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pickImage();
			}
		});
		
		
		btnNextCreateNewMeetup = (Button)findViewById(R.id.buttonNextCreateNewMeetup);
		//btnNextCreateNewMeetup.getBackground().setColorFilter(0x710089E1, Mode.MULTIPLY);
		btnNextCreateNewMeetup.getBackground().setColorFilter(Color.argb(255, 0, 137, 225), Mode.MULTIPLY);
		btnNextCreateNewMeetup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), spnRoutineMeetupType.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
				
				//saat Time End diaktifkan
				if (chkSpecifyEndTime.isChecked()) {
					if (spnSelectCommunity.getSelectedItem() == null || editMeetupTitle.getText().toString().equals("") ||
							editMeetupDateHeldStart.getText().toString().equals("") || editMeetupTimeHeldStart.getText().toString().equals("") ||
							editMeetupDateHeldEnd.getText().toString().equals("") || editMeetupTimeHeldEnd.getText().toString().equals("") ||
							autoCompleteMeetupPlaceCity.getText().toString().equals("") || autoCompleteMeetupPlaceDetail.getText().toString().equals("")) {
						
						Toast.makeText(getApplicationContext(), "Please fill the mandatory field.", Toast.LENGTH_LONG).show();
					}
					else {
						getThePlaceCoordinate();
						
						Calendar calStart = new GregorianCalendar();
						calStart.set(theYearStart, theMonthStart, theDayStart, theHourStart, theMinuteStart, 0);
						long timeStart = calStart.getTime().getTime();
						Toast.makeText(getApplicationContext(), Long.toString(timeStart), Toast.LENGTH_LONG).show();
						
						Calendar calEnd = new GregorianCalendar();
						calEnd.set(theYearEnd, theMonthEnd, theDayEnd, theHourEnd, theMinuteEnd, 0);
						long timeEnd = calEnd.getTime().getTime();
						
						//Toast.makeText(getApplicationContext(), theHourStart + ", " + theMinuteStart, Toast.LENGTH_LONG).show();
						//Toast.makeText(getApplicationContext(), theYearStart + ", " + theMonthStart + ", " + theDayStart, Toast.LENGTH_LONG).show();
					}
				}
				//saat Time End tidak diaktifkan
				else {
					if (spnSelectCommunity.getSelectedItem() == null || editMeetupTitle.getText().toString().equals("") ||
							editMeetupDateHeldStart.getText().toString().equals("") || editMeetupTimeHeldStart.getText().toString().equals("") ||
							autoCompleteMeetupPlaceCity.getText().toString().equals("") || autoCompleteMeetupPlaceDetail.getText().toString().equals("")) {
						
						Toast.makeText(getApplicationContext(), "Please fill the mandatory field.", Toast.LENGTH_LONG).show();
					}
					else {
						getThePlaceCoordinate();
						
						Calendar calStart = new GregorianCalendar();
						calStart.set(theYearStart, theMonthStart, theDayStart, theHourStart, theMinuteStart, 0);
						long timeStart = calStart.getTime().getTime();
						Toast.makeText(getApplicationContext(), Long.toString(timeStart), Toast.LENGTH_LONG).show();
						
						
						//consider about the time end
						Calendar calEnd = new GregorianCalendar();
						calEnd.set(theYearEnd, theMonthEnd, theDayEnd, theHourEnd, theMinuteEnd, 0);
						long timeEnd = calEnd.getTime().getTime();
						
						//Toast.makeText(getApplicationContext(), theHourStart + ", " + theMinuteStart, Toast.LENGTH_LONG).show();
						//Toast.makeText(getApplicationContext(), theYearStart + ", " + theMonthStart + ", " + theDayStart, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		
		progressDialog = ProgressDialog.show(this, null, "Please Wait!");
		
		donlodTaskCommunityList = new DownloadTaskCommunityList();
		donlodTaskCommunityList.execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		//return super.onCreateDialog(id);
		
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
		
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			if (dateIndicator == 1) {
				theYearStart = year; theMonthStart = monthOfYear; theDayStart = dayOfMonth;
				editMeetupDateHeldStart.setText(new StringBuilder().append(theDayStart)
						   .append("/").append(theMonthStart+1).append("/").append(theYearStart));
			}
			else if (dateIndicator == 2) {
				theYearEnd = year; theMonthEnd = monthOfYear; theDayEnd = dayOfMonth;
				editMeetupDateHeldEnd.setText(new StringBuilder().append(theDayEnd)
						   .append("/").append(theMonthEnd+1).append("/").append(theYearEnd));
			}
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if (timeIndicator == 1) {
				//editEventTimeHeldStart.setText(new StringBuilder().append(theHour).append(":").append(theMinute)
				//		.append(" ").append(AM_PM));
				theHourStart = hourOfDay; theMinuteStart = minute;
				
				String myMinute = Integer.toString(minute);
				if (myMinute.length() < 2) {
					myMinute = "0" + Integer.toString(minute);
				}
				
				
				if (hourOfDay > 12) {
					editMeetupTimeHeldStart.setText(new StringBuilder().append(hourOfDay-12).append(":").append(myMinute).append(" pm"));
				}
				else if (hourOfDay == 12) {
					editMeetupTimeHeldStart.setText(new StringBuilder().append(hourOfDay).append(":").append(myMinute).append(" pm"));
				}
				else if (hourOfDay < 12) {
					editMeetupTimeHeldStart.setText(new StringBuilder().append(hourOfDay).append(":").append(myMinute).append(" am"));
				}
			}
			else if (timeIndicator == 2) {
				//editEventTimeHeldEnd.setText(new StringBuilder().append(theHour).append(":").append(theMinute)
				//		.append(" ").append(AM_PM));
				theHourEnd = hourOfDay; theMinuteEnd = minute;
				
				String myMinute = Integer.toString(minute);
				if (myMinute.length() < 2) {
					myMinute = "0" + Integer.toString(minute);
				}
				
				
				if (hourOfDay > 12) {
					editMeetupTimeHeldEnd.setText(new StringBuilder().append(hourOfDay-12).append(":").append(myMinute).append(" pm"));
				}
				else if (hourOfDay == 12) {
					editMeetupTimeHeldEnd.setText(new StringBuilder().append(hourOfDay).append(":").append(myMinute).append(" pm"));
				}
				else if (hourOfDay < 12) {
					editMeetupTimeHeldEnd.setText(new StringBuilder().append(hourOfDay).append(":").append(myMinute).append(" am"));
				}
			}
		}
	};
	
	
	public void pickImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		InputStream stream = null;
		
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
			try {
				// We need to recyle unused bitmaps
				if (bitmapFromSDCard != null) {
					bitmapFromSDCard.recycle();
				}
				stream = getContentResolver().openInputStream(data.getData());
				bitmapFromSDCard = BitmapFactory.decodeStream(stream);

				imgPickFromSDCard.setImageBitmap(bitmapFromSDCard);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void getThePlaceCoordinate() {
		//untuk mendapatkan koordinat dari autocomplete
		String thePlace = autoCompleteMeetupPlaceDetail.getText().toString();
		
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
					theFullCoordinate = parser.getValue(e, COORDINATES);
					
					theLongLatCoordinate = theFullCoordinate.substring(0, theFullCoordinate.length()-2);
					longInput = theLongLatCoordinate.substring(0, theLongLatCoordinate.indexOf(","));
					latInput = theLongLatCoordinate.substring(theLongLatCoordinate.indexOf(",")+1, theLongLatCoordinate.length());
				}
				
				Toast.makeText(getApplicationContext(), theFullCoordinate, Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), longInput + " and " + latInput, Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(getApplicationContext(), "Gak ada", Toast.LENGTH_SHORT).show();
				latInput = "0.0000000"; longInput = "0.0000000";
			}
			
		}
		else {
			Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
		}
		////////////////////////////////////
	}
	
	
	public class DownloadTaskCommunityList extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String URL_Commun_Member = "http://api.wakuwakuw.com/rest/community_members/?user_id=" + Timeline.idUser;
			
			XMLParser parserCommunMember = new XMLParser();
			String xmlCommunMember = parserCommunMember.getXmlFromUrl(URL_Commun_Member, Timeline.isiUsername, Timeline.isiPassword);
			Document docCommunMember = parserCommunMember.getDomElement(xmlCommunMember.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			
			NodeList nlCommunMember = docCommunMember.getElementsByTagName(DATUM);
			
			if (nlCommunMember.getLength() != 0) {
				for (int i = 0; i < nlCommunMember.getLength(); i++) {
					Element elCommunMember = (Element) nlCommunMember.item(i);
					// adding each child node to HashMap key => value
					String communityId = parserCommunMember.getValue(elCommunMember, COMMUNITY_ID);
					
					
					//ini buat ngambil nama kategori (untuk community)
					String URL_Community = "http://api.wakuwakuw.com/rest/communities/?id=" + communityId;
					XMLParser parserCommunity = new XMLParser();
					String xmlCommunity = parserCommunity.getXmlFromUrl(URL_Community, Timeline.isiUsername, Timeline.isiPassword);
					Document docCommunity = parserCommunity.getDomElement(xmlCommunity);
					NodeList nlCommunity = docCommunity.getElementsByTagName("data");
					Element elCommunity = (Element) nlCommunity.item(0);
					communityList.add(parserCommunity.getValue(elCommunity, NAME));
					///////////////////////////////////////////////
				}
				
			}
			else {
				//adapterSelectCommunity = null;
				//communityList = null;
			}
			
			return communityList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			adapterSelectCommunity = new ArrayAdapter<String>(FormCreateNewMeetup.this, android.R.layout.simple_spinner_item, result);
			adapterSelectCommunity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spnSelectCommunity.setAdapter(adapterSelectCommunity);
		}
	}
}
