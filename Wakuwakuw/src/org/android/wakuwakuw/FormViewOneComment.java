package org.android.wakuwakuw;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.android.wakuwakuw.FormViewAllComment.DownloadTaskComment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FormViewOneComment extends Activity {
	
	private final String DATA = "data";
	private final String DATUM = "datum";
	private final String PARENT_ID = "parent_id";
	private final String ID_COMMENT = "id"; 
	private final String ID_USER = "user_id";
	private final String NAME_USER = "name"; 
	private final String COMMENT = "comment";
	private final String TIME_CREATED = "time_created";
	
	private ScrollView scrlUserComment;
	private LinearLayout linear, linearUserComment, linearPostOneComment;
	private ImageButton imgBtnRefreshOneComment, imgBtnBack;
	private ProgressBar progressBarRefreshOneComment;
	private RelativeLayout relativeAddComment[] = new RelativeLayout[10];
	private Button btnSendComment;
	private EditText editComment;
	private ImageView imgFirstUserComment;
	private TextView txtFirstUserName, txtFirstUserComment, txtFirstUserCommentDate;
	
	public static Drawable logoFirstUserComment;
	public static String idComment, firstUserName, firstUserComment, firstUserCommentDate;
	public String URL_Comment_In_Parent, URL_Put_Comment, URL_Body;
	
	private ArrayList<Drawable> logoUserInParent;
	private ArrayList<String> usernameInParent;
	private ArrayList<String> userCommentInParent;
	private ArrayList<String> commentDateInParent;
	
	private SimpleDateFormat outFormat;
	private Date time;
	
	public static DownloadTaskCommentInParent donlodTaskCommentInParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_view_one_comment);
		
		outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' hh:mm aaa");
		time = new Date();
		//long timeInNumber = time.getTime() / 1000;
		
		scrlUserComment = (ScrollView)findViewById(R.id.scrollViewUserComment);
		linearUserComment = (LinearLayout)findViewById(R.id.linearUserComment);
		linearPostOneComment = (LinearLayout)findViewById(R.id.linearPostOneComment);
		editComment = (EditText)findViewById(R.id.editMyCommentAgain);
		imgFirstUserComment = (ImageView)findViewById(R.id.imgUser);
		txtFirstUserName = (TextView)findViewById(R.id.txtNamaUser);
		txtFirstUserComment = (TextView)findViewById(R.id.txtCommentUser);
		txtFirstUserCommentDate = (TextView)findViewById(R.id.txtTglCommentUser);
		
		imgFirstUserComment.setImageDrawable(logoFirstUserComment);
		txtFirstUserName.setText(firstUserName); txtFirstUserComment.setText(firstUserComment);
		txtFirstUserCommentDate.setText(firstUserCommentDate);
		
		progressBarRefreshOneComment = (ProgressBar)findViewById(R.id.progressBarRefreshOneComment);
		
		imgBtnRefreshOneComment = (ImageButton)findViewById(R.id.imgButtonRefreshOneComment);
		imgBtnRefreshOneComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//linearUserComment.removeAllViews();
				
				imgBtnRefreshOneComment.setVisibility(View.INVISIBLE);
				progressBarRefreshOneComment.setVisibility(View.VISIBLE);
				
				donlodTaskCommentInParent = new DownloadTaskCommentInParent();
				donlodTaskCommentInParent.execute(URL_Comment_In_Parent);
			}
		});
		
		imgBtnBack = (ImageButton)findViewById(R.id.imgButtonBack);
		imgBtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		if (!Timeline.isiUsername.equals("") && !Timeline.isiPassword.equals("")) {
			linearPostOneComment.setVisibility(View.VISIBLE);
			
			btnSendComment = (Button)findViewById(R.id.buttonSendCommentAgain);
			btnSendComment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
					//View menuLayout = inflater.inflate(R.layout.your_menu_layout, mainLayout, true);
					//View menuLayout = inflater.inflate(R.layout.form_view_one_comment, linear, false);
					
					if (editComment.getText().toString().equals("")) {
						Toast.makeText(getApplicationContext(), "Please insert your comment.", Toast.LENGTH_SHORT).show();
					}
					else {
						String timeNow = outFormat.format(Date.parse(time.toLocaleString()));
						//addComment(Timeline.logoUser, Timeline.namaUser, editComment.getText().toString(), timeNow);
						
						if (FormViewAllComment.categoryStatus.equalsIgnoreCase("event")) {
							URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/event_comments?parent_id=" + idComment +
								"&order_by=time_created&ascending=true";
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/event_comments";
							URL_Body = "event_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString() + "&parent_id=" + idComment;
						}
						else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("meetup")) {
							URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/meetup_comments?parent_id=" + idComment +
								"&order_by=time_created&ascending=true";
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/meetup_comments";
							URL_Body = "meetup_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString() + "&parent_id=" + idComment;
						}
						else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("community")) {
							URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/community_comments?parent_id=" + idComment +
								"&order_by=time_created&ascending=true";
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/community_comments";
							URL_Body = "community_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString() + "&parent_id=" + idComment;
						}
						
						XMLParser parser = new XMLParser();
						parser.postSomething(URL_Put_Comment, URL_Body);
						
						//linearUserComment.removeAllViews();
						
						imgBtnRefreshOneComment.setVisibility(View.INVISIBLE);
						progressBarRefreshOneComment.setVisibility(View.VISIBLE);
						
						donlodTaskCommentInParent = new DownloadTaskCommentInParent();
						donlodTaskCommentInParent.execute(URL_Comment_In_Parent);
						
						editComment.setText("");
					}
				}
			});
		}
		else {
			linearPostOneComment.setVisibility(View.GONE);
		}
		
		//Toast.makeText(getApplicationContext(), idComment, Toast.LENGTH_SHORT).show();
		
		if (FormViewAllComment.categoryStatus.equalsIgnoreCase("event")) {
			URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/event_comments?parent_id=" + idComment +
				"&order_by=time_created&ascending=true";
			//URL_Put_Comment = "http://api.wakuwakuw.com/rest/event_comments";
			//URL_Body = "event_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
			//	editComment.getText().toString() + "&parent_id=" + idComment;
		}
		else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("meetup")) {
			URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/meetup_comments?parent_id=" + idComment +
				"&order_by=time_created&ascending=true";
			//URL_Put_Comment = "http://api.wakuwakuw.com/rest/meetup_comments";
			//URL_Body = "meetup_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
			//	editComment.getText().toString() + "&parent_id=" + idComment;
		}
		else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("community")) {
			URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/community_comments?parent_id=" + idComment +
				"&order_by=time_created&ascending=true";
			//URL_Put_Comment = "http://api.wakuwakuw.com/rest/community_comments";
			//URL_Body = "community_id=" + FormViewAllComment.theId + "&user_id=" + Timeline.idUser + "&comment=" + 
			//	editComment.getText().toString() + "&parent_id=" + idComment;
		}
		
		imgBtnRefreshOneComment.setVisibility(View.INVISIBLE);
		progressBarRefreshOneComment.setVisibility(View.VISIBLE);
		
		donlodTaskCommentInParent = new DownloadTaskCommentInParent();
		donlodTaskCommentInParent.execute(URL_Comment_In_Parent);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//return super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_form_view, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.jumpToTopFormView) {
			scrlUserComment.scrollTo(0, 0);
			return true;
		}
		else if (item.getItemId() == R.id.refreshFormView) {
			//linearUserComment.removeAllViews();
			
			imgBtnRefreshOneComment.setVisibility(View.INVISIBLE);
			progressBarRefreshOneComment.setVisibility(View.VISIBLE);
			
			donlodTaskCommentInParent = new DownloadTaskCommentInParent();
			donlodTaskCommentInParent.execute(URL_Comment_In_Parent);
			
			scrlUserComment.scrollTo(0, 0);
			return true;
		}
		
		return false;
	}
	
	public void addComment(int index, Drawable logoUser, String username, String comment, String date) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		
		float dp40 = 40f;
		float fpixels40 = metrics.density * dp40;
		int pixels40 = (int) (fpixels40 + 0.5f);
		//int pixels = (int) (metrics.density * dp + 0.5f);
		
		float dp15 = 15f;
		float fpixels15 = metrics.density * dp15;
		int pixels15 = (int) (fpixels15 + 0.5f);
		
		float dp10 = 10f;
		float fpixels10 = metrics.density * dp10;
		int pixels10 = (int) (fpixels10 + 0.5f);

		float dp5 = 5f;
		float fpixels5 = metrics.density * dp5;
		int pixels5 = (int) (fpixels5 + 0.5f);
		
		//parameter buat Relative Layout
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rp.setMargins(pixels10, pixels10, pixels5, pixels10);
		
		relativeAddComment[index] = new RelativeLayout(this);
		relativeAddComment[index].setLayoutParams(rp);
		
		//parameter buat ImageView
		RelativeLayout.LayoutParams rpImage = new RelativeLayout.LayoutParams(pixels40, pixels40);
		rpImage.setMargins(pixels10, pixels10, pixels5, pixels5);
		rpImage.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	
		ImageView imgUser = new ImageView(this);
		imgUser.setLayoutParams(rpImage);
		imgUser.setImageDrawable(logoUser);
		imgUser.setId(1);
		
		//parameter buat TextView UserName
		RelativeLayout.LayoutParams rpUserName = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//rpUserName.setMargins(0, pixels5, 0, 0);
		rpUserName.addRule(RelativeLayout.ALIGN_TOP, imgUser.getId());
		rpUserName.addRule(RelativeLayout.RIGHT_OF, imgUser.getId());
		
		TextView txtUserName = new TextView(this);
		txtUserName.setLayoutParams(rpUserName);
		txtUserName.setText(username);
		txtUserName.setTextColor(Color.BLACK);
		txtUserName.setTypeface(null, Typeface.BOLD);
		txtUserName.setId(2);
		
		
		//parameter buat TextView User Comment
		RelativeLayout.LayoutParams rpUserComment = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rpUserComment.setMargins(0, pixels5, 0, 0);
		rpUserComment.addRule(RelativeLayout.RIGHT_OF, imgUser.getId());
		rpUserComment.addRule(RelativeLayout.BELOW, txtUserName.getId());
		
		TextView txtUserComment = new TextView(this);
		txtUserComment.setLayoutParams(rpUserComment);
		txtUserComment.setText(comment);
		txtUserComment.setTextColor(Color.BLACK);
		txtUserComment.setId(3);
		
		//parameter buat TextView tanggal user comment
		RelativeLayout.LayoutParams rpUserCommentDate = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rpUserCommentDate.setMargins(0, pixels5, 0, pixels5);
		rpUserCommentDate.addRule(RelativeLayout.RIGHT_OF, imgUser.getId());
		rpUserCommentDate.addRule(RelativeLayout.BELOW, txtUserComment.getId());
		
		TextView txtUserCommentDate = new TextView(this);
		txtUserCommentDate.setLayoutParams(rpUserCommentDate);
		txtUserCommentDate.setText(date);
		txtUserCommentDate.setTextColor(Color.BLACK);
		txtUserCommentDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		txtUserCommentDate.setId(4);

		
		
		relativeAddComment[index].addView(imgUser);
		relativeAddComment[index].addView(txtUserName);
		relativeAddComment[index].addView(txtUserComment);
		relativeAddComment[index].addView(txtUserCommentDate);
		
		
		linearUserComment.addView(relativeAddComment[index]);
	}
	
	public class DownloadTaskCommentInParent extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			logoUserInParent = new ArrayList<Drawable>();
			usernameInParent = new ArrayList<String>();
			userCommentInParent = new ArrayList<String>();
			commentDateInParent = new ArrayList<String>();
			
			int jmlhComment = 0;
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(URL_Comment_In_Parent, Timeline.isiUsername, Timeline.isiPassword);
			Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
			
			NodeList nl = doc.getElementsByTagName(DATUM);
			
			if (nl.getLength() != 0) {
				//percobaan kacau
				jmlhComment = nl.getLength();
				
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					
					//String idCommentInParent = parser.getValue(e, ID_COMMENT);
					
					String userId = parser.getValue(e, ID_USER);
					
					String URLUser = "http://api.wakuwakuw.com/rest/users/" + userId;
					XMLParser parserUser = new XMLParser();
					String xmlUser = parserUser.getXmlFromUrl(URLUser, Timeline.isiUsername, Timeline.isiPassword);
					Document docUser = parserUser.getDomElement(xmlUser);
					NodeList nlUser = docUser.getElementsByTagName(DATA);
					Element elUser = (Element) nlUser.item(0);
					//String usernameInParent = parserUser.getValue(elUser, NAME_USER);
					usernameInParent.add(parserUser.getValue(elUser, NAME_USER));
					
					//Drawable logoUserInParent = LoadImageFromWeb("http://wakuwakuw.com/img/user/" + userId + "?size=feed");
					logoUserInParent.add(LoadImageFromWeb("http://wakuwakuw.com/img/user/" + userId + "?size=feed"));
					
					//String userCommentInParent = parser.getValue(e, COMMENT);
					userCommentInParent.add(parserUser.getValue(e, COMMENT));
					
					Date time = new Date();
					time.setTime(Long.parseLong(parser.getValue(e, TIME_CREATED)) * 1000);
					SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' hh:mm aaa");
					String tanggal = outFormat.format(time);
					commentDateInParent.add(tanggal);
					
					//addComment(logoUserInParent, usernameInParent, userCommentInParent, tanggal);
				}
			}
			
			return jmlhComment;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			imgBtnRefreshOneComment.setVisibility(View.VISIBLE);
			progressBarRefreshOneComment.setVisibility(View.GONE);
			
			linearUserComment.removeAllViews();
			
			if (result != 0) {
				relativeAddComment = new RelativeLayout[result];
				
				for (int i = 0; i < result; i++) {
					addComment(i, logoUserInParent.get(i), usernameInParent.get(i), userCommentInParent.get(i), commentDateInParent.get(i));
				}
			}
			
			Toast.makeText(FormViewOneComment.this, "Finished", Toast.LENGTH_SHORT).show();
		}
		
	}
	//cara ngeload image dari URL
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
