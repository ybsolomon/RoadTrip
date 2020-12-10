import java.util.*;
import java.io.*;

public class RoadTrip {

	private Graph g;
	
	/**
	 * Construct RoadTrip
	 */
	public RoadTrip() {
		g = new Graph();
	}

	/**
	 * set graph to user input
	 * @param graph - Graph variable
	 */
	public void setDijkstra(Graph graph) {
		g = graph;
	}

	public static void main(String[] args) {
      	Scanner scan = new Scanner(System.in);
        RoadTrip trip = new RoadTrip();
        File city = new File("roads.csv");
        File attraction = new File("attractions.csv");

        trip.loadCities(city);
        trip.loadAttractions(attraction);
        trip.loadRoads(city);
        
      	ArrayList<String> attractions = new ArrayList<String>();

        System.out.println("Enter the city you want to start with (ie San Francisco CA): ");
        String start = scan.nextLine();

        System.out.println("Enter the city you want to end with: ");
      	String end = scan.nextLine();

      	System.out.println("Enter the amount of attractions you want to visit: ");
      	int num = scan.nextInt();
      	scan.nextLine();

      	
        for (int i = 0; i < num; i++) {
           System.out.println("Enter attraction " + (i+1));
           attractions.add(scan.nextLine());
        }
        scan.close();
      
        List<String> finalPath = trip.route(start, end, attractions);
        
        for (int i = 1; i < finalPath.size(); i++) {
        	if(finalPath.get(i-1).equals(finalPath.get(i)) ) {
        		finalPath.remove(i--);
        	}
        }

        System.out.println("\nRoute:");
        for (int i = 0; i < finalPath.size(); i++) {
        	System.out.println(finalPath.get(i));
        }
        
	}
  
  	/**
  	 * builds the most efficient path from the start_city to end_city while passing through
  	 * all members of attractions array
  	 * @param starting_city - String user inputed start city
  	 * @param ending_city - String user inputed final destination
  	 * @param attractions - List<String> of user inputed attractions
  	 * @return List<String> of cities visited
  	 */
	public List<String> route(String starting_city, String ending_city, List<String> attractions) {
  		List<String> route = new ArrayList<String>();
  		
		String i_am_here = starting_city;
      	g.dijkstra(g.edgeMatrix, g.cities.get(i_am_here));
      	
      	while(attractions.size() > 0){
      		g.path = new Stack<Integer>();
      		int index = g.find_nearest(attractions, ending_city);
          	i_am_here = g.cityMap.get(index);
          	for (int i = 0; i < attractions.size(); i++) {
          		if (g.attractions.get(attractions.get(i)).equals(i_am_here)) {
          			attractions.remove(i);
          		}
          	}
          	
          	while (!g.path.empty()) {
          		route.add(g.cityMap.get(g.path.pop()));
          	}
        }
      	
      	g.path = new Stack<Integer>();
      	g.find_nearest(attractions, ending_city);
      	while (!g.path.empty()) {
      		route.add(g.cityMap.get(g.path.pop()));
      	}

        return route;
    }

	/**
	 * loads cities from file into Hashtable<String, Integer> to access later
	 * @param file - File of roads
	 */
	public void loadCities(File file) { 
        try { 
            Scanner scan = new Scanner(file);  
            scan.useDelimiter(",|\\n");
            
            while (scan.hasNextLine()) { 
                String cityA = scan.next();
                String cityB = scan.next();
                scan.next();
                scan.next();

                g.addCity(cityA);
                g.addCity(cityB);
            } 
            scan.close();
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
    }
    
    /**
     * loads attractions into Hashtable<String, String> to access later
     * @param file - File of attractions
     */
	public void loadAttractions(File file){
        try { 
        	Hashtable<String, String> attractions = new Hashtable<String, String>();
            Scanner scan = new Scanner(file); 
      
            scan.useDelimiter(",|\\n");
            
            while (scan.hasNextLine()) { 
                String attraction = scan.next();
                String cityB = scan.next();
                
                attractions.put(attraction, cityB); 
            }

            g.setAttractions(attractions); 
            scan.close();
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
       
    }

    /**
     * loads edges into edge matrix
     * @param file - File of roads
     */
	public void loadRoads(File file) {
    	int[][] edges = new int[g.cityMap.size()][g.cityMap.size()];
    	Hashtable<String, Integer> cities = g.getCities();

    	try {
    		Scanner scan = new Scanner(file); 
            scan.useDelimiter(",|\\n");

            while (scan.hasNextLine()) { 
                String cityA = scan.next();
                String cityB = scan.next();
                int miles = scan.nextInt();
                
                scan.next();
				
                int indexA = (int) cities.get(cityA);
                int indexB = (int) cities.get(cityB);
                
				edges[indexA][indexB] = miles; 
				edges[indexB][indexA] = miles;
            }
            scan.close();
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 

        g.setEdgeMatrix(edges);
    }
}