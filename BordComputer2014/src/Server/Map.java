package Server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Map extends JPanel {

	private static final long serialVersionUID = -6192313289004122606L;
	final JMapViewer map = new JMapViewer();
    private  JCheckBox button = new JCheckBox("Auto Zoom to Fit all points");
    private ArrayList<MapMarker> mapMarkerList = new ArrayList<MapMarker>();
    
    private HashMap<Socket, double[]> points;

    public Map() {

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        JLabel helpLabel = new JLabel("Use right button to move,"
                + " left double click or wheel to zoom.");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setDisplayToFitMapMarkers();
            	
            }
        });
        button.setSelected(true);
        map.getZoomContolsVisible();
        panel.add(button);
        panel.add(helpLabel);
        add(map, BorderLayout.CENTER);

        points = new HashMap<Socket, double[]>();
    }
    
    public synchronized void addPoint(Socket socket, double lat, double lon) {
    	double[] latlon = {lat, lon};
    	points.put(socket, latlon);
    }
    
    public void addPoint(double lat, double lon){
    	MapMarkerDot point = new MapMarkerDot(lat, lon);
        map.addMapMarker(point);
        mapMarkerList.add(point);
    	if(button.isSelected()){
    		map.setDisplayToFitMapMarkers();
    	}
       
    }
    
    public void clear(){
    	java.util.Iterator<MapMarker> iterator = mapMarkerList.iterator();
    	while(iterator.hasNext()){
        	map.removeMapMarker(iterator.next());

		}
    	mapMarkerList.clear();
    }
    
    public synchronized void removeSocket(Socket socket) {
    	points.remove(socket);
    }

	public synchronized void printAllPoints() {
		clear();
		
		for (double[] latlon : points.values()) {
			addPoint(latlon[0], latlon[1]);
		}
	}
}
