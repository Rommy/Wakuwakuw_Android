package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class FormPhotoGallery extends Activity {
	
	private GridView gridViewPhotoGallery;
	private ProgressDialog progressDialog;
	
	private ArrayList<Drawable> isiPhoto;
	private ArrayList<String> isiPhotoId;
	//private ArrayList<String> isiPhotoId, isiUserId, isiCaption, isiTimeCreated;
	
	public static String categoryStatus;
	public String URL_All_Photos;
	
	public ImageAdapter imgAdapter;
	
	public DownloadAllPhotos donlodAllPhotos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_photo_gallery);
		
		if (categoryStatus.equals("event")) {
			URL_All_Photos = "http://api.wakuwakuw.com/rest/event_photos/?event_id=" + FormDetail.idAcara;
		}
		else if (categoryStatus.equals("meetup")) {
			URL_All_Photos = "http://api.wakuwakuw.com/rest/meetup_photos/?meetup_id=" + FormDetail.idAcara;
		}
		else if (categoryStatus.equals("community")) {
			URL_All_Photos = "http://api.wakuwakuw.com/rest/community_photos/?community_id=" + FormDetailCommun.idCommun;
		}
		
		
		gridViewPhotoGallery = (GridView)findViewById(R.id.gridViewPhotoGallery);
		//gridViewPhotoGallery.setAdapter(imgAdapter);
		gridViewPhotoGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> ad, View v, int pos, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), ((TextView) v.findViewById(R.id.grid_item_id)).getText().toString(), Toast.LENGTH_SHORT).show();
				FormPhotoDetail.photoId = ((TextView) v.findViewById(R.id.grid_item_id)).getText().toString();
				
				Intent in = new Intent(FormPhotoGallery.this, FormPhotoDetail.class);
				startActivity(in);
			}
		});
		
		progressDialog = ProgressDialog.show(this, null, "Loading");
		
		donlodAllPhotos = new DownloadAllPhotos();
		donlodAllPhotos.execute(URL_All_Photos);
	}
	
	
	public class DownloadAllPhotos extends AsyncTask<String, Void, ImageAdapter> {

		@Override
		protected ImageAdapter doInBackground(String... params) {
			// TODO Auto-generated method stub
			isiPhoto = new ArrayList<Drawable>();
			isiPhotoId = new ArrayList<String>();
			/*isiUserId = new ArrayList<String>();
			isiCaption = new ArrayList<String>();
			isiTimeCreated = new ArrayList<String>();*/
			
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(URL_All_Photos, Timeline.isiUsername, Timeline.isiPassword);
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element el = (Element) nl.item(i);
					
					String photoId = parser.getValue(el, PHOTO_ID);
					
					isiPhoto.add(LoadImageFromWeb("http://wakuwakuw.com/img/photo/" + photoId + "?size=stream"));
					isiPhotoId.add(photoId);
				}
				
				imgAdapter = new ImageAdapter(FormPhotoGallery.this, isiPhoto, isiPhotoId);
			}
			else {
				imgAdapter = null;
			}
			
			return imgAdapter;
		}
		
		
		@Override
		protected void onPostExecute(ImageAdapter imgAdapter) {
			// TODO Auto-generated method stub
			//super.onPostExecute(imgAdapter);
			
			progressDialog.dismiss();
			Toast.makeText(FormPhotoGallery.this, "Finished", Toast.LENGTH_SHORT).show();
			gridViewPhotoGallery.setAdapter(imgAdapter);
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
