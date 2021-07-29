
public class LinkedList{
	//instance variables
	public int size;
	public Node head;
	
	//constructor
	public LinkedList(){
		head = new Node();
		size = 0;
	}
	
	//getter method for size
	public int size(){
		return size;
	}
	
	//method to determine the amount between 2 intersections
	public double findAmount(Intersection3 int2) {
		Edge temp2 = head.edge;
		while(temp2 != null){			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)){
				return temp2.road.distance;
			}
			temp2 = temp2.next;
		}		
		return -1;
	}
	
	//method to add an intersection
	public void add(Intersection3 intersect) {
		if(head.intersection == null) {
			head.intersection = intersect;
		}
		size++;
	}
	
	//method to determine if the inserections are connected
	public boolean checkConnected(Intersection3 int2) {
		Edge temp2 = head.edge;
		while(temp2 != null){			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)){
				return true;
			}
			temp2 = temp2.next;
		}		
		return false;
	}
	
	//method to determine if TWO intersections are connected
	public boolean contains (Intersection3 i) {
		Node temp = head;		
		while(temp != null) {
			if(temp.intersection.equals(i)) {
				return true;
			}
			
			temp = temp.next;
		}
		return false;
	}
	
	//method to add a road into a list
	public void addRoad(Road road ) {
		Edge tempEdge = new Edge();
		tempEdge.road = road;
		tempEdge.next = head.edge;
		head.edge = tempEdge;
	}
	
}
