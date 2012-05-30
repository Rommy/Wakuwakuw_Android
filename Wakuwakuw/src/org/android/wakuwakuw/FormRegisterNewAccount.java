package org.android.wakuwakuw;

import labs.captcha.CaptchaException;
import labs.captcha.CaptchaService;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FormRegisterNewAccount extends Activity {
	
	private static final String DEVKEY = "zr6yOv7pIVEBP3PkjdH1DjghJKtFoaDw6kmrgU6r";
	private CaptchaService captchaService;
	private String idCaptcha;
	private Bitmap bitmapCaptcha;
	
	public static DownloadTaskCaptcha donlodTaskCaptcha;
	
	private EditText editRegisterFullName, editRegisterEmail, editRegisterWakuwakuwUsername, 
		editRegisterWakuwakuwPassword, editInputCaptcha;
	private ImageView imgCaptcha;
	private TextView txtReloadCaptcha;
	private Button btnConfirmRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_register_new_account);
		
		captchaService = new CaptchaService();
		
		editRegisterFullName = (EditText)findViewById(R.id.editRegisterFullName);
		editRegisterEmail = (EditText)findViewById(R.id.editRegisterEmail);
		editRegisterWakuwakuwUsername = (EditText)findViewById(R.id.editRegisterWakuwakuwUsername);
		editRegisterWakuwakuwPassword = (EditText)findViewById(R.id.editRegisterWakuwakuwPassword);
		editInputCaptcha = (EditText)findViewById(R.id.editInputCaptcha);
		
		imgCaptcha = (ImageView)findViewById(R.id.imgCaptcha);
		
		txtReloadCaptcha = (TextView)findViewById(R.id.txtReloadCaptcha);
		txtReloadCaptcha.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//reload captcha here
				try {
					bitmapCaptcha = captchaService.getCaptcha(DEVKEY);
				} catch (CaptchaException e) {
					e.printStackTrace();
				}
				
				imgCaptcha.setImageBitmap(bitmapCaptcha);
			}
		});
		
		btnConfirmRegister = (Button)findViewById(R.id.buttonConfirmRegister);
		btnConfirmRegister.getBackground().setColorFilter(Color.parseColor("#FF8FCF29"), Mode.MULTIPLY);
		btnConfirmRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editRegisterFullName.getText().toString().equals("") || editRegisterEmail.getText().toString().equals("") ||
						editRegisterWakuwakuwUsername.getText().toString().equals("") || editRegisterWakuwakuwPassword.getText().toString().equals("")) {
					Toast.makeText(FormRegisterNewAccount.this, "Fill all neccesary fields please.", Toast.LENGTH_LONG).show();
				}
				else {
					//tahap verifikasi captcha
					if (editInputCaptcha.getText().toString().equals("")) {
						Toast.makeText(FormRegisterNewAccount.this, "Image verification failed. Please type the text again.", Toast.LENGTH_LONG).show();
					}
					else {
						//jika user memasukkan captcha dgn benar
						if (captchaService.verify(idCaptcha, editInputCaptcha.getText().toString(), DEVKEY)) {
							Toast.makeText(FormRegisterNewAccount.this, "Image verification success.", Toast.LENGTH_LONG).show();
						}
						//jika user memasukkan captcha dgn salah
						else {
							Toast.makeText(FormRegisterNewAccount.this, "Image verification failed. Please type the text again.", Toast.LENGTH_LONG).show();
							
							try {
								bitmapCaptcha = captchaService.getCaptcha(DEVKEY);
							} catch (CaptchaException e) {
								e.printStackTrace();
							}
							
							imgCaptcha.setImageBitmap(bitmapCaptcha);
						}
					}
					///////////////
				}
			}
		});
		
		donlodTaskCaptcha = new DownloadTaskCaptcha();
		donlodTaskCaptcha.execute();
	}
	
	
	public class DownloadTaskCaptcha extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				bitmapCaptcha = captchaService.getCaptcha(DEVKEY);
				//image.setImageBitmap(img);
			} catch (CaptchaException e) {
				e.printStackTrace();
			}
			
			return bitmapCaptcha;
		}
		
		
		@Override
		protected void onPostExecute(Bitmap bitmapResult) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			imgCaptcha.setImageBitmap(bitmapResult);
		}	
	}
}
