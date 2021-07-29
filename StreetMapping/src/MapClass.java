import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class MapClass {
	//creates graph as a hash map
	public HashMap<String, LinkedList> g;//graph
	public static int numInts;
	
	//instance variables
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection3> intMap;//intersection map
	public static PriorityQueue<Intersection3> unknownIntHeap;
	public static PriorityQueue<Road> minSpanRoads;//minimum weight spanning roads
	public static HashMap<String, HashSet<String>> intSets;
	public static ArrayList<Road> minWeight;
	public static Intersection3 [] shortestPath;//shortest path Dijkstra's algorithm

	public static double minLatitude;
	public static double maxLatitude;
	public static double minLongitude;
	public static double maxLongitude;
	
	//constructor method
	public MapClass(int numVertices){
		g = new HashMap<String, LinkedList>();
		
		roads = new ArrayList<Road>();
		intMap = new HashMap<String, Intersection3>();
		numInts = numVertices;
		
		//compares the heaps of intersections
		Comparator<Intersection3> comparatorHeap = new Comparator<Intersection3>() {
        	@Override
        	public int compare(Intersection3 intersection1, Intersection3 intersection2){
            	if(intersection1.distance < intersection2.distance) {
            		return -1;
            	}
            	else {
            		return 1;
            	}
            }
		};
		
		//defines heap for unknown intersections
		unknownIntHeap = new PriorityQueue<Intersection3>(numVertices, comparatorHeap);
		
		//compares the heaps of roads
		Comparator<Road> comparatorHeap2 = new Comparator<Road>(){
        	@Override
        	public int compare(Road road1, Road road2){
            	if(road1.distance < road2.distance) {
            		return -1;
            	}
            	else {
            		return 1;
            	}
            }
		};
		
		//defines heap of the roads
		minSpanRoads = new PriorityQueue<Road>(numVertices*3, comparatorHeap2);
		
		//this sets latitude/longitude values to a starting integer
		minLatitude = minLongitude = Integer.MAX_VALUE;
		maxLatitude = maxLongitude = Integer.MIN_VALUE;		
	}//end of constructor
	
	//method to return size of intersections in the graph 
	public int size() {
		return g.size();//graph size
	}
	
	//creates path method
    public static String createPath(String lastID){
		Intersection3 tempID = intMap.get(lastID);//find ID that has lastID as it's ID		
		String [] path = new String[intMap.size()];//path that has the order of the nodes from start to end of vertex
		
		int count = 0;
		
		while(tempID.path != null){
			path[count] = tempID.IntersectionID;
			tempID = tempID.path;
			count++;
		}
		
		path[count] = tempID.IntersectionID;//adds beginning vertex to the path
		
		int completePath = 0;
		
		for(int i = 0; i < path.length; i++){//finds length of path
			if(path[i] == null) {
				completePath = i;
				break;
			}
		}
		shortestPath = new Intersection3 [completePath];//graphs directions		
		
		for(int i = 0; i < completePath; i++){
			shortestPath[i] = intMap.get(path[i]);
		}		
		String lastPath = "";
		
		for(int i = count ; i > -1; i--) {//creates a string path from beginning to end of vertex
			lastPath = lastPath + path[i] + "\n";
		}
		return lastPath;
	}
    
    //distance needed to travel between intersections
    public static double shortestPathLength() {		
		return shortestPath[0].distance * 0.000621371;//conversion from meters to miles
	}
	
    //finds minimum unknown vertex 
    public static Intersection3 minUnknownVertex() {		
		Intersection3 temp = unknownIntHeap.remove();		
		return intMap.get(temp.IntersectionID);		
	}
    
    //makes a set for spanning tree algorithm
    public void makeSet() {
    	intSets = new HashMap<String, HashSet<String>>();
		HashSet<String> ints;//intersections
		Iterator<Entry<String, LinkedList>> iterator = g.entrySet().iterator();//iterates over entries
		
		while (iterator.hasNext()) {
	        HashMap.Entry<String, LinkedList> pair = (HashMap.Entry<String, LinkedList>) iterator.next();
	        ints = new HashSet<String>();      
	        ints.add(pair.getKey());        
	        intSets.put(pair.getKey(), ints);        
		}
    }
	
    //determines minimum weight spanning tree of roads
    public void minWeightRoads() {
    	makeSet();
    	minWeight = new ArrayList<Road>();
    	Road roadNow;
    	
    	HashSet<String> x;//x and y are intersections of roads
		HashSet<String> y;
		
		while(minSpanRoads.size() > 0) {	
			roadNow = minSpanRoads.remove();//find road with shortest distance from the heap of roads			
			x = intSets.get(roadNow.intersect1);
			y = intSets.get(roadNow.intersect2);
			
			if(!x.equals(y)){
				minWeight.add(roadNow);
				x.addAll(y);//joins two x and y sets
				for(String intID: x) {
					intSets.put(intID, x);
				}
			}
		}		
    }
    
    //method to find paths that have the shortest distance
    public void shortestDistance(String intID) {
    	Intersection3 beginning = intMap.get(intID);
    	unknownIntHeap.remove(beginning);
    	beginning.distance=0;
    	unknownIntHeap.add(beginning);
    	double amount;//cost
    	int numUnknownVertices = intMap.size();
    	
    	while(numUnknownVertices > 0){
			
			//smallestUnknownVertex() returns the unknown vertex with the smallest distance from the heap of intersections
			Intersection3 temp = minUnknownVertex();
			
			temp.known = true;
			numUnknownVertices--;
			
			//LinkedList has all the roads connected to the current smallest unknown vertex
			LinkedList nowVertex = g.get(temp.IntersectionID);
			
			//get the first edge from the linked list
			Edge nowRoad = nowVertex.head.edge;
			Intersection3 nowInt;
			
			//while there are still edges in the linked list
			while(nowRoad != null) {
				
				//get the correct intersection in the edge
				//we want the intersection that is NOT the same as the one we are currently visiting
				if(nowRoad.road.intersect1.equals(temp.IntersectionID)) {
					nowInt = intMap.get(nowRoad.road.intersect2);
				}
				else {
					nowInt = intMap.get(nowRoad.road.intersect1);
				}
				
				//if the intersection is unknown
				if(nowInt.known == false) {
					
					//find the cost to get from the current vertex to its adjacent one
					amount = findAmount(temp, nowInt);
					
					if(temp.distance + amount < nowInt.distance) {
						
						//update the intersection by removing it from the heap
						unknownIntHeap.remove(nowInt);
						
						//changing the values
						nowInt.distance = temp.distance + amount;
						nowInt.path = temp;
						
						//and adding it back into the heap
						unknownIntHeap.add(nowInt);
					}
				}
				//get to the next edge in the linked list
				nowRoad = nowRoad.next;
			}
		}
    }
    
    //determine amount between two connected intersections
    public double findAmount(Intersection3 int1, Intersection3 int2) {
		LinkedList temp = g.get(int1.IntersectionID);//make linked list for intersection 1
		return temp.findAmount(int2);//use recursion to find amount for intersection 2
	}
    
    //checks to see if two intersections are connected
    public boolean checkConnected(Intersection3 int1, Intersection3 int2) {
    	LinkedList temp = g.get(int1.IntersectionID);
    	return temp.checkConnected(int2);//recursion to call on linked list
    }
    
    //method to add intersection to the graph
    public void add(Intersection3 int1) {
    	if(int1.latitude < minLatitude){//if statements to update lat/long values
			minLatitude = int1.latitude;
		}
    	if(int1.latitude > maxLatitude){
			maxLatitude = int1.latitude;
		}
		
		if(int1.longitude < minLongitude){
			minLongitude = int1.longitude;
		}
		
		if(int1.longitude > maxLongitude){
			maxLongitude = int1.longitude;
		}
		
		intMap.put(int1.IntersectionID, int1);
		unknownIntHeap.add(int1);
		LinkedList list= new LinkedList();//creates new Linked List
		list.add(int1);
		g.put(int1.IntersectionID, list);
    }
    
    //method to add a road to the graph
    public void add (Road r) {
    	LinkedList i1 = g.get(r.intersect1);
		LinkedList i2 = g.get(r.intersect2);
		i1.addRoad(r);
		i2.addRoad(r);
		
		minSpanRoads.add(r);
		roads.add(r);
    }
    
    //method that returns the intersection that goes with its intersection ID
    public static Intersection3 intLookup(String intID) {
		return intMap.get(intID);
	}
    
    //finds distance between intersection objects
    public static double roadDistance(Intersection3 int1, Intersection3 int2) {
		return findDist(int1.latitude, int1.longitude, int2.latitude, int2.longitude);
	}
    
    //calculates distance between long and lats
    public static double findDist(double lat1, double long1, double lat2, double long2) {
    	int Radius = 6371000;//earth radius
		
		lat1 = Math.toRadians(lat1);
		long1 = Math.toRadians(long1);
		lat2 = Math.toRadians(lat2);
		long2 = Math.toRadians(long2);
		
		double fixLat = lat2-lat1;
		double fixLong = long2-long1;
		
		double x = (Math.sin(fixLat/2) * Math.sin(fixLat/2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(fixLong/2) * Math.sin(fixLong/2));
		double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1-x));
		
		return Radius * y;
    }
    
    
}
