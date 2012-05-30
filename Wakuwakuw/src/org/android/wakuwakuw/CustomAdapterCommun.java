package org.android.wakuwakuw;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapterCommun extends BaseAdapter {
	public ArrayList<String> idCommun;
	//public ArrayList<String> idLogoCommun;
	public ArrayList<Drawable> logoCommun;
	public ArrayList<String> namaCommun;
	public ArrayList<String> jenisCommun;
	public ArrayList<String> deskripsiCommun;
	public ArrayList<String> linkURL;
	public ArrayList<String> jmlhMember;
	public ArrayList<String> jmlhComment;
	public ArrayList<String> jmlhLike;
	public Activity context;
	public LayoutInflater inflater;

	public CustomAdapterCommun(Activity context, ArrayList<String> idCommun, ArrayList<Drawable> logoCommun, 
			ArrayList<String> namaCommun, ArrayList<String> jenisCommun, ArrayList<String> deskripsiCommun,
			ArrayList<String> linkURL, ArrayList<String> jmlhMember, ArrayList<String> jmlhComment, ArrayList<String> jmlhLike) {
		super();

		this.context = context;
		this.idCommun = idCommun;
		//this.idLogoCommun = idCommun;
		this.logoCommun = logoCommun;
		this.namaCommun = namaCommun;
		this.jenisCommun = jenisCommun;
		this.deskripsiCommun = deskripsiCommun;
		this.linkURL = linkURL;
		this.jmlhMember = jmlhMember;
		this.jmlhComment = jmlhComment;
		this.jmlhLike = jmlhLike;

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return idCommun.size();
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
		ImageView imgViewLogoCommun;
		TextView txtNamaCommun;
		TextView txtJenisCommun;
		TextView txtDeskripsiCommun;
		TextView txtIdCommun;
		TextView txtJmlhMember;
		TextView txtJmlhComment;
		TextView txtJmlhLike;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.daftar_dlm_list_communities, null);

			holder.imgViewLogoCommun = (ImageView) convertView.findViewById(R.id.imgViewLogoCommun);
			holder.txtNamaCommun = (TextView) convertView.findViewById(R.id.txtViewNamaCommun);
			holder.txtJenisCommun = (TextView) convertView.findViewById(R.id.txtViewJenisCommun);
			holder.txtDeskripsiCommun = (TextView) convertView.findViewById(R.id.txtViewDeskripsiCommun);
			holder.txtIdCommun = (TextView) convertView.findViewById(R.id.txtViewIdCommun);
			holder.txtJmlhMember = (TextView) convertView.findViewById(R.id.txtViewJumlahCommunMember);
			holder.txtJmlhComment = (TextView) convertView.findViewById(R.id.txtViewJumlahCommunComment);
			holder.txtJmlhLike = (TextView) convertView.findViewById(R.id.txtViewJumlahCommunLike);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		
		holder.txtIdCommun.setText(idCommun.get(position));
		holder.imgViewLogoCommun.setImageDrawable(logoCommun.get(position));	
		holder.txtNamaCommun.setText(namaCommun.get(position));
		holder.txtJenisCommun.setText(jenisCommun.get(position));
		if (deskripsiCommun.get(position).length() > 50) {
			String readMore = ">> Read More <<";
			holder.txtDeskripsiCommun.setText(deskripsiCommun.get(position).substring(0, 50) + " ...\n\n" + readMore);
		}
		else {
			holder.txtDeskripsiCommun.setText(deskripsiCommun.get(position));
		}
		
		//holder.txtDeskripsiCommun.setText(deskripsiCommun.get(position).substring(0, 10));
		
		holder.txtJmlhMember.setText(jmlhMember.get(position));
		holder.txtJmlhComment.setText(jmlhComment.get(position));
		holder.txtJmlhLike.setText(jmlhLike.get(position));
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String s = holder.txtNamaCommun.getText().toString();
				//int index = namaCommun.indexOf(s);
				
				//int index = logoCommun.indexOf(holder.imgViewLogoCommun.getDrawable());
				int index = idCommun.indexOf(holder.txtIdCommun.getText().toString());
				
				FormDetailCommun.idCommun = idCommun.get(index);
				FormDetailCommun.idLogoCommun = idCommun.get(index);
				FormDetailCommun.logoAcaraCommun = logoCommun.get(index);
				FormDetailCommun.namaCommun = namaCommun.get(index);
				FormDetailCommun.jenisCommun = jenisCommun.get(index);
				FormDetailCommun.deskripCommun = deskripsiCommun.get(index);
				FormDetailCommun.linkURL = linkURL.get(index);
				FormDetailCommun.jmlhMemberCommun = jmlhMember.get(index);
				FormDetailCommun.jmlhCommentCommun = jmlhComment.get(index);
				FormDetailCommun.jmlhLikeCommun = jmlhLike.get(index);
				
				FormPhotoGallery.categoryStatus = "community";
				
				FormViewAllComment.categoryStatus = "community";
				FormViewAllComment.theId = idCommun.get(index);
				
				Intent in = new Intent(context, FormDetailCommun.class);
				context.startActivity(in);
			}
		});
		
		/*
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Event OnLongClick", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		*/
		
		return convertView;
	} 
}
