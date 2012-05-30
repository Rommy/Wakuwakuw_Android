package org.android.wakuwakuw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class FormCreateNewCommunity extends Activity {
	
	private EditText editCommunityName, editCommunityContactPerson, editCommunityEmail, editCommunityWebsite, 
			editCommunityAddress, editCommunityPhone, editCommunityDescription;
	private Spinner spnCommunityCategory;
	private ImageView imgPickFromSDCard;
	private Button btnPickFromSDCard, btnNextCreateNewCommunity;
	
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmapFromSDCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_create_new_community);
		
		editCommunityName = (EditText)findViewById(R.id.editCommunityName);
		editCommunityContactPerson = (EditText)findViewById(R.id.editCommunityContactPerson);
		editCommunityEmail = (EditText)findViewById(R.id.editCommunityEmail);
		editCommunityWebsite = (EditText)findViewById(R.id.editCommunityWebsite);
		editCommunityAddress = (EditText)findViewById(R.id.editCommunityAddress);
		editCommunityPhone = (EditText)findViewById(R.id.editCommunityPhone);
		editCommunityDescription = (EditText)findViewById(R.id.editCommunityDescription);
		
		spnCommunityCategory = (Spinner)findViewById(R.id.spnCommunityCategory);
		
		imgPickFromSDCard = (ImageView)findViewById(R.id.imgViewPickFromSDCardCommunity);
		
		
		btnPickFromSDCard = (Button)findViewById(R.id.buttonPickImageCommunity);
		btnPickFromSDCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pickImage();
			}
		});
		
		
		btnNextCreateNewCommunity = (Button)findViewById(R.id.buttonNextCreateNewCommunity);
		//btnNextCreateNewCommunity.getBackground().setColorFilter(0x710089E1, Mode.MULTIPLY);
		btnNextCreateNewCommunity.getBackground().setColorFilter(Color.argb(255, 0, 137, 225), Mode.MULTIPLY);
		btnNextCreateNewCommunity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
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
}
