package org.android.wakuwakuw;

import static org.android.wakuwakuw.Variabel.*;

/*import static org.android.wakuwakuw.Variabel.CATEGORY_NAME;
import static org.android.wakuwakuw.Variabel.COMMUNITY_CATEGORY_ID;
import static org.android.wakuwakuw.Variabel.COMMUNITY_ID;
import static org.android.wakuwakuw.Variabel.DESCRIPTION;
import static org.android.wakuwakuw.Variabel.ID;
import static org.android.wakuwakuw.Variabel.LATITUDE;
import static org.android.wakuwakuw.Variabel.LOCATION;
import static org.android.wakuwakuw.Variabel.LONGITUDE;
import static org.android.wakuwakuw.Variabel.TIME_END;
import static org.android.wakuwakuw.Variabel.TIME_START;
import static org.android.wakuwakuw.Variabel.TITLE;
import static org.android.wakuwakuw.Variabel.TOTAL_COMMENTS;
import static org.android.wakuwakuw.Variabel.TOTAL_LIKES;
import static org.android.wakuwakuw.Variabel.TOTAL_MEMBERS;*/

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.graphics.drawable.Drawable;

public class DateArranger {
	
	//public int x;
	//CustomAdapter customAdapter;	
	
	/*
	private ArrayList<Drawable> isiLogoKump;
	private ArrayList<String> isiIdKump, isiIdLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, isiTanggalKumpStart, 
					isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump,
					isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump;
	*/
	
