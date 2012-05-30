package org.android.wakuwakuw;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageAdapter extends BaseAdapter {
	
	public ArrayList<Drawable> photo;
	public ArrayList<String> idPhoto;
	//public ArrayList<String> idUser;
	//public ArrayList<String> caption;
	//public ArrayList<String> timeCreated;
	
	public Context context;

	public ImageAdapter(Context context, ArrayList<Drawable> photo, ArrayList<String> idPhoto) {
		super();
		
		this.context = context;
		
		this.photo = photo;
		this.idPhoto = idPhoto;
		/*this.idUser = idUser;
		this.caption = caption;
		this.timeCreated = timeCreated;*/
	}

	@Override
	public int getCount() {
		return idPhoto.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from daftar_dlm_photo_gallery.xml
			gridView = inflater.inflate(R.layout.daftar_dlm_photo_gallery, null);

			// set image based on selected text
			ImageView imgPhoto = (ImageView) gridView.findViewById(R.id.grid_item_image);
			imgPhoto.setImageDrawable(photo.get(position));
			
			
			// set value into textview
			TextView txtCaption= (TextView) gridView.findViewById(R.id.grid_item_id);
			txtCaption.setText(idPhoto.get(position));
			

			/*
			String mobile = mobileValues[position];

			if (mobile.equals("Windows")) {
				imageView.setImageResource(R.drawable.windows_logo);
			} else if (mobile.equals("iOS")) {
				imageView.setImageResource(R.drawable.ios_logo);
			} else if (mobile.equals("Blackberry")) {
				imageView.setImageResource(R.drawable.blackberry_logo);
			} else {
				imageView.setImageResource(R.drawable.android_logo);
			}
			*/

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

}
