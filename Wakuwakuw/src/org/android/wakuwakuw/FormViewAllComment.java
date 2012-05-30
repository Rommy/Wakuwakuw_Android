package org.android.wakuwakuw;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FormViewAllComment extends Activity {
	
	private LinearLayout linearPostComment;
	private ListView listAllComment;
	private Button btnComment;
	private EditText editComment;
	private ImageButton imgBtnRefreshAllComment, imgBtnBack;
	private ProgressBar progressBarRefreshAllComment;
	
	private final String DATA = "data";
	private final String DATUM = "datum";
	private final String PARENT_ID = "parent_id";
	private final String ID_COMMENT = "id"; 
	private final String ID_USER = "user_id";
	private final String NAME_USER = "name"; 
	private final String COMMENT = "comment";
	private final String TIME_CREATED = "time_created";
	
	public String URL_All_Comments, URL_Put_Comment, URLBody, URL_Comment_In_Parent;
	public static String theId, categoryStatus;
	
	private ArrayList<Drawable> isiLogoUser;
	private ArrayList<String> isiIdComment, isiNamaUser, isiComment, isiTanggalComment, isiJmlhCommentInParent;
	
	private CustomAdapterAllComment customAdapterAllComment;
	
	public static DownloadTaskComment donlodTaskComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_view_all_comment);
		
		linearPostComment = (LinearLayout)findViewById(R.id.linearPostComment);
		listAllComment = (ListView)findViewById(R.id.listViewAllComments);
		//listAllComment.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daftar));
		/*listAllComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> ad, View v, int pos, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(), FormViewOneComment.class);
				startActivity(in);
			}
		});*/
		
		editComment = (EditText)findViewById(R.id.editMyComment);
		
		progressBarRefreshAllComment = (ProgressBar)findViewById(R.id.progressBarRefreshAllComment);
		
		imgBtnRefreshAllComment = (ImageButton)findViewById(R.id.imgButtonRefreshAllComment);
		imgBtnRefreshAllComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgBtnRefreshAllComment.setVisibility(View.INVISIBLE);
				progressBarRefreshAllComment.setVisibility(View.VISIBLE);
				
				donlodTaskComment = new DownloadTaskComment();
				donlodTaskComment.execute(URL_All_Comments);
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
			linearPostComment.setVisibility(View.VISIBLE);
			
			btnComment = (Button)findViewById(R.id.buttonSendComment);
			btnComment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if (editComment.getText().toString().equals("")) {
						Toast.makeText(getApplicationContext(), "Please insert your comment.", Toast.LENGTH_SHORT).show();
					}
					else {
						if (categoryStatus.equalsIgnoreCase("event")) {
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/event_comments";
							URLBody = "event_id=" + theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString();
							URL_All_Comments = "http://api.wakuwakuw.com/rest/event_comments?event_id=" + theId + 
								"&order_by=time_created&ascending=false";
						}
						else if (categoryStatus.equalsIgnoreCase("meetup")) {
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/meetup_comments";
							URLBody = "meetup_id=" + theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString();
							URL_All_Comments = "http://api.wakuwakuw.com/rest/meetup_comments?meetup_id=" + theId + 
								"&order_by=time_created&ascending=false";
						}
						else if (categoryStatus.equalsIgnoreCase("community")) {
							URL_Put_Comment = "http://api.wakuwakuw.com/rest/community_comments";
							URLBody = "community_id=" + theId + "&user_id=" + Timeline.idUser + "&comment=" + 
								editComment.getText().toString();
							URL_All_Comments = "http://api.wakuwakuw.com/rest/community_comments?community_id=" + theId + 
								"&order_by=time_created&ascending=false";
						}
						
						XMLParser parser = new XMLParser();
						parser.postSomething(URL_Put_Comment, URLBody);
						
						imgBtnRefreshAllComment.setVisibility(View.INVISIBLE);
						progressBarRefreshAllComment.setVisibility(View.VISIBLE);
						
						donlodTaskComment = new DownloadTaskComment();
						donlodTaskComment.execute(URL_All_Comments);
						
						editComment.setText("");
					}
					
				}
			});
		}
		else {
			linearPostComment.setVisibility(View.GONE);
		}
		
		
		//URL_All_Event_Comments = "http://api.wakuwakuw.com/rest/event_comments?event_id=200&order_by=time_created&ascending=true";
		//if (customAdapterAllComment == null) {
			if (categoryStatus.equalsIgnoreCase("event")) {
				URL_All_Comments = "http://api.wakuwakuw.com/rest/event_comments?event_id=" + theId + 
					"&order_by=time_created&ascending=false";
			}
			else if (categoryStatus.equalsIgnoreCase("meetup")) {
				URL_All_Comments = "http://api.wakuwakuw.com/rest/meetup_comments?meetup_id=" + theId + 
					"&order_by=time_created&ascending=false";
			}
			else if (categoryStatus.equalsIgnoreCase("community")) {
				URL_All_Comments = "http://api.wakuwakuw.com/rest/community_comments?community_id=" + theId + 
					"&order_by=time_created&ascending=false";
			}
			
			imgBtnRefreshAllComment.setVisibility(View.INVISIBLE);
			progressBarRefreshAllComment.setVisibility(View.VISIBLE);
			
			donlodTaskComment = new DownloadTaskComment();
			donlodTaskComment.execute(URL_All_Comments);
		//}
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
			listAllComment.setSelection(0);
			return true;
		}
		else if (item.getItemId() == R.id.refreshFormView) {
			imgBtnRefreshAllComment.setVisibility(View.INVISIBLE);
			progressBarRefreshAllComment.setVisibility(View.VISIBLE);
			
			donlodTaskComment = new DownloadTaskComment();
			donlodTaskComment.execute(URL_All_Comments);
			
			//listAllComment.setSelection(0);
			//Toast.makeText(FormViewAllComment.this, theId, Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return false;
	}
	
	public class DownloadTaskComment extends AsyncTask<String, Void, CustomAdapterAllComment> {

		@Override
		protected CustomAdapterAllComment doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (donlodTaskComment.isCancelled()) {
                //break;
				customAdapterAllComment = null;
            }
			else {
				isiIdComment = new ArrayList<String>();
				isiLogoUser = new ArrayList<Drawable>();
				isiNamaUser = new ArrayList<String>();
				isiComment = new ArrayList<String>();
				isiTanggalComment = new ArrayList<String>();
				isiJmlhCommentInParent = new ArrayList<String>();
				
				XMLParser parser = new XMLParser();
				String xml = parser.getXmlFromUrl(URL_All_Comments, Timeline.isiUsername, Timeline.isiPassword);
				Document doc = parser.getDomElement(xml.replaceAll("&lt;br /&gt;", "\n").replaceAll("&amp;", "<![CDATA[&]]>"));
				
				NodeList nl = doc.getElementsByTagName(DATUM);
				
				if (nl.getLength() != 0) {
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						// adding each child node to HashMap key => value
						
						if (parser.getValue(e, PARENT_ID) == null || parser.getValue(e, PARENT_ID).equals("") || parser.getValue(e, PARENT_ID).equals("0")) {
							String IdComment = parser.getValue(e, ID_COMMENT);
							
							//isiIdComment.add(parser.getValue(e, ID_COMMENT));
							isiIdComment.add(IdComment);
							
							String userId = parser.getValue(e, ID_USER);
							
							String URLUser = "http://api.wakuwakuw.com/rest/users/" + userId;
							XMLParser parserUser = new XMLParser();
							String xmlUser = parserUser.getXmlFromUrl(URLUser, Timeline.isiUsername, Timeline.isiPassword);
							Document docUser = parserUser.getDomElement(xmlUser);
							NodeList nlUser = docUser.getElementsByTagName(DATA);
							Element elUser = (Element) nlUser.item(0);
							isiNamaUser.add(parserUser.getValue(elUser, NAME_USER));
							
							isiLogoUser.add(LoadImageFromWeb("http://wakuwakuw.com/img/user/" + userId + "?size=feed"));
							
							isiComment.add(parser.getValue(e, COMMENT));
							
							Date time = new Date();
							time.setTime(Long.parseLong(parser.getValue(e, TIME_CREATED)) * 1000);
							//SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' hh:mm aaa");
							//String tanggal = outFormat.format(time);
							//isiTanggalComment.add(tanggal);
							
							isiTanggalComment.add(time.toLocaleString());
							
							
							//untuk mendapatkan jumlah comment di dalam comment
							if (FormViewAllComment.categoryStatus.equalsIgnoreCase("event")) {
								URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/event_comments?parent_id=" + IdComment +
									"&order_by=time_created&ascending=true";
							}
							else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("meetup")) {
								URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/meetup_comments?parent_id=" + IdComment +
									"&order_by=time_created&ascending=true";
							}
							else if (FormViewAllComment.categoryStatus.equalsIgnoreCase("community")) {
								URL_Comment_In_Parent = "http://api.wakuwakuw.com/rest/community_comments?parent_id=" + IdComment +
									"&order_by=time_created&ascending=true";
							}
							
							XMLParser parserComment = new XMLParser();
							String xmlComment= parserComment.getXmlFromUrl(URL_Comment_In_Parent, Timeline.isiUsername, Timeline.isiPassword);
							Document docComment = parserComment.getDomElement(xmlComment);
							NodeList nlComment = docComment.getElementsByTagName(DATUM);
							int jmlhComment = nlComment.getLength();
							
							isiJmlhCommentInParent.add(Integer.toString(jmlhComment));
							////////////////////////////////////////////////////////////
						}
					}
					
					customAdapterAllComment = new CustomAdapterAllComment(FormViewAllComment.this, isiIdComment, isiLogoUser, isiNamaUser, 
							isiComment, isiTanggalComment, isiJmlhCommentInParent);
					//Timeline.listCommunities = new ListView(ChangeCity.this);
					//Timeline.listCommunities.setAdapter(customAdapterCommun);
				}
				else {
					customAdapterAllComment = null;
					//Timeline.listViewCategory.setAdapter(null);
				}
				//////////////////////////////////////////
				
				//communsLoadFinished = true;
			}
			
			return customAdapterAllComment;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			if (donlodTaskComment.isCancelled()) {
                //break;
				customAdapterAllComment = null;
            }
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(CustomAdapterAllComment adapter) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			Toast.makeText(FormViewAllComment.this, "Finished", Toast.LENGTH_SHORT).show();
			
			imgBtnRefreshAllComment.setVisibility(View.VISIBLE);
			progressBarRefreshAllComment.setVisibility(View.GONE);
			
			if (adapter == null) {
				listAllComment.setBackgroundResource(R.drawable.background_none);
			}
			else {
				listAllComment.setBackgroundDrawable(null);
				listAllComment.setBackgroundColor(Color.parseColor("#EBEBEB"));
			}
			
			listAllComment.setAdapter(adapter);
			
			
			//if (eventsLoadFinished && meetupsLoadFinished && communsLoadFinished)
			//	Timeline.progressBarTimeline.setVisibility(View.GONE);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			customAdapterAllComment = null;
			super.onCancelled();
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
