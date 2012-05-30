package org.android.wakuwakuw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MarkerPeta extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	private final int mRadius = 20;
	private Location location;
	public String addressString;
	
	private final String RESPONSE = "Response";
	private final String ADMINISTRATIVE_AREA_NAME = "AdministrativeAreaName";
	private final String LOCALITY_NAME = "LocalityName";
	
	private String administrativeAreaName, localityName;
	
	private XMLParser parser;
	
	public MarkerPeta(Drawable defaultMarker, Context context) {
		// TODO Auto-generated constructor stub
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
	
	/*
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// TODO Auto-generated method stub
		if (event.getAction() == 1) {
			//GeoPoint p = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
			//Toast.makeText(mContext, p.getLatitudeE6() / 1E6 + ", " + p.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show();
			
			GeoPoint p = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
			String latitudePoint = Double.toString((double)(p.getLatitudeE6()/1E6));
			String longitudePoint = Double.toString((double)(p.getLongitudeE6()/1E6));
			
			//Toast.makeText(mContext, latitudePoint + ", " + longitudePoint, Toast.LENGTH_SHORT).show();
			
			String URL_Get_CityName = "http://maps.google.com/maps/geo?q=" + 
				latitudePoint + "," + longitudePoint + "&output=xml&oe=utf8&sensor=true";
			
			parser = new XMLParser();
			String xml = parser.getXmlFromUrl(URL_Get_CityName);
			
			if (xml != null) {
				Document doc = parser.getDomElement(xml);
				NodeList nl = doc.getElementsByTagName(RESPONSE);
				
				if (nl.getLength() != 0) {
					for (int i = 0; i < nl.getLength(); i++) {
						Element e = (Element) nl.item(i);
						
						administrativeAreaName = parser.getValue(e, ADMINISTRATIVE_AREA_NAME);
						localityName = parser.getValue(e, LOCALITY_NAME);
						
					}
				}
				else {
					administrativeAreaName = ""; localityName = "";
				}
				
				
				if (localityName == null || localityName.equals(""))
					Toast.makeText(mContext, administrativeAreaName, Toast.LENGTH_SHORT).show();
				else 
					Toast.makeText(mContext, localityName, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(mContext, "No Connection", Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}
	*/
	
	@Override
	public boolean onTap(int index) {
		// TODO Auto-generated method stub
		OverlayItem item = mOverlays.get(index);
		
		if (item.getTitle() != null && item.getSnippet() != null) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
		}
		
		return true;
	}
	
	/*public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// TODO Auto-generated method stub
		if (event.getAction() == 1) {
			GeoPoint p = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
			Toast.makeText(mContext, p.getLatitudeE6() / 1E6 + ", " + p.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show();
		}
		return false;
	}*/
	
	/*public boolean onTouchEvent(MotionEvent event, MapView mapView) 
    {   
        //---when user lifts his finger---
        // untuk mendapatkan nama jalan
        if (event.getAction() == 1) {                
            GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());

            Geocoder geoCoder = new Geocoder(mContext, Locale.getDefault());
            
            try {
                List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6()/1E6, p.getLongitudeE6()/1E6, 1);
                
                String add = "";
                if (addresses.size() > 0) 
                {
                    for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++)
                    {
                    	add += addresses.get(0).getAddressLine(i) + "\n";
                    	add += addresses.get(0).getCountryName() + "\n";
                    }
                }

                Toast.makeText(mContext, add, Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {                
                e.printStackTrace();
            }   
            return false;
        }
        else                
            return false;
    } */
}
