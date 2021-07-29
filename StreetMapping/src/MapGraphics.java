import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
public class MapGraphics extends JPanel {//Map GUI Class
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection3> intMap;
	public static boolean thickLines = false;
	
	public static double minLat;
	public static double minLong;
	public static double maxLat;
	public static double maxLong;
	public static double xScale, yScale;
	
	//constructor for MapGraphics Class
	public MapGraphics(ArrayList<Road> roads, HashMap<String, Intersection3> intMap, double minLat, double maxLat, double minLong, double maxLong){
		
		MapGraphics.roads = roads;
		MapGraphics.intMap = intMap;
		
		MapGraphics.minLat = minLat;
		MapGraphics.maxLat = maxLat;
		MapGraphics.minLong = minLong;
		MapGraphics.maxLong = maxLong;
		
		setPreferredSize(new Dimension(800, 800));
	}
	
	//paint component for graph
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		g2.setColor(Color.BLACK);
		
		//increase line thickness
		if(thickLines) {
			g2.setStroke(new BasicStroke(3));
		}
		
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);
		
		Intersection3 int1, int2;
		
		double x1, y1, x2, y2;
		
		for(Road r : roads){//graphs roads
			
			scale();
			
			int1 = intMap.get(r.intersect1);
			int2 = intMap.get(r.intersect2);
			
			x1 = int1.longitude;
			y1 = int1.latitude;
			x2 = int2.longitude;
			y2 = int2.latitude;
		
			g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
					(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
			
		}
		//graphs the directions using the shortest path 
				if(MapClass.shortestPath != null) {
					
					g2.setColor(Color.RED);
					
					for(int i = 0; i < MapClass.shortestPath.length - 1; i++) {
						
						x1 = MapClass.shortestPath[i].longitude;
						y1 = MapClass.shortestPath[i].latitude;
						x2 = MapClass.shortestPath[i+1].longitude;
						y2 = MapClass.shortestPath[i+1].latitude;
						
						g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
								(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

					}
					
					
				}
				
				//graphing map
				if(MapClass.minWeight != null) {
					for(Road road : MapClass.minWeight) {
						
						g2.setColor(Color.BLUE);
						
						int1 = intMap.get(road.intersect1);
						int2 = intMap.get(road.intersect2);
						
						x1 = int1.longitude;
						y1 = int1.latitude;
						x2 = int2.longitude;
						y2 = int2.latitude;
					
						g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
								(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
						
					}
				}
	}
	
public void scale() {//scales
		
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);
		
	}
	
}
