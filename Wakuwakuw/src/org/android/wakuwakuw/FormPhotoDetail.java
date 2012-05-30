package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.CAPTION;
import static org.android.wakuwakuw.Variabel.DATA;
import static org.android.wakuwakuw.Variabel.TIME_CREATED;
import static org.android.wakuwakuw.Variabel.USER_ID;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FormPhotoDetail extends Activity {
	
	private ImageView imgPhotoDetail;
	private TextView txtPhotoCaptionDetail, txtPhotoDate;
	private ProgressBar progBarLoading;
	private ProgressDialog progressDialog;
	
	public static String photoId;
	private Drawable photo;
	public String URLPhotoDetail, photoCaption, photoDate;
	
	public DownloadPhotoDetail donlodPhotoDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_photo_detail);
		
		URLPhotoDetail = "http://api.wakuwakuw.com/rest/photos/" + photoId;
		
		imgPhotoDetail = (ImageView)findViewById(R.id.imgViewPhotoGalleryDetail);
		
		txtPhotoCaptionDetail = (TextView)findViewById(R.id.txtViewPhotoCaption);
		txtPhotoDate = (TextView)findViewById(R.id.txtViewPhotoDate);
		
		progBarLoading = (ProgressBar)findViewById(R.id.progressBarLoadingPhotoDetail);
		
		//progressDialog = ProgressDialog.show(this, null, "Loading");
		
		donlodPhotoDetail = new DownloadPhotoDetail();
		donlodPhotoDetail.execute();
	}
	
	
	public class DownloadPhotoDetail extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... param) {
			// TODO Auto-generated method stub
			photo = LoadImageFromWeb("http://wakuwakuw.com/img/photo/" + photoId + "?size=big");
			
			XMLParser parserPhoto= new XMLParser();
			String xmlPhoto = parserPhoto.getXmlFromUrl(URLPhotoDetail, Timeline.isiUsername, Timeline.isiPassword);
			Document docPhoto = parserPhoto.getDomElement(xmlPhoto);
			NodeList nlPhoto = docPhoto.getElementsByTagName(DATA);
			Element elPhoto = (Element) nlPhoto.item(0);
			
			photoCaption = parserPhoto.getValue(elPhoto, CAPTION);
			
			Date time = new Date();
			time.setTime(Long.parseLong(parserPhoto.getValue(elPhoto, TIME_CREATED)) * 1000);
			SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' hh:mm aaa");
			String timeCreated = outFormat.format(time);
			photoDate = timeCreated;
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			//progressDialog.dismiss();
			
			progBarLoading.setVisibility(ViewGroup.GONE);
			
			imgPhotoDetail.setVisibility(View.VISIBLE);
			imgPhotoDetail.setImageDrawable(photo);
			
			txtPhotoCaptionDetail.setText(photoCaption);
			txtPhotoCaptionDetail.setVisibility(View.VISIBLE);
			
			txtPhotoDate.setText(photoDate);
			txtPhotoDate.setVisibility(View.VISIBLE);
		}
	}
	
	
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
}
