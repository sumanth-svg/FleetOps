package main.service;

import main.model.Route;
import java.util.*;

public class RouteManager {

    private HashMap<String, Route> routes = new HashMap<>();
    private HashMap<String, List<String>> adjacency = new HashMap<>();
    private String[] defaultStops = {"X", "Y", "Z", "A", "B", "C"};

    // Edge class for Dijkstra
    private static class Edge {
        String to;
        int weight;
        Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Add Route with random stops
    public void addRoute(String routeID) {
        // Pick 3-5 random stops
        Random rand = new Random();
        int n = 3 + rand.nextInt(3);
        String[] stops = new String[n];
        for (int i = 0; i < n; i++) {
            stops[i] = defaultStops[rand.nextInt(defaultStops.length)];
        }

        Route r = new Route(routeID, stops);
        routes.put(routeID, r);

        // Add edges to adjacency list
        for (int i = 0; i < stops.length - 1; i++) {
            adjacency.putIfAbsent(stops[i], new ArrayList<>());
            adjacency.get(stops[i]).add(stops[i + 1]);

            // Also add reverse for undirected graph
            adjacency.putIfAbsent(stops[i + 1], new ArrayList<>());
            adjacency.get(stops[i + 1]).add(stops[i]);
        }

        System.out.println("Route added with stops: " + Arrays.toString(stops));
    }

    // Show all routes
    public void showRoutes() {
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
            return;
        }
        for (Route r : routes.values()) {
            r.displayRoute();
        }
    }

    // Shortest path using Dijkstra (all edges weight = 1)
    public void shortestPath(String start, String end) {
        if (!adjacency.containsKey(start) || !adjacency.containsKey(end)) {
            System.out.println("Start or end stop not found.");
            return;
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String stop : adjacency.keySet()) {
            dist.put(stop, Integer.MAX_VALUE);
        }
        dist.put(start, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            for (String neighbor : adjacency.getOrDefault(current, new ArrayList<>())) {
                int alt = dist.get(current) + 1; // weight = 1
                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // Reconstruct path
        if (!prev.containsKey(end) && !start.equals(end)) {
            System.out.println("No path found from " + start + " to " + end);
            return;
        }

        List<String> path = new ArrayList<>();
        String curr = end;
        path.add(curr);
        while (prev.containsKey(curr)) {
            curr = prev.get(curr);
            path.add(curr);
        }
        Collections.reverse(path);

        System.out.println("Shortest path from " + start + " to " + end + ": " + path);
    }
}