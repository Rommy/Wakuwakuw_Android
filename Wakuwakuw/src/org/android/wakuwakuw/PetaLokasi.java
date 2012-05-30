package org.android.wakuwakuw;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PetaLokasi extends MapActivity {
	
	public MapView mapView;
	public MapController mapController;
	public List<Overlay> listOverlays;
	public Drawable drawable;
	public MarkerPeta penandaPeta;
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.map_event_meetup_location);
		
		mapView = (MapView) findViewById(R.id.mapEventLocation);
        mapView.setBuiltInZoomControls(true);
        //mapView.setStreetView(true);
        
        mapController = mapView.getController();
        mapController.setZoom(18);
        
        listOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.arrow);
        penandaPeta = new MarkerPeta(drawable, this);
        
        GeoPoint point = new GeoPoint((int)(Float.parseFloat(FormDetail.koorLatAcara) * 1E6), 
        		(int)(Float.parseFloat(FormDetail.koorLongAcara) * 1E6));
        OverlayItem overlayitem = new OverlayItem(point, null, null);
        mapController.animateTo(point);
        
        penandaPeta.addOverlay(overlayitem);  
        listOverlays.add(penandaPeta);
        
        mapView.invalidate();
	}
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
