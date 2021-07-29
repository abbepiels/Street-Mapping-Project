This project is a JAVA program that takes a given data set of points in an area and outputs a map based on the read data. Each data point represents the coordinates of location in that specific area. This program creates a graph using the data it reads, calculates the shortest path between two designated locations, and displays the map using Java Graphics. Dijkstra's algorithm and Kruskal's algorithm are used to compute the shorted path between two locations and minimum weight spanning tree of the graph built. 

Dijkstra's algorithm is built using a HashMap that maps each IntersectionID to an Intersection. In order to find the shortest unknown path, the HashMap which allows the program to lookup each intersection very quickly, is used to insert each Intersection in a PriorityQueue when they are put into the graph. 

Kruskal's algorithm is built using a HashMap that maps each intersectionID to a HashSet of all the IntersectionsIds that that intersection is connected to, and a PriorityQueue of Roads to find the shortest road between two points. This allows for a quick lookup of the shortest road. Once Kruksals' algorithm is used, the current Road will either be added to a different ArrayList of Roads, or will not be. It is then added to the graph

Dijkstra's algorithm and Kruskal's algorithm are both written in the MapClass class.

Overall:
The graph representation uses a HashMap of Strings mapped to LinkedLists. Each string is an IntersectionID. Each Linkedist has a node Head that is able to store the Intersection object and an Edge pointer that stores a road of which the intersection is included in. Because all edges point to the next edge, a LinkedList is formed. After the LinkedList is created, every road is added to an ArrayList. Java Graphic is employed and a line is painted by using the end points of the road.

To run this program:
Download all files
Open through terminal 
Compile the MainTest class. When running this class, add the arguments fileName and instruction (:show, :directions, :meridian map);
Example: java MainTest /Users/abbepiels/Documents/StreetMap/monroe.txt :show 


