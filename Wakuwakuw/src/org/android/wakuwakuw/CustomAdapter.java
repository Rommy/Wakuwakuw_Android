package org.android.wakuwakuw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
	public ArrayList<String> Id;
	public ArrayList<String> IdLogo;
	public ArrayList<Drawable> logo;
	public ArrayList<String> judul;
	public ArrayList<String> penjelasan;
	public ArrayList<String> alamat;
	public ArrayList<String> tanggal;
	public ArrayList<String> tanggalAkhir;
	public ArrayList<String> jenis;
	public ArrayList<String> linkURL;
	public ArrayList<String> koorLat;
	public ArrayList<String> koorLong;
	public ArrayList<String> jmlhMember;
	public ArrayList<String> jmlhComment;
	public ArrayList<String> jmlhLike;
	public String EventOrMeetup;
	public boolean happeningNow;
	public Activity context;
	public LayoutInflater inflater;

	public CustomAdapter(Activity context, ArrayList<String> Id, ArrayList<String> IdLogo, ArrayList<Drawable> logo, ArrayList<String> judul, 
			ArrayList<String> penjelasan, ArrayList<String> alamat, ArrayList<String> tanggal, ArrayList<String> tanggalAkhir,
			ArrayList<String> jenis, ArrayList<String> linkURL, ArrayList<String> koorLat, ArrayList<String> koorLong, 
			ArrayList<String> jmlhMember, ArrayList<String> jmlhComment, ArrayList<String> jmlhLike, 
			String EventOrMeetup, boolean happeningNow) {
		super();

		this.context = context;
		this.EventOrMeetup = EventOrMeetup;
		this.Id = Id;
		this.IdLogo = IdLogo;
		this.logo = logo;
		this.judul = judul;
		this.penjelasan = penjelasan;
		this.alamat = alamat;
		this.tanggal = tanggal;
		this.tanggalAkhir = tanggalAkhir;
		this.jenis = jenis;
		this.linkURL = linkURL;
		this.koorLat = koorLat;
		this.koorLong = koorLong;
		this.jmlhMember = jmlhMember;
		this.jmlhComment = jmlhComment;
		this.jmlhLike = jmlhLike;
		this.happeningNow = happeningNow;

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Id.size();
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
		ImageView imgViewLogo;
		TextView txtId;
		TextView txtJudul;
		//TextView txtPenjelasan;
		TextView txtAlamat;
		TextView txtTanggal;
		TextView txtJenis;
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
			convertView = inflater.inflate(R.layout.daftar_dlm_list, null);

			holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
			holder.txtId = (TextView) convertView.findViewById(R.id.txtViewIdAcara);
			holder.txtJudul = (TextView) convertView.findViewById(R.id.txtViewJudul);
			//holder.txtPenjelasan = (TextView) convertView.findViewById(R.id.txtViewPenjelasan);
			holder.txtAlamat = (TextView) convertView.findViewById(R.id.txtViewAlmtAcara);
			holder.txtTanggal = (TextView) convertView.findViewById(R.id.txtViewTglAcara);
			holder.txtJenis = (TextView) convertView.findViewById(R.id.txtViewJenisAcara);
			holder.txtJmlhMember = (TextView) convertView.findViewById(R.id.txtViewJumlahMember);
			holder.txtJmlhComment = (TextView) convertView.findViewById(R.id.txtViewJumlahComment);
			holder.txtJmlhLike = (TextView) convertView.findViewById(R.id.txtViewJumlahLike);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		//nanti dipakai buat ngubah holder.txtTanggal jadi dalam bentuk jam aja...
		SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
		
		holder.imgViewLogo.setImageDrawable(logo.get(position));
		holder.txtId.setText(Id.get(position));
		holder.txtJudul.setText(judul.get(position));
		//holder.txtPenjelasan.setText(penjelasan.get(position));
		holder.txtAlamat.setText(alamat.get(position));
		
		//proses pengecekan untuk menampilkan 'happening now'
		if (happeningNow)
			holder.txtTanggal.setText("Happening Now");
		else
			holder.txtTanggal.setText(outFormat.format(Date.parse(tanggal.get(position))));
		
		holder.txtJenis.setText(jenis.get(position));
		
		holder.txtJmlhMember.setText(jmlhMember.get(position));
		holder.txtJmlhComment.setText(jmlhComment.get(position));
		holder.txtJmlhLike.setText(jmlhLike.get(position));
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String s = holder.txtJudul.getText().toString();
				//int index = judul.indexOf(s);
				
				//int index = logo.indexOf(holder.imgViewLogo.getDrawable());
				int index = Id.indexOf(holder.txtId.getText().toString());
				
				//Toast.makeText(context, s, Toast.LENGTH_LONG).show();
				//Toast.makeText(context, Integer.toString(judul.indexOf(s)), Toast.LENGTH_LONG).show();
				
				FormDetail.idAcara = Id.get(index);
				FormDetail.idLogo= IdLogo.get(index);
				FormDetail.logoAcara = logo.get(index);
				FormDetail.namaAcara = judul.get(index);
				FormDetail.tmptAcara = alamat.get(index);
				FormDetail.waktuAcara = tanggal.get(index);
				FormDetail.waktuAkhirAcara = tanggalAkhir.get(index);
				FormDetail.jenisAcara = jenis.get(index);
				FormDetail.deskripAcara = penjelasan.get(index);
				FormDetail.linkURL = linkURL.get(index);
				FormDetail.koorLatAcara = koorLat.get(index);
				FormDetail.koorLongAcara = koorLong.get(index);
				FormDetail.jmlhMember = jmlhMember.get(index);
				FormDetail.jmlhComment = jmlhComment.get(index);
				FormDetail.jmlhLike = jmlhLike.get(index);
				FormDetail.categoryStatus = EventOrMeetup;
				
				FormPhotoGallery.categoryStatus = EventOrMeetup;
				
				FormViewAllComment.categoryStatus = EventOrMeetup;
				FormViewAllComment.theId = Id.get(index);
				
				Intent in = new Intent(context, FormDetail.class);
				context.startActivity(in);
			}
		});
		
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Event OnLongClick", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		return convertView;
	} 
}
