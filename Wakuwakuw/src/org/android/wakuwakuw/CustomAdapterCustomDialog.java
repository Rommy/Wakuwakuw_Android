package org.android.wakuwakuw;

import java.util.ArrayList;

import org.android.wakuwakuw.CustomAdapterAllComment.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterCustomDialog extends BaseAdapter{

	public ArrayList<String> theId;
	public ArrayList<Drawable> theLogo;
	public ArrayList<String> theName;
	public Activity context;
	public LayoutInflater inflater;
	
	public CustomAdapterCustomDialog(Activity context, ArrayList<String> theId, ArrayList<Drawable> theLogo, ArrayList<String> theName) {
		// TODO Auto-generated constructor stub
		super();

		this.context = context;
		this.theId = theId;
		this.theLogo = theLogo;
		this.theName = theName;
		
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return theId.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		ImageView imgViewLogoUser;
		TextView txtNamaUser;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.daftar_dlm_custom_dialog, null);

			holder.imgViewLogoUser = (ImageView) convertView.findViewById(R.id.imgViewLogoUserDlmCustomDialog);
			holder.txtNamaUser = (TextView) convertView.findViewById(R.id.txtViewNamaUserDlmCustomDialog);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.imgViewLogoUser.setImageDrawable(theLogo.get(position));	
		holder.txtNamaUser.setText(theName.get(position));
		
		
		return convertView;
	}

}
