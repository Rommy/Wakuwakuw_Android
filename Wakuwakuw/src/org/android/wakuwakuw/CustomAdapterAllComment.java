package org.android.wakuwakuw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.android.wakuwakuw.CustomAdapterCommun.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterAllComment extends BaseAdapter {

	public ArrayList<String> idComment;
	public ArrayList<Drawable> logoUser;
	public ArrayList<String> namaUser;
	public ArrayList<String> isiCommentUser;
	public ArrayList<String> tanggalComment;
	public ArrayList<String> jmlhComment;
	public Activity context;
	public LayoutInflater inflater;
	public SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' hh:mm aaa");
	
	public CustomAdapterAllComment(Activity context, ArrayList<String> idComment, ArrayList<Drawable> logoUser, 
			ArrayList<String> namaUser, ArrayList<String> isiCommentUser, ArrayList<String> tanggalComment, ArrayList<String> jmlhComment) {
		super();

		this.context = context;
		this.idComment = idComment;
		this.logoUser = logoUser;
		this.namaUser = namaUser;
		this.isiCommentUser = isiCommentUser;
		this.tanggalComment = tanggalComment;
		this.jmlhComment = jmlhComment;

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return idComment.size();
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
		TextView txtIsiKomen;
		TextView txtTanggalKomen;
		TextView txtIdComment;
		TextView txtJmlhComment;
		ImageView imgViewComment;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.daftar_dlm_list_all_comments, null);

			holder.imgViewLogoUser = (ImageView) convertView.findViewById(R.id.imgViewLogoUser);
			holder.txtNamaUser = (TextView) convertView.findViewById(R.id.txtViewNamaUser);
			holder.txtIsiKomen = (TextView) convertView.findViewById(R.id.txtViewIsiComment);
			holder.txtTanggalKomen = (TextView) convertView.findViewById(R.id.txtViewTanggalComment);
			holder.txtIdComment = (TextView) convertView.findViewById(R.id.txtViewIdComment);
			holder.txtJmlhComment = (TextView) convertView.findViewById(R.id.txtViewJumlahCommentInParent);
			holder.imgViewComment = (ImageView) convertView.findViewById(R.id.imgViewCommentInParent);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		//SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
		
		holder.txtIdComment.setText(idComment.get(position));
		holder.imgViewLogoUser.setImageDrawable(logoUser.get(position));	
		holder.txtNamaUser.setText(namaUser.get(position));
		holder.txtIsiKomen.setText(isiCommentUser.get(position));
		holder.txtTanggalKomen.setText(outFormat.format(Date.parse(tanggalComment.get(position))));
		
		/*if (isiCommentUser.get(position).length() > 50) {
			String readMore = ">> Read More <<";
			holder.txtIsiKomen.setText(isiCommentUser.get(position).substring(0, 50) + " ...\n\n" + readMore);
		}
		else {
			holder.txtIsiKomen.setText(isiCommentUser.get(position));
		}*/
		
		if (Integer.parseInt(jmlhComment.get(position)) == 0) {
			holder.imgViewComment.setVisibility(View.GONE);
			holder.txtJmlhComment.setVisibility(View.GONE);
		} 
		else if (Integer.parseInt(jmlhComment.get(position)) == 1) {
			holder.imgViewComment.setVisibility(View.VISIBLE);
			holder.txtJmlhComment.setVisibility(View.VISIBLE);
			holder.txtJmlhComment.setText(jmlhComment.get(position) + " comment");
		}
		else if (Integer.parseInt(jmlhComment.get(position)) > 1) {
			holder.imgViewComment.setVisibility(View.VISIBLE);
			holder.txtJmlhComment.setVisibility(View.VISIBLE);
			holder.txtJmlhComment.setText(jmlhComment.get(position) + " comments");
		}
		
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String s = holder.txtNamaCommun.getText().toString();
				//int index = namaCommun.indexOf(s);
				
				int index = idComment.indexOf(holder.txtIdComment.getText().toString());
				
				
				FormViewOneComment.idComment = idComment.get(index);
				FormViewOneComment.logoFirstUserComment = logoUser.get(index);
				FormViewOneComment.firstUserName = namaUser.get(index);
				FormViewOneComment.firstUserComment = isiCommentUser.get(index);
				FormViewOneComment.firstUserCommentDate = outFormat.format(Date.parse(tanggalComment.get(index)));
				
				
				Intent in = new Intent(context, FormViewOneComment.class);
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
