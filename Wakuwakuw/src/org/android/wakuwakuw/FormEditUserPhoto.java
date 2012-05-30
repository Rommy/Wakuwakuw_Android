package org.android.wakuwakuw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class FormEditUserPhoto extends Activity {
	
	private static final int SD_CARD_REQUEST = 1;
	private static final int CAMERA_REQUEST = 1888;
	
	private ImageView imgUserPhoto;
	private Button btnUploadPhoto, btnConfirmUpload, btnCancelUpload;
	private ProgressDialog progressDialog;
	
	public static Drawable currentUserPhoto;
	private Bitmap bitmapFromSDCard;
	
	private String pilihanOption[] = {"Upload File", "Take With Camera"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit_user_photo);
		
		imgUserPhoto = (ImageView)findViewById(R.id.imgViewUserPhoto);
		imgUserPhoto.setImageDrawable(currentUserPhoto);
		
		btnUploadPhoto = (Button)findViewById(R.id.buttonUploadPhoto);
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder optionUploadPhoto = new AlertDialog.Builder(FormEditUserPhoto.this);
				//optionUserProfile.setView(layout);
				optionUploadPhoto.setItems(pilihanOption, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// TODO Auto-generated method stub
						//saat user memilih upload photo langsung dari kartu memory (SD Card)
						if (position == 0) {
							pickImageFromSDCard();
						}
						//saat user memilih mengambil photo melalui camera
						else {
							Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			                startActivityForResult(cameraIntent, CAMERA_REQUEST);
						}
					}
				});
				
				AlertDialog alertUploadPhoto = optionUploadPhoto.create();
				alertUploadPhoto.show();
			}
		});
		
		btnConfirmUpload = (Button)findViewById(R.id.buttonConfirmUpload);
		btnConfirmUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnCancelUpload = (Button)findViewById(R.id.buttonCancelUpload);
		btnCancelUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	public void pickImageFromSDCard() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, SD_CARD_REQUEST);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imgUserPhoto.setImageBitmap(photo);
        }
		else if (requestCode == SD_CARD_REQUEST && resultCode == Activity.RESULT_OK) {
			InputStream stream = null;
			
			try {
				// We need to recyle unused bitmaps
				if (bitmapFromSDCard != null) {
					bitmapFromSDCard.recycle();
				}
				stream = getContentResolver().openInputStream(data.getData());
				bitmapFromSDCard = BitmapFactory.decodeStream(stream);

				imgUserPhoto.setImageBitmap(bitmapFromSDCard);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			finally {
				if (stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
