import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.JFrame;

public class MainTest {
	public static void main(String[] args) throws FileNotFoundException {
		long beginningTime = System.currentTimeMillis();
		File mapData = new File(args[0]);
	
		MapGraphics.thickLines = true;
		
	
		Scanner sc = new Scanner(mapData);//initial scanner
		
		int numInts = 0;//count of intersections
		
		while(sc.nextLine().startsWith("i")){
			numInts++;
		}
		sc.close();
		 
		String intID;
		double latitude;
		double longitude;
		Intersection3 a;
		
		Scanner sc2 = new Scanner(mapData);//second scanner
		MapClass map = new MapClass(numInts);//makes the map
		
		String nowLine = sc2.nextLine();
		
		String [] information;
		
		while(nowLine.startsWith("i")) {//adding intersections
			information= nowLine.split("\t");
			intID= information[1];
			latitude = Double.parseDouble(information[2]);
			longitude = Double.parseDouble(information[3]);
			
			a = new Intersection3();
			a.distance = Integer.MAX_VALUE;
			a.IntersectionID = intID;
			a.latitude = latitude;
			a.longitude = longitude;
			a.known = false;
			nowLine = sc2.nextLine();
			map.add(a);
		}
		String roadID, int1, int2;
		Intersection3 b,c ;
		double dist;//distance
		
		while(nowLine.startsWith("r")){//adding roads
			information= nowLine.split("\t");
			roadID=information[1];
			int1=information[2];
			int2=information[3];
			
			b=MapClass.intLookup(int1);
			c=MapClass.intLookup(int2);
			
			dist=MapClass.roadDistance(b, c);
			map.add(new Road(roadID, int1, int2, dist));
			
			if(sc2.hasNextLine() == false){
				break;
			}
			nowLine = sc2.nextLine();			
		}
		String fileName;
		if(mapData.getName().equals("ur.txt")) {
			fileName = "UR Campus";
			
		}
		else if(mapData.getName().equals("monroe.txt")) {
			fileName = "Monroe County";
		
		}
		else if(mapData.getName().equals("nys.txt")) {
			fileName = "New York State";
			
		}
		else {
			fileName= "Map";
			
		}
		
		//variables to see if shortest path or minimum  weight span tree needs to be used
		boolean mapVisible= false;
		boolean shortPath= false;
		boolean minWeightSpanTree= false;
		
		String directionsBeg = "i0";
		String directionsEnd = "i1";
		
		for(int i = 0; i < args.length; i++){//looks at command lines
			
			if(args[i].equals(":show")) {
				mapVisible = true;
			}
			
			if(args[i].equals(":directions")){
				shortPath = true;
				directionsBeg = args[i+1];
				directionsEnd = args[i+2];
			}
			
			if(args[i].equals(":meridianmap")){
				minWeightSpanTree = true;
			}			
		}
		if (shortPath==true) {
			System.out.println("short path is true");
			map.shortestDistance(directionsBeg);
			System.out.println("\nThe shortest path from " + directionsBeg + " to " + directionsEnd + " is: ");
			System.out.println(MapClass.createPath(directionsEnd));
			System.out.println("Length of the path from " + directionsBeg + " to " + directionsEnd + " is: " + MapClass.shortestPathLength() + " miles.");
		}
		if(minWeightSpanTree==true) {
			map.minWeightRoads();
			System.out.println("\nRoads Taken to Create Minimum Weight Spanning Tree for " + fileName + ":\n");
			
			for(Road r : MapClass.minWeight) {
				System.out.println(r.roadID);
			}	
		}
		
		if(mapVisible == true) {//GUI visibility
			JFrame frame = new JFrame("Map");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
			frame.getContentPane().add(new MapGraphics(MapClass.roads, MapClass.intMap, MapClass.minLatitude, MapClass.maxLatitude, MapClass.minLongitude, MapClass.maxLongitude));
			frame.pack();
			frame.setVisible(true);
		}
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime-beginningTime;
		
		System.out.println("\n\nTime required to do everything: " + elapsedTime/1000 + " seconds.");
		
		
		sc2.close();
		
	}
	
}
