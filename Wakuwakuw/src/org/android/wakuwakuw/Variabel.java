package org.android.wakuwakuw;

public interface Variabel {
	public String URL_Events = null;
	public String URL_Meetups = null;
	public String URL_Communs = null;
	
	public final String DATA = "data";
	public final String DATUM = "datum";
	public final String NAME = "name";
	public final String USER_CITY_ID = "city_id";
	public final String CITY_ID = "id";
	public final String CITY_NAME = "name";
	
	public final String COUNTRY_ID = "country_id";
	public final String COUNTRY_NAME = "name";
	
	//untuk events & meetups//
	public final String ID = "id"; 
	public final String TITLE = "title"; 
	public final String DESCRIPTION = "description";
	public final String LOCATION = "location";
	public final String TIME_START = "time_start";
	public final String TIME_END = "time_end";
	public final String COMMUNITY_ID = "community_id";
	public final String COMMUNITY_CATEGORY_ID = "community_category_id";
	public final String SLUG = "slug";
	public final String LATITUDE = "lat";
	public final String LONGITUDE = "lng";
	
	public final String EVENT_CATEGORY_ID = "event_category_id";
	public final String CATEGORY_NAME = "name";
	
	//untuk communities//
	//public final String ID_COMMUN = "id"; 
	public final String NAME_COMMUN = "name"; 
	public final String DESCRIP_COMMUN = "description";
	public final String NAME_URI = "name_uri";
	public final String TOTAL_MEMBERS = "total_members";
	public final String TOTAL_GUESTS = "total_guests";
	public final String TOTAL_LIKES = "total_likes";
	public final String TOTAL_COMMENTS = "total_comments";
	
	public final String USER_ID = "user_id";
	public final String GENDER = "gender";
	public final String BIRTHDAY = "birthday";
	public final String EMAIL = "email";
	
	//untuk photos
	public final String PHOTO_ID = "photo_id";
	public final String CAPTION = "caption";
	public final String TIME_CREATED = "time_created";
	public final String TIME_UPDATED = "time_updated";
}