	public void arrangeDate(Activity context, SeparatedListAdapter adapternya, ArrayList<String> isiId, ArrayList<String> isiIdLogo, ArrayList<Drawable> isiLogo, 
			ArrayList<String> isiJudul,	ArrayList<String> isiPenjelasan, ArrayList<String> isiAlamat, ArrayList<String> isiTanggalStart, ArrayList<String> isiTanggalEnd, 
			ArrayList<String> isiJenis, ArrayList<String> isiLinkURL, ArrayList<String> isiKoorLat, ArrayList<String> isiKoorLong, 
			ArrayList<String> isiJmlhMember, ArrayList<String> isiJmlhComment, ArrayList<String> isiJmlhLike, 
			ArrayList<String> isiIdKump, ArrayList<String> isiIdLogoKump, ArrayList<Drawable> isiLogoKump, ArrayList<String> isiJudulKump,
			ArrayList<String> isiPenjelasanKump, ArrayList<String> isiAlamatKump, ArrayList<String> isiTanggalKumpStart, ArrayList<String> isiTanggalKumpEnd,
			ArrayList<String> isiJenisKump, ArrayList<String> isiLinkURLKump, ArrayList<String> isiKoorLatKump, ArrayList<String> isiKoorLongKump,
			ArrayList<String> isiJmlhMemberKump, ArrayList<String> isiJmlhCommentKump, ArrayList<String> isiJmlhLikeKump,
			CustomAdapter customAdapter, String eventOrMeetup) {
		
		/*
		this.isiIdKump = isiIdKump;
		this.isiIdLogoKump = isiIdLogoKump;
		this.isiLogoKump = isiLogoKump;
		this.isiJudulKump = isiJudulKump;
		this.isiPenjelasanKump = isiPenjelasanKump;
		this.isiAlamatKump = isiAlamatKump;
		this.isiTanggalKumpStart = isiTanggalKumpStart;
		this.isiTanggalKumpEnd = isiTanggalKumpEnd;
		this.isiJenisKump = isiJenisKump;
		this.isiLinkURLKump = isiLinkURLKump;
		this.isiKoorLatKump = isiKoorLatKump;
		this.isiKoorLongKump = isiKoorLongKump;
		this.isiJmlhMemberKump = isiJmlhMemberKump;
		this.isiJmlhCommentKump = isiJmlhCommentKump;
		this.isiJmlhLikeKump = isiJmlhLikeKump;
		*/
		
		Date time = new Date();
		long now = time.getTime();
		
		SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");;
		String tanggalSama;
		
		boolean isHappeningNow;
		
		isiIdKump = new ArrayList<String>();
		isiIdLogoKump = new ArrayList<String>();
		isiLogoKump = new ArrayList<Drawable>();
		isiJudulKump = new ArrayList<String>();
		isiPenjelasanKump = new ArrayList<String>();
		isiAlamatKump = new ArrayList<String>();
		isiTanggalKumpStart = new ArrayList<String>();
		isiTanggalKumpEnd = new ArrayList<String>();
		isiJenisKump = new ArrayList<String>();
		isiLinkURLKump = new ArrayList<String>();
		isiKoorLatKump = new ArrayList<String>();
		isiKoorLongKump = new ArrayList<String>();
		isiJmlhMemberKump = new ArrayList<String>();
		isiJmlhCommentKump = new ArrayList<String>();
		isiJmlhLikeKump = new ArrayList<String>();
		
		
		isiIdKump.add(isiId.get(0));
		isiIdLogoKump.add(isiIdLogo.get(0));
		isiLogoKump.add(isiLogo.get(0));
		isiJudulKump.add(isiJudul.get(0));
		isiPenjelasanKump.add(isiPenjelasan.get(0));
		isiAlamatKump.add(isiAlamat.get(0));
		isiTanggalKumpStart.add(isiTanggalStart.get(0));
		isiTanggalKumpEnd.add(isiTanggalEnd.get(0));
		isiJenisKump.add(isiJenis.get(0));
		isiLinkURLKump.add(isiLinkURL.get(0));
		isiKoorLatKump.add(isiKoorLat.get(0));
		isiKoorLongKump.add(isiKoorLong.get(0));
		isiJmlhMemberKump.add(isiJmlhMember.get(0));
		isiJmlhCommentKump.add(isiJmlhComment.get(0));
		isiJmlhLikeKump.add(isiJmlhLike.get(0));
		
		//pengecekan happening now saat result cuma satu
		long time_start = Date.parse(isiTanggalStart.get(0));
		if (time_start <= now) {
			tanggalSama = "Happening Now";
			isHappeningNow = true;
		}
		else {
			outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
			tanggalSama = outFormat.format(Date.parse(isiTanggalKumpStart.get(0))); 
			isHappeningNow = false;
		}
		//////////////////////////////////////////////////
		
		customAdapter = new CustomAdapter(context, isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
				isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, 
				isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump, eventOrMeetup, isHappeningNow);
		
		for (int x=1; x<=isiTanggalStart.size(); x++) {
			if (x <= isiTanggalStart.size() - 1) {
				//proses pengecekan apakah event happening now atau belum
				long time_start_1 = Date.parse(isiTanggalStart.get(x-1));
				long time_start_2 = Date.parse(isiTanggalStart.get(x));
				
				/* mencek apakah time_start item pertama DAN time_start kedua kurang dari sama dengan waktu sekarang
				 * Disini juga berfungsi menampung semua item di CustomAdapter & akan melakukannya terus selama kondisi terpenuhi
				 */
				if (time_start_1 <= now && time_start_2 <= now) {
					isiIdKump.add(isiId.get(x));
					isiIdLogoKump.add(isiIdLogo.get(x));
					isiLogoKump.add(isiLogo.get(x));
					isiJudulKump.add(isiJudul.get(x));
					isiPenjelasanKump.add(isiPenjelasan.get(x));
					isiAlamatKump.add(isiAlamat.get(x));
					isiTanggalKumpStart.add(isiTanggalStart.get(x));
					isiTanggalKumpEnd.add(isiTanggalEnd.get(x));
					isiJenisKump.add(isiJenis.get(x));
					isiLinkURLKump.add(isiLinkURL.get(x));
					isiKoorLatKump.add(isiKoorLat.get(x));
					isiKoorLongKump.add(isiKoorLong.get(x));
					isiJmlhMemberKump.add(isiJmlhMember.get(x));
					isiJmlhCommentKump.add(isiJmlhComment.get(x));
					isiJmlhLikeKump.add(isiJmlhLike.get(x));
					
					customAdapter = new CustomAdapter(context, isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
							isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, 
							isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump, eventOrMeetup, true);
				}
				/* mencek apakah time_start kedua sudah tidak memenuhi kondisi yg pertama dimana semua time_start
				 * HARUS kurang dari sama dengan waktu sekarang. Dan saat kondisi tsb tak terpenuhi maka
				 * semua item yg terkumpul tadi di CustomAdapter akan 'dilepaskan' dan ditampilkan di SeparatedListAdapter.
				 * Setelah itu, proses pembentukan CustomAdapter baru kembali dilakukan.
				 */
				else if (time_start_1 <= now && time_start_2 > now) {
					adapternya.addSection("Happening Now", customAdapter);
					
					isiIdKump = new ArrayList<String>();
					isiIdLogoKump = new ArrayList<String>();
					isiLogoKump = new ArrayList<Drawable>();
					isiJudulKump = new ArrayList<String>();
					isiPenjelasanKump = new ArrayList<String>();
					isiAlamatKump = new ArrayList<String>();
					isiTanggalKumpStart = new ArrayList<String>();
					isiTanggalKumpEnd = new ArrayList<String>();
					isiJenisKump = new ArrayList<String>();
					isiLinkURLKump = new ArrayList<String>();
					isiKoorLatKump = new ArrayList<String>();
					isiKoorLongKump = new ArrayList<String>();
					isiJmlhMemberKump = new ArrayList<String>();
					isiJmlhCommentKump = new ArrayList<String>();
					isiJmlhLikeKump = new ArrayList<String>();
					
					isiIdKump.add(isiId.get(x));
					isiIdLogoKump.add(isiIdLogo.get(x));
					isiLogoKump.add(isiLogo.get(x));
					isiJudulKump.add(isiJudul.get(x));
					isiPenjelasanKump.add(isiPenjelasan.get(x));
					isiAlamatKump.add(isiAlamat.get(x));
					isiTanggalKumpStart.add(isiTanggalStart.get(x));
					isiTanggalKumpEnd.add(isiTanggalEnd.get(x));
					isiJenisKump.add(isiJenis.get(x));
					isiLinkURLKump.add(isiLinkURL.get(x));
					isiKoorLatKump.add(isiKoorLat.get(x));
					isiKoorLongKump.add(isiKoorLong.get(x));
					isiJmlhMemberKump.add(isiJmlhMember.get(x));
					isiJmlhCommentKump.add(isiJmlhComment.get(x));
					isiJmlhLikeKump.add(isiJmlhLike.get(x));
					
					//ini untuk mengetahui nama hari
					//outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
					tanggalSama = outFormat.format(Date.parse(isiTanggalKumpStart.get(0))); 
					
					customAdapter = new CustomAdapter(context, isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
							isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, 
							isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump, eventOrMeetup, false);
					//adapter.addSection(isiTanggalKump.get(0), customAdapter2); 
				}
				/* Hampir sama dgn kondisi pertama, tapi bukan untuk Happening Now tapi untuk item yg belum terjadi sekarang.
				 * Proses pengecekan didalamnya pun juga hampir sama, namun yg membedakan cuma header untuk SeparatedListAdapter,
				 * yang menggunakan tanggal kejadian event itu.
				 */
				else if (time_start_1 > now && time_start_2 > now) {
					//waktu mulai (time_start) sama
					if (outFormat.format(Date.parse(isiTanggalStart.get(x))).equals(outFormat.format(Date.parse(isiTanggalStart.get(x-1))))) {
					//if (isiTanggal.get(x).equals(isiTanggal.get(x-1))) {
						isiIdKump.add(isiId.get(x));
						isiIdLogoKump.add(isiIdLogo.get(x));
						isiLogoKump.add(isiLogo.get(x));
						isiJudulKump.add(isiJudul.get(x));
						isiPenjelasanKump.add(isiPenjelasan.get(x));
						isiAlamatKump.add(isiAlamat.get(x));
						isiTanggalKumpStart.add(isiTanggalStart.get(x));
						isiTanggalKumpEnd.add(isiTanggalEnd.get(x));
						isiJenisKump.add(isiJenis.get(x));
						isiLinkURLKump.add(isiLinkURL.get(x));
						isiKoorLatKump.add(isiKoorLat.get(x));
						isiKoorLongKump.add(isiKoorLong.get(x));
						isiJmlhMemberKump.add(isiJmlhMember.get(x));
						isiJmlhCommentKump.add(isiJmlhComment.get(x));
						isiJmlhLikeKump.add(isiJmlhLike.get(x));
						
						//ini untuk mengetahui nama hari
						//outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
						tanggalSama = outFormat.format(Date.parse(isiTanggalKumpStart.get(0))); 
						
						customAdapter = new CustomAdapter(context, isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
								isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, 
								isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump, eventOrMeetup, false);
						//adapter.addSection(isiTanggal.get(x), customAdapter);
						//adapter.addSection("Kepet", customAdapter2);
					}
					else {
						//untuk mengetahui apakah eventnya terjadi sekarang atau belum
						/*if (tanggalSama.equals(outFormat.format(time))) {
							tanggalSama = "Happening Now";
						}*/
						///////////////////////////////////////////////////////////////
						
						adapternya.addSection(tanggalSama, customAdapter);
						
						isiIdKump = new ArrayList<String>();
						isiIdLogoKump = new ArrayList<String>();
						isiLogoKump = new ArrayList<Drawable>();
						isiJudulKump = new ArrayList<String>();
						isiPenjelasanKump = new ArrayList<String>();
						isiAlamatKump = new ArrayList<String>();
						isiTanggalKumpStart = new ArrayList<String>();
						isiTanggalKumpEnd = new ArrayList<String>();
						isiJenisKump = new ArrayList<String>();
						isiLinkURLKump = new ArrayList<String>();
						isiKoorLatKump = new ArrayList<String>();
						isiKoorLongKump = new ArrayList<String>();
						isiJmlhMemberKump = new ArrayList<String>();
						isiJmlhCommentKump = new ArrayList<String>();
						isiJmlhLikeKump = new ArrayList<String>();
						
						isiIdKump.add(isiId.get(x));
						isiIdLogoKump.add(isiIdLogo.get(x));
						isiLogoKump.add(isiLogo.get(x));
						isiJudulKump.add(isiJudul.get(x));
						isiPenjelasanKump.add(isiPenjelasan.get(x));
						isiAlamatKump.add(isiAlamat.get(x));
						isiTanggalKumpStart.add(isiTanggalStart.get(x));
						isiTanggalKumpEnd.add(isiTanggalEnd.get(x));
						isiJenisKump.add(isiJenis.get(x));
						isiLinkURLKump.add(isiLinkURL.get(x));
						isiKoorLatKump.add(isiKoorLat.get(x));
						isiKoorLongKump.add(isiKoorLong.get(x));
						isiJmlhMemberKump.add(isiJmlhMember.get(x));
						isiJmlhCommentKump.add(isiJmlhComment.get(x));
						isiJmlhLikeKump.add(isiJmlhLike.get(x));
						
						//ini untuk mengetahui nama hari
						outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
						tanggalSama = outFormat.format(Date.parse(isiTanggalKumpStart.get(0))); 
						
						customAdapter = new CustomAdapter(context, isiIdKump, isiIdLogoKump, isiLogoKump, isiJudulKump, isiPenjelasanKump, isiAlamatKump, 
								isiTanggalKumpStart, isiTanggalKumpEnd, isiJenisKump, isiLinkURLKump, isiKoorLatKump, isiKoorLongKump, 
								isiJmlhMemberKump, isiJmlhCommentKump, isiJmlhLikeKump, eventOrMeetup, false);
						//adapter.addSection(isiTanggalKump.get(0), customAdapter2); 
					}
				}
			}
			/* Berfungsi untuk 'melepaskan' CustomAdapter yg masih tersimpan yg diakibatkan oleh hasil / result item cuma 1.
			 * Setelah itu, seperti biasa, ditampilkan di SeparatedListAdapter.
			 */
			else {
				if (customAdapter != null) {
					adapternya.addSection(tanggalSama, customAdapter);
				}
			}
		}
	}
	
