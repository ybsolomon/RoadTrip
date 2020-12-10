import java.util.*;

public class Graph {
    
    protected int count = 0;
    protected int edgeMatrix[][];
    private int dist[];
    private int parents[];
    private int numVertices;
    protected Hashtable<String, Integer> cities;
    protected Hashtable<String, String> attractions;
    protected List<String> cityMap;
    protected Stack<Integer> path;
    
    public Graph() {
        path = new Stack<Integer>();
        cities = new Hashtable<String, Integer>();
        attractions = new Hashtable<String, String>();
        cityMap = new ArrayList<String>();
        numVertices = 0;
    }

    public void addCity(String city) {
        try {
            if (!cities.containsKey(city)) {
            cityMap.add(city);
            cities.put(city, count++);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void setAttractions(Hashtable<String, String> attractions) {
        this.attractions = attractions;
    }

    public void setCities(Hashtable<String, Integer> cities) {
        this.cities = cities;
    }

    public void setEdgeMatrix(int[][] edges) {
        edgeMatrix = edges;
        numVertices = edges.length;
    }


    public Hashtable<String, Integer> getCities() {
        return cities;
    }
    
    /**
     * runs dijkstra's algorithm
     * @param graph - int[][] of edge weights
     * @param src - int starting vertex
     */
    void dijkstra(int graph[][], int src) 
    { 
        dist = new int[numVertices]; 
        Boolean visited[] = new Boolean[numVertices]; 
        parents = new int[numVertices];
  
        
        for (int i = 0; i < numVertices; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            visited[i] = false; 
        } 
  
        dist[src] = 0; 
        parents[src] = -1;
  
        for (int count = 0; count < numVertices - 1; count++) { 
            int u = minDistance(dist, visited); 
            if (u == -1) { break;}
            visited[u] = true; 
  
            for (int v = 0; v < numVertices; v++) {
  
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v]; 
            		parents[v] = u;
            	}
            }
        }
    }
    
    /**
     * finds closest unvisited city
     * @param dist - int array of distances
     * @param visited - boolean array of visited cities
     * @return index of closest city
     */
    private int minDistance(int dist[], Boolean visited[]) 
    { 
        int min = Integer.MAX_VALUE, min_index = -1; 
  
        for (int v = 0; v < numVertices; v++) 
            if (visited[v] == false && dist[v] < min) { 
                min = dist[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    }
    
    /**
     * adds path to path stack
     * @param currentVertex
     */
    private void addPath(int currentVertex)
    {
    	if (currentVertex != -1) {
    		path.push(currentVertex);
    	}
        
        if (parents[currentVertex] != -1) {
            addPath(parents[currentVertex]);
        }
    }
    
    /**
     * find_nearest returns the closest city to the current location
     * @param attraction
     * @param destination
     * @return index to remove from outer attraction array
     */
    protected int find_nearest(List<String> attraction, String destination) { 
        if (attraction.size() != 0) { 
            int shortest_city = cities.get(attractions.get(attraction.get(0)));

            if (attraction.size() >= 2) {
	            for (int i = 1; i < attraction.size(); i++) {
	                if (dist[shortest_city] > dist[cities.get(attractions.get(attraction.get(i)))]) { 
	                    shortest_city = cities.get(attractions.get(attraction.get(i)));  
	                }
	                
	            }
            }
            addPath(shortest_city);
            dijkstra(edgeMatrix, shortest_city); 
            return shortest_city;
        }
        else {
       	 	addPath(cities.get(destination));
       	 	return cities.get(destination);
        }
    }       
}