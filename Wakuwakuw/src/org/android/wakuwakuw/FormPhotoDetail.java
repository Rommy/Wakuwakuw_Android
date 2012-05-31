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
import android.widget.Toast;

public class FormPhotoDetail extends Activity {
	
	private ImageView imgPhotoDetail, imgToLeft, imgToRight;
	private TextView txtPhotoCaptionDetail, txtPhotoDate;
	private ProgressBar progBarLoading;
	
	public static String photoId;
	public static ArrayList<Drawable> isiPhoto;
	public static ArrayList<String> isiPhotoId;
	private Drawable photo;
	public String URLPhoto, URLPhotoDetail, photoCaption, photoDate;
	public int indexPhoto;
	
	public DownloadPhotoDetail donlodPhotoDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_photo_detail);
		
		indexPhoto = isiPhotoId.indexOf(photoId);
		
		URLPhoto = "http://wakuwakuw.com/img/photo/" + photoId + "?size=big";
		URLPhotoDetail = "http://api.wakuwakuw.com/rest/photos/" + photoId;
		
		imgPhotoDetail = (ImageView)findViewById(R.id.imgViewPhotoGalleryDetail);
		
		txtPhotoCaptionDetail = (TextView)findViewById(R.id.txtViewPhotoCaption);
		txtPhotoDate = (TextView)findViewById(R.id.txtViewPhotoDate);
		
		progBarLoading = (ProgressBar)findViewById(R.id.progressBarLoadingPhotoDetail);
		
		imgToLeft = (ImageView)findViewById(R.id.imgViewToLeft);
		imgToLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indexPhoto = indexPhoto - 1;
				if (indexPhoto >= 0) {
					progBarLoading.setVisibility(ViewGroup.VISIBLE);
					imgPhotoDetail.setVisibility(View.GONE);
					txtPhotoCaptionDetail.setVisibility(View.GONE);
					txtPhotoDate.setVisibility(View.GONE);
					imgToLeft.setVisibility(View.GONE);
					imgToRight.setVisibility(View.GONE);
					
					//Toast.makeText(getApplicationContext(), "Indexnya " + Integer.toString(indexPhoto) + " = " + isiPhotoId.get(indexPhoto), Toast.LENGTH_SHORT).show();
					
					URLPhoto = "http://wakuwakuw.com/img/photo/" + isiPhotoId.get(indexPhoto) + "?size=big";
					URLPhotoDetail = "http://api.wakuwakuw.com/rest/photos/" + isiPhotoId.get(indexPhoto);
					
					donlodPhotoDetail = new DownloadPhotoDetail();
					donlodPhotoDetail.execute();
				}
				else {
					indexPhoto = 0;
					Toast.makeText(getApplicationContext(), "This is the first photo.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		imgToRight = (ImageView)findViewById(R.id.imgViewToRight);
		imgToRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indexPhoto = indexPhoto + 1;
				if (indexPhoto < isiPhotoId.size()) {
					progBarLoading.setVisibility(ViewGroup.VISIBLE);
					imgPhotoDetail.setVisibility(View.GONE);
					txtPhotoCaptionDetail.setVisibility(View.GONE);
					txtPhotoDate.setVisibility(View.GONE);
					imgToLeft.setVisibility(View.GONE);
					imgToRight.setVisibility(View.GONE);
					
					//Toast.makeText(getApplicationContext(), "Indexnya " + Integer.toString(indexPhoto) + " = " + isiPhotoId.get(indexPhoto), Toast.LENGTH_SHORT).show();
					
					URLPhoto = "http://wakuwakuw.com/img/photo/" + isiPhotoId.get(indexPhoto) + "?size=big";
					URLPhotoDetail = "http://api.wakuwakuw.com/rest/photos/" + isiPhotoId.get(indexPhoto);
					
					donlodPhotoDetail = new DownloadPhotoDetail();
					donlodPhotoDetail.execute();
				}
				else {
					indexPhoto = isiPhotoId.size() - 1;
					Toast.makeText(getApplicationContext(), "This is the last photo.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		donlodPhotoDetail = new DownloadPhotoDetail();
		donlodPhotoDetail.execute();
	}
	
	
	public class DownloadPhotoDetail extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... URL) {
			// TODO Auto-generated method stub
			//photo = LoadImageFromWeb("http://wakuwakuw.com/img/photo/" + photoId + "?size=big");
			photo = LoadImageFromWeb(URLPhoto);
			
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
			progBarLoading.setVisibility(ViewGroup.GONE);
			
			imgPhotoDetail.setVisibility(View.VISIBLE);
			imgPhotoDetail.setImageDrawable(photo);
			
			txtPhotoCaptionDetail.setText(photoCaption);
			txtPhotoCaptionDetail.setVisibility(View.VISIBLE);
			
			txtPhotoDate.setText(photoDate);
			txtPhotoDate.setVisibility(View.VISIBLE);
			
			imgToLeft.setVisibility(View.VISIBLE);
			imgToRight.setVisibility(View.VISIBLE);
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