	/*
	public void renewArrayList(ArrayList<String> isiIdKump, ArrayList<String> isiIdLogoKump, ArrayList<Drawable> isiLogoKump, ArrayList<String> isiJudulKump,
			ArrayList<String> isiPenjelasanKump, ArrayList<String> isiAlamatKump, ArrayList<String> isiTanggalKumpStart, ArrayList<String> isiTanggalKumpEnd,
			ArrayList<String> isiJenisKump, ArrayList<String> isiLinkURLKump, ArrayList<String> isiKoorLatKump, ArrayList<String> isiKoorLongKump,
			ArrayList<String> isiJmlhMemberKump, ArrayList<String> isiJmlhCommentKump, ArrayList<String> isiJmlhLikeKump) {
		isiIdKump = new ArrayList<String>();
		isiIdLogoKump = new ArrayList<String>();
		isiLogoKump = new ArrayList<Drawable>();
		isiJudulKump = new ArrayList<String>();
		isiPenjelasanKump = new ArrayList<String>();
		isiAlamatKump = new ArrayList<String>();
		isiTanggalKumpStart = new ArrayList<String>();
		isiTanggalKumpEnd = new ArrayList<String>();
		isiJenisKump = new ArrayList<String>();
		isiLinkURLKump = new ArrayList<String>();
		isiKoorLatKump = new ArrayList<String>();
		isiKoorLongKump = new ArrayList<String>();
		isiJmlhMemberKump = new ArrayList<String>();
		isiJmlhCommentKump = new ArrayList<String>();
		isiJmlhLikeKump = new ArrayList<String>();
	}
	*/
}