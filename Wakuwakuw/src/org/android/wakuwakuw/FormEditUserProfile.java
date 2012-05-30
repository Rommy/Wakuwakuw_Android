package org.android.wakuwakuw;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FormEditUserProfile extends Activity {
	
	static final int DATE_DIALOG_ID = 2;
	
	private TextView txtHeaderEditProfile;
	private EditText editUserName, editUserEmail, editUserBirthday, editUserPlace;
	private ImageView imgCalendarPickBirthday;
	private RadioButton radioBtnMale, radioBtnFemale;
	private Button btnConfirmEditUserProfile, btnCancelEditUserProfile;
	
	private String userId, userName, userEmail, userGender, userPlace;
	private int theDay, theMonth, theYear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit_user_profile);
		
		Intent sender = getIntent();
		userId = sender.getExtras().getString("Id");
		userName = sender.getExtras().getString("Username");
        userEmail = sender.getExtras().getString("Email");
        userGender = sender.getExtras().getString("Gender");
        userPlace = sender.getExtras().getString("Place");
        theDay = sender.getExtras().getInt("BirthDay");
        theMonth = sender.getExtras().getInt("BirthMonth");
        theYear = 1900 + sender.getExtras().getInt("BirthYear");
        
		txtHeaderEditProfile = (TextView)findViewById(R.id.txtHeaderEditProfile);
		
		editUserName = (EditText)findViewById(R.id.editProfileUserName);
		editUserEmail = (EditText)findViewById(R.id.editProfileUserEmail);
		editUserBirthday = (EditText)findViewById(R.id.editProfileUserBirthday);
		editUserPlace = (EditText)findViewById(R.id.editProfileUserPlace);
		
		radioBtnMale = (RadioButton)findViewById(R.id.radioBtnMale);
		radioBtnFemale = (RadioButton)findViewById(R.id.radioBtnFemale);
		
		imgCalendarPickBirthday = (ImageView)findViewById(R.id.imgCalendarPickBirthday);
		imgCalendarPickBirthday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		btnConfirmEditUserProfile = (Button)findViewById(R.id.buttonConfirmEditUserProfile);
		btnConfirmEditUserProfile.getBackground().setColorFilter(Color.argb(255, 0, 137, 225), Mode.MULTIPLY);
		btnConfirmEditUserProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar cal = new GregorianCalendar();
				//indeks month mulai dari 0 - 11, jadi gunakan theMonth-1, 
				//contoh: misal bulan Juni (6) maka theMonth-1 = 5
				cal.set(theYear, theMonth-1, theDay, 0, 0, 0);
				long birthTime = cal.getTime().getTime();
				Toast.makeText(getApplicationContext(), Long.toString(birthTime / 1000), Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
				
				String URL_Put_Comment = "http://api.wakuwakuw.com/rest/users/" + userId;
				String URL_Body = "name=" + "Rommy Saiko" + "&email=" + userEmail + "&username=Rommy" + 
					"&password=myandroid";
				
				XMLParser parser = new XMLParser();
				parser.editSomething(URL_Put_Comment, URL_Body, Timeline.isiUsername, Timeline.isiPassword);
			}
		});
		
		btnCancelEditUserProfile = (Button)findViewById(R.id.buttonCancelEditUserProfile);
		btnCancelEditUserProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		txtHeaderEditProfile.setText("Edit Profile (" + userName + ")");
		editUserName.setText(userName);
		editUserEmail.setText(userEmail);
		editUserPlace.setText(userPlace);
		editUserBirthday.setText(theDay + "/" + theMonth + "/" + theYear);
		if (userGender.equals("Male"))
			radioBtnMale.setChecked(true);
		else
			radioBtnFemale.setChecked(true);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		//return super.onCreateDialog(id);
		
		switch (id) {
		//case TIME_DIALOG_ID:
		//	return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
		case DATE_DIALOG_ID:
			//return new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			return new DatePickerDialog(this, datePickerListener, theYear, theMonth-1, theDay);
		}
		
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			theYear= year; theMonth = monthOfYear; theDay = dayOfMonth;
			editUserBirthday.setText(new StringBuilder().append(theDay)
					   .append("/").append(theMonth+1).append("/").append(theYear));
		}
	};
}
