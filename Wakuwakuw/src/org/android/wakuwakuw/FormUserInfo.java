package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FormUserInfo extends Activity {
	
	private Button btnViewCommunity;
	private ImageButton imgBtnBack, imgBtnOption;
	private ImageView imgUserPhoto, imgSeparator;
	private TextView txtUserCity, txtUserExplanation;
	private ProgressDialog progressDialog;
	
	private ArrayList<Drawable> isiLogoCommun;
	private ArrayList<String> isiIdCommun, isiNamaCommun;
	
	private CustomAdapterCustomDialog customAdapter;
	
	public static String statusOpenedFrom, additionalParameter;
	private Drawable userPhoto;
	private String userId, userName, userEmail, city, gender, genderLabel, birthday, age;
	private int birthDay, birthMonth, birthYear, totalCommunity;
	private String linkUserProfile;
	
	private String pilihanOption[] = {"Edit Profile", "Edit Photo"};
	
	public DownloadTaskUserInfo donlodTaskUserInfo;
	public DownloadTaskCommunityList donlodTaskCommunList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_user_info);
		
		imgSeparator = (ImageView)findViewById(R.id.imgViewSeparatorUserDetail2);
		imgUserPhoto = (ImageView)findViewById(R.id.imgDepanUserProfile);
		
		txtUserCity = (TextView)findViewById(R.id.txtUserCity);
		txtUserExplanation = (TextView)findViewById(R.id.txtUserExplanation);
		
		imgBtnBack = (ImageButton)findViewById(R.id.imgButtonBack);
		imgBtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		imgBtnOption = (ImageButton)findViewById(R.id.imgButtonOptionUserProfile);
		imgBtnOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder optionUserProfile = new AlertDialog.Builder(FormUserInfo.this);
				//optionUserProfile.setView(layout);
				optionUserProfile.setTitle("Option");
				optionUserProfile.setItems(pilihanOption, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						if (position == 0) {
							Intent in = new Intent(FormUserInfo.this, FormEditUserProfile.class);
							in.putExtra("Id", userId);
							in.putExtra("Username", userName);
							in.putExtra("Email", userEmail);
							in.putExtra("Place", city);
							in.putExtra("Gender", gender);
							in.putExtra("BirthDay", birthDay);
							in.putExtra("BirthMonth", birthMonth);
							in.putExtra("BirthYear", birthYear);
							
							startActivity(in);
						}
						else {
							FormEditUserPhoto.currentUserPhoto = userPhoto;
							Intent in = new Intent(FormUserInfo.this, FormEditUserPhoto.class);
							startActivity(in);
						}
					}
				});
				
				AlertDialog alertUserProfile = optionUserProfile.create();
				alertUserProfile.show();
			}
		});
		
		btnViewCommunity = (Button)findViewById(R.id.btnViewCommunity);
		btnViewCommunity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(FormUserInfo.this, null, "Please Wait!");
				
				String URL_Commun_Member = "http://api.wakuwakuw.com/rest/community_members/?user_id=" + userId;
				
				donlodTaskCommunList = new DownloadTaskCommunityList();
				donlodTaskCommunList.execute(URL_Commun_Member);
			}
		});
		
		progressDialog = ProgressDialog.show(this, null, "Please Wait!");
		
		if (statusOpenedFrom.equals("Timeline")) {
			linkUserProfile = "http://api.wakuwakuw.com/rest/users/?id=" + Timeline.idUser;
			imgSeparator.setVisibility(View.VISIBLE);
			imgBtnOption.setVisibility(View.VISIBLE);
		}
		else {
			linkUserProfile = "http://api.wakuwakuw.com/rest/users/?name=" + URLEncoder.encode(additionalParameter);
			imgSeparator.setVisibility(View.GONE);
			imgBtnOption.setVisibility(View.GONE);
		}
		
		donlodTaskUserInfo = new DownloadTaskUserInfo();
		donlodTaskUserInfo.execute(linkUserProfile);
	}
	
	
	public class DownloadTaskUserInfo extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... url) {
			// TODO Auto-generated method stub
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(url[0], Timeline.isiUsername, Timeline.isiPassword);
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				Element el = (Element) nl.item(0);
				
				userId = parser.getValue(el, ID);
				userName = parser.getValue(el, NAME);
				userEmail = parser.getValue(el, EMAIL);
				birthday = parser.getValue(el, BIRTHDAY);
				genderLabel = parser.getValue(el, GENDER);
				if (genderLabel.equals("f"))
					gender = "Female";
				else if (genderLabel.equals("m"))
					gender = "Male";
				
				String cityId = parser.getValue(el, USER_CITY_ID);
				String Url_City = "http://api.wakuwakuw.com/rest/cities/?id=" + cityId;
				
				XMLParser parserCity = new XMLParser();
				String xmlCity = parserCity.getXmlFromUrl(Url_City, Timeline.isiUsername, Timeline.isiPassword);
				Document docCity = parserCity.getDomElement(xmlCity);
				NodeList nlCity = docCity.getElementsByTagName(DATUM);
				Element elCity = (Element) nlCity.item(0);
				city = parser.getValue(elCity, CITY_NAME);
				
				userPhoto = LoadImageFromWeb("http://wakuwakuw.com/img/user/" + userId + "?size=medium");
				
				//mendapatkan total coommunity yg sudah diikuti
				String URL_Commun_Member = "http://api.wakuwakuw.com/rest/community_members/?user_id=" + userId;
				XMLParser parserCommunMember = new XMLParser();
				String xmlCommunMember = parserCommunMember.getXmlFromUrl(URL_Commun_Member, Timeline.isiUsername, Timeline.isiPassword);
				Document docCommunMember = parserCommunMember.getDomElement(xmlCommunMember);
				NodeList nlCommunMember = docCommunMember.getElementsByTagName(DATUM);
				totalCommunity = nlCommunMember.getLength();
				////////////////////////////////////////////////
				
				long userBirthday = Long.parseLong(birthday) * 1000;
				age = Integer.toString(getAge(userBirthday));
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			imgUserPhoto.setImageDrawable(userPhoto);
			txtUserCity.setText(city);
			txtUserExplanation.setText("Hello I'm " + userName + ", " + age + " years old (" + gender + ") from " + city + ".");
			if (totalCommunity > 1)
				btnViewCommunity.setText("Communities (" + totalCommunity + ")");
			else if (totalCommunity <= 1) 
				btnViewCommunity.setText("Community(" + totalCommunity + ")");
		}
	}
	
	public class DownloadTaskCommunityList extends AsyncTask<String, Void, CustomAdapterCustomDialog> {

		@Override
		protected CustomAdapterCustomDialog doInBackground(String... url) {
			// TODO Auto-generated method stub
			XMLParser parserCommunMember = new XMLParser();
			String xmlCommunMember = parserCommunMember.getXmlFromUrl(url[0], Timeline.isiUsername, Timeline.isiPassword);
			Document docCommunMember = parserCommunMember.getDomElement(xmlCommunMember.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			NodeList nlCommunMember = docCommunMember.getElementsByTagName(DATUM);
			
			if (nlCommunMember.getLength() != 0) {
				isiIdCommun = new ArrayList<String>();
				isiLogoCommun = new ArrayList<Drawable>();
				isiNamaCommun = new ArrayList<String>();
				
				for (int i = 0; i < nlCommunMember.getLength(); i++) {
					Element elCommunMember = (Element) nlCommunMember.item(i);
					// adding each child node to HashMap key => value
					String communityId = parserCommunMember.getValue(elCommunMember, COMMUNITY_ID);
					isiIdCommun.add(communityId);
					
					//ini buat ngambil nama kategori (untuk community)
					String URL_Community = "http://api.wakuwakuw.com/rest/communities/?id=" + communityId;
					XMLParser parserCommunity = new XMLParser();
					String xmlCommunity = parserCommunity.getXmlFromUrl(URL_Community, Timeline.isiUsername, Timeline.isiPassword);
					Document docCommunity = parserCommunity.getDomElement(xmlCommunity);
					NodeList nlCommunity = docCommunity.getElementsByTagName(DATA);
					Element elCommunity = (Element) nlCommunity.item(0);
					isiNamaCommun.add(parserCommunity.getValue(elCommunity, NAME));
					
					isiLogoCommun.add(LoadImageFromWeb("http://wakuwakuw.com/img/community/" + communityId + "?size=feed"));
					///////////////////////////////////////////////
				}
				
				customAdapter = new CustomAdapterCustomDialog(FormUserInfo.this, isiIdCommun, isiLogoCommun, isiNamaCommun);
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
			LayoutInflater inflater = (LayoutInflater) FormUserInfo.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
			
			ListView listCustomDialog = (ListView) layout.findViewById(R.id.listViewCustomDialog);
			listCustomDialog.setAdapter(result);
			listCustomDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> ad, View v, int pos, long id) {
					// TODO Auto-generated method stub
					/*
					FormUserInfo.statusOpenedFrom = "FormDetailCommun";
					FormUserInfo.additionalParameter = result.theName.get(pos).toString();
					
					Intent in = new Intent(FormUserInfo.this, FormUserInfo.class);
					startActivity(in);*/
				}
			});
			
			AlertDialog.Builder optionCommunList = new AlertDialog.Builder(FormUserInfo.this);
			optionCommunList.setTitle(userName + " joins this community");
			optionCommunList.setView(layout);
			
			AlertDialog alertCommunList = optionCommunList.create();
			alertCommunList.show();
		}
	}
	
	//method untuk mengetahui umur user
	private int getAge(long userBirthday) {
		Date now = new Date();
		int currentYear = now.getYear();
		int currentMonth = now.getMonth() + 1;
		int currentDay = now.getDate();
		
		Date birthTime = new Date(userBirthday);
		birthYear = birthTime.getYear();
		birthMonth = birthTime.getMonth() + 1;
		birthDay = birthTime.getDate();
		
		int year_diff  = currentYear - birthYear;
	    int month_diff = currentMonth - birthMonth;
	    int day_diff   = currentDay - birthDay;
	    
	    if (day_diff < 0 || month_diff < 0)
	        year_diff--;
	    
	    return year_diff;
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
