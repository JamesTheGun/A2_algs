// @edu:student-assignment

package uq.comp3506.a2;

// You may wish to import more/other structures too
import uq.comp3506.a2.structures.Edge;
import uq.comp3506.a2.structures.Vertex;
import uq.comp3506.a2.structures.Entry;
import uq.comp3506.a2.structures.TopologyType;
import uq.comp3506.a2.structures.Tunnel;
import uq.comp3506.a2.structures.UnorderedMap;
import uq.comp3506.a2.structures.Heap;

import java.util.ArrayList;
import java.util.List;

// This is part of COMP3506 Assignment 2. Students must implement their own solutions.

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * No bounds are provided. You should maximize efficiency where possible.
 * Below we use `S` and `U` to represent the generic data types that a Vertex
 * and an Edge can have, respectively, to avoid confusion between V and E in
 * typical graph nomenclature. That is, Vertex objects store data of type `S`
 * and Edge objects store data of type `U`.
 */
public class Problems {

    /**
     * Return a double representing the minimum radius of illumination required
     * to light the entire tunnel. Your answer will be accepted if
     * |your_ans - true_ans| is less than or equal to 0.000001
     * @param tunnelLength The length of the tunnel in question
     * @param lightIntervals The list of light intervals in [0, tunnelLength];
     * that is, all light interval values are >= 0 and <= tunnelLength
     * @return The minimum radius value required to illuminate the tunnel
     * or -1 if no light fittings are provided
     * Note: We promise that the input List will be an ArrayList.
     */
    public static double tunnelLighting(int tunnelLength, List<Integer> lightIntervals) {
        //put two dummy lights at the end... find biggest delta between consecutive... halve
        lightIntervals.add(tunnelLength);
        lightIntervals.add(0);
        lightIntervals.sort(Integer::compareTo);
        int largestDelta = 0;
        for (int i = 0; i < lightIntervals.size() - 1; i++) {
            int delta = lightIntervals.get(i + 1) - lightIntervals.get(i);
            if (delta > largestDelta) {
                largestDelta = delta;
            }
        }
        return largestDelta/2.0;
    }

    /**
     * adjacency result containing adjacency list and all nodes which exist
     */

    private static record adjacencyResult(
            UnorderedMap<Vertex, ArrayList<Vertex>> adjacencies,
            ArrayList<Vertex> nodeList
    ) {}

    /**
     * get adjacency rsult
     */
    private static <S, U> adjacencyResult getAdjacencyResult(List<Edge<S, U>> edgeList) {
        UnorderedMap<Vertex, ArrayList<Vertex>> adjacencies = new UnorderedMap<>();
        ArrayList<Vertex> nodeList = new ArrayList<>();

        for (Edge<S, U> edge : edgeList) {
            Vertex vertex1 = edge.getVertex1();
            Vertex vertex2 = edge.getVertex2();

            ArrayList<Vertex> list1 = adjacencies.get(vertex1);
            if (list1 == null) {
                list1 = new ArrayList<>();
                adjacencies.put(vertex1, list1);
                nodeList.add(vertex1);
            }
            list1.add(vertex2);

            ArrayList<Vertex> list2 = adjacencies.get(vertex2);
            if (list2 == null) {
                list2 = new ArrayList<>();
                adjacencies.put(vertex2, list2);
                nodeList.add(vertex2);
            }
            list2.add(vertex1);
        }
        return new adjacencyResult(adjacencies, nodeList);
    }

    /**
     * get unvisited
     */
    private static Vertex getUnVisited(ArrayList<Vertex> seenNodes,  ArrayList<Vertex> allNodes) {
        for (Vertex node : allNodes) {
            if (!seenNodes.contains(node)) {
                return node;
            }
        }
        return null;
    }

    /**
     * generate the thing we need to return
     */
    private static <S, U> TopologyType getReturn(boolean disconnected, boolean graph, boolean tree) {
        if (!graph && !tree) {
            return TopologyType.UNKNOWN;
        }

        if (!disconnected) {
            if (tree) {
                return TopologyType.CONNECTED_TREE;
            } else {
                return TopologyType.CONNECTED_GRAPH;
            }
        }

        if (disconnected) {
            if (tree && !graph) {
                return TopologyType.FOREST;
            } else if (graph && !tree) {
                return TopologyType.DISCONNECTED_GRAPH;
            } else if (graph && tree) {
                return TopologyType.HYBRID;
            }
        }

        return TopologyType.UNKNOWN; // Fallback safety
    }
    /**
     * Compute the TopologyType of the graph as represented by the given edgeList.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @return The corresponding TopologyType.
     * Note: We promise not to provide any self loops, double edges, or isolated
     * vertices.
     */

        public static <S, U> TopologyType topologyDetection(List<Edge<S, U>> edgeList) {
            if (edgeList.size() == 0) {
                return TopologyType.UNKNOWN;
            }
            // depth first search problem because we are finding cycles
            adjacencyResult adjRes = getAdjacencyResult(edgeList);
            boolean disconect = false;
            boolean graph = false;
            boolean tree = false;

            UnorderedMap<Vertex, ArrayList<Vertex>> adjacencies = adjRes.adjacencies;
            ArrayList<Vertex> nodeList = adjRes.nodeList;

            Vertex currentNode = null;
            Vertex previousNode = null;
            Vertex startingNode = nodeList.get(0);

            ArrayList<Vertex> seenNodes = new ArrayList<>();
            ArrayList<Vertex> explorationList = new ArrayList<>();

            while (currentNode != startingNode) {
                if (currentNode == null) {
                    currentNode = startingNode;
                }
                previousNode = currentNode;
                ArrayList<Vertex> adjacent = adjacencies.get(currentNode);
                if (adjacent == null) break;
                for (Vertex node : adjacent) {
                    if (!seenNodes.contains(node)) {
                        graph = true;
                        seenNodes.add(node);
                        currentNode = node;
                        break;
                    }
                }
                if (currentNode == previousNode) {
                    if (!explorationList.isEmpty()) {
                        currentNode = explorationList.remove(0);
                    }
                    if (currentNode != null && currentNode.equals(startingNode)) {
                        Vertex node = getUnVisited(seenNodes, nodeList);
                        if (!graph) {
                            tree = true;
                        }
                        if (node == null) {
                            return getReturn(disconect, graph, tree);
                        }
                        disconect = true;
                        currentNode = node;
                    }
                }
            }
            return getReturn(disconect, graph, tree);
        }

    /**
     *
     */
    private static record adjacentStation(
            Vertex station,
            int distance
    ) {}

    private static record adjacentStations(
            UnorderedMap<Vertex, ArrayList<adjacentStation>> adjacencies
    ) {
        void addAdjacent(int distance, Vertex origin, Vertex adjacent) {
            ArrayList<adjacentStation> list = adjacencies.get(origin);
            if (list == null) {
                list = new ArrayList<>();
                adjacencies.put(origin, list);
            }
            list.add(new adjacentStation(adjacent, distance));
        }
    }

    private static <S, U> adjacentStations getAdjacentStations(List<Edge<S, U>> edgeList) {
        UnorderedMap<Vertex, ArrayList<adjacentStation>> adjacencies = new UnorderedMap<>();

        for (Edge<S, U> edge : edgeList) {
            Vertex v1 = edge.getVertex1();
            Vertex v2 = edge.getVertex2();
            int weight = (Integer) edge.getData();

            ArrayList<adjacentStation> list1 = adjacencies.get(v1);
            if (list1 == null) {
                list1 = new ArrayList<>();
                adjacencies.put(v1, list1);
            }
            list1.add(new adjacentStation(v2, weight));

            ArrayList<adjacentStation> list2 = adjacencies.get(v2);
            if (list2 == null) {
                list2 = new ArrayList<>();
                adjacencies.put(v2, list2);
            }
            list2.add(new adjacentStation(v1, weight));
        }
        return new adjacentStations(adjacencies);
    }

    /**
     * Compute the list of reachable destinations and their minimum costs.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @param origin The origin vertex object.
     * @param threshold The total time the driver can drive before a break is required.
     * @return an ArrayList of Entry types, where the first element is the identifier
     *         of a reachable station (within the time threshold), and the second
     *         element is the minimum cost of reaching that given station. The
     *         order of the list is not important.
     * Note: We promise that S will be of Integer type.
     * Note: You should return the origin in your result with a cost of zero.
     */
    public static <S, U> List<Entry<Integer, Integer>> routeManagement(List<Edge<S, U>> edgeList,
                                                          Vertex<S> origin, int threshold) {
        ArrayList<Entry<Integer, Integer>> answers = new ArrayList<>();
        adjacentStations stations = getAdjacentStations(edgeList);
        ArrayList<Vertex> explorationList = new ArrayList<>();
        Vertex nodeToAdd = origin;
        ArrayList<Edge<S, U>> MSTedges = new ArrayList<>();

        while (nodeToAdd != null) {
            Edge edgeToAdd = null;
            explorationList.add(nodeToAdd);
            nodeToAdd = null;
            int bestDistance = Integer.MAX_VALUE;
            for (Vertex node : explorationList)
                for (adjacentStation adjacent : stations.adjacencies.get(node)) {
                    if (adjacent.distance < bestDistance) {
                        bestDistance =  adjacent.distance;
                        edgeToAdd = new Edge(node, adjacent.station, adjacent.distance);
                    }
                }
            MSTedges.add(edgeToAdd);
        }

        stations = getAdjacentStations(MSTedges);
        adjacencyResult adjacencyResults = getAdjacencyResult(MSTedges);
        UnorderedMap<Vertex, ArrayList<Vertex>> adjacencies = adjacencyResults.adjacencies;
        ArrayList<adjacentStation> stationStack = new ArrayList<>();
        currentStation =
        while (stat != null) {}
        }
    }

    /**
     * Compute the tunnel that if flooded will cause the maximal flooding of 
     * the network
     * @param tunnels A list of the tunnels to consider; see Tunnel.java
     * @return The identifier of the Tunnel that would cause maximal flooding.
     * Note that for Tunnel A to drain into some other tunnel B, the distance
     * from A to B must be strictly less than the radius of A plus an epsilon
     * allowance of 0.000001. 
     * Note also that all identifiers in tunnels are GUARANTEED to be in the
     * range [0, n-1] for n unique tunnels.
     */
    public static int totallyFlooded(List<Tunnel> tunnels) {
        return -1;
    }

    /**
     * Compute the number of sites that cannot be infiltrated from the given starting sites.
     * @param sites The list of unique site identifiers. A site identifier is GUARANTEED to be
     *              non-negative, starting from 0 and counting upwards to n-1.
     * @param rules The infiltration rule. The right-hand side of a rule is represented by a list
     *             of lists of site identifiers (as is done in the assignment specification). The
     *             left-hand side of a rule is given by the rule's index in the parameter `rules`
     *             (i.e. the rule whose left-hand side is 4 will be at index 4 in the parameter
     *              `rules` and can be accessed with `rules.get(4)`).
     * @param startingSites The list of site identifiers to begin your infiltration from.
     * @return The number of sites which cannot be infiltrated.
     */
    public static int susDomination(List<Integer> sites, List<List<List<Integer>>> rules,
                                     List<Integer> startingSites) {
        return -1;
    }
}
