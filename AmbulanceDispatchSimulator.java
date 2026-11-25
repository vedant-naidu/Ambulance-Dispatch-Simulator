import java.util.*;

/**
 * Ambulance Dispatch Simulator
 *
 * DSA concepts used:
 * 1. Weighted graph represented using an adjacency list.
 * 2. Dijkstra's algorithm using a min-priority queue.
 * 3. A separate priority queue for emergencies (severity first, FIFO on ties).
 * 4. HashMap for ambulance lookup and graph adjacency lists.
 *
 * Compile: javac AmbulanceDispatchSimulator.java
 * Run:     java AmbulanceDispatchSimulator
 */
public class AmbulanceDispatchSimulator {

    private static final Scanner SCANNER = new Scanner(System.in);

    private CityGraph city;
    private final Map<String, Ambulance> ambulances = new LinkedHashMap<String, Ambulance>();
    private PriorityQueue<Emergency> waitingEmergencies;
    private long nextArrivalOrder;
    private int nextEmergencyId;

    // ----------------------------- ENUMS ----------------------------------

    enum AmbulanceStatus {
        AVAILABLE, BUSY
    }

    enum EmergencyStatus {
        WAITING, DISPATCHED, COMPLETED
    }

    // ----------------------------- MODELS ---------------------------------

    static class Ambulance {
        private final String id;
        private String currentLocation;
        private AmbulanceStatus status;
        private Emergency assignedEmergency;
        private PathResult assignedPath;

        Ambulance(String id, String currentLocation) {
            this.id = id;
            this.currentLocation = currentLocation;
            this.status = AmbulanceStatus.AVAILABLE;
        }
    }

    static class Emergency {
        private final String id;
        private final String location;
        private final int severity;
        private final long arrivalOrder;
        private EmergencyStatus status;

        Emergency(String id, String location, int severity, long arrivalOrder) {
            this.id = id;
            this.location = location;
            this.severity = severity;
            this.arrivalOrder = arrivalOrder;
            this.status = EmergencyStatus.WAITING;
        }
    }

    static class Road {
        private final String destination;
        private final int distance;

        Road(String destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    static class NodeDistance {
        private final String location;
        private final int distance;

        NodeDistance(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    static class PathResult {
        private final int distance;
        private final List<String> path;

        PathResult(int distance, List<String> path) {
            this.distance = distance;
            this.path = path;
        }

        boolean isReachable() {
            return distance >= 0 && !path.isEmpty();
        }
    }

    static class AmbulanceChoice {
        private final Ambulance ambulance;
        private final PathResult pathResult;

        AmbulanceChoice(Ambulance ambulance, PathResult pathResult) {
            this.ambulance = ambulance;
            this.pathResult = pathResult;
        }
    }

    // -------------------------- WEIGHTED GRAPH -----------------------------

    static class CityGraph {
        // HashMap: location -> list of directly connected roads.
        private final Map<String, List<Road>> adjacencyList = new LinkedHashMap<String, List<Road>>();

        void addLocation(String location) {
            if (!adjacencyList.containsKey(location)) {
                adjacencyList.put(location, new ArrayList<Road>());
            }
        }

        // The assignment's roads are treated as bidirectional.
        void addRoad(String source, String destination, int distance) {
            if (!adjacencyList.containsKey(source) || !adjacencyList.containsKey(destination)) {
                throw new IllegalArgumentException("Both locations must exist before adding a road.");
            }
            if (distance < 0) {
                throw new IllegalArgumentException("Road distance cannot be negative.");
            }
            adjacencyList.get(source).add(new Road(destination, distance));
            adjacencyList.get(destination).add(new Road(source, distance));
        }

        boolean containsLocation(String location) {
            return adjacencyList.containsKey(location);
        }

        Set<String> getLocations() {
            return adjacencyList.keySet();
        }

        /**
         * Dijkstra's shortest-path algorithm.
         * Returns both the minimum distance and the actual route.
         * A distance of -1 means that the destination cannot be reached.
         */
        PathResult dijkstra(String source, String destination) {
            if (!containsLocation(source) || !containsLocation(destination)) {
                return new PathResult(-1, Collections.<String>emptyList());
            }

            Map<String, Integer> distances = new HashMap<String, Integer>();
            Map<String, String> previous = new HashMap<String, String>();

            for (String location : adjacencyList.keySet()) {
                distances.put(location, Integer.MAX_VALUE);
            }
            distances.put(source, 0);

            PriorityQueue<NodeDistance> minQueue = new PriorityQueue<NodeDistance>(
                new Comparator<NodeDistance>() {
                    @Override
                    public int compare(NodeDistance a, NodeDistance b) {
                        return Integer.compare(a.distance, b.distance);
                    }
                }
            );
            minQueue.offer(new NodeDistance(source, 0));

            while (!minQueue.isEmpty()) {
                NodeDistance current = minQueue.poll();

                // Ignore an outdated queue entry if a shorter distance was found later.
                if (current.distance != distances.get(current.location)) {
                    continue;
                }

                if (current.location.equals(destination)) {
                    break;
                }

                for (Road road : adjacencyList.get(current.location)) {
                    int newDistance = current.distance + road.distance;
                    if (newDistance < distances.get(road.destination)) {
                        distances.put(road.destination, newDistance);
                        previous.put(road.destination, current.location);
                        minQueue.offer(new NodeDistance(road.destination, newDistance));
                    }
                }
            }

            int finalDistance = distances.get(destination);
            if (finalDistance == Integer.MAX_VALUE) {
                return new PathResult(-1, Collections.<String>emptyList());
            }

            List<String> route = new ArrayList<String>();
            String step = destination;
            while (step != null) {
                route.add(step);
                if (step.equals(source)) {
                    break;
                }
                step = previous.get(step);
            }

            Collections.reverse(route);
            if (route.isEmpty() || !route.get(0).equals(source)) {
                return new PathResult(-1, Collections.<String>emptyList());
            }
            return new PathResult(finalDistance, route);
        }

        void displayMap() {
            System.out.println("\n----------- CITY ROAD MAP -----------");
            for (Map.Entry<String, List<Road>> entry : adjacencyList.entrySet()) {
                System.out.print(entry.getKey() + " : ");
                List<Road> roads = entry.getValue();
                for (int i = 0; i < roads.size(); i++) {
                    Road road = roads.get(i);
                    System.out.print(road.destination + " (" + road.distance + " km)");
                    if (i < roads.size() - 1) {
                        System.out.print(" | ");
                    }
                }
                System.out.println();
            }
            System.out.println("-------------------------------------\n");
        }
    }

    // ------------------------- INITIALIZATION ------------------------------

    private void initializeSystem() {
        city = new CityGraph();
        city.addLocation("Hospital");
        city.addLocation("Sector1");
        city.addLocation("Sector2");
        city.addLocation("Sector3");

        city.addRoad("Hospital", "Sector1", 4);
        city.addRoad("Sector1", "Sector2", 3);
        city.addRoad("Sector2", "Sector3", 5);
        city.addRoad("Hospital", "Sector3", 10);

        ambulances.clear();
        ambulances.put("AMB1", new Ambulance("AMB1", "Hospital"));
        ambulances.put("AMB2", new Ambulance("AMB2", "Sector3"));

        // Smaller severity number means higher priority; arrival order breaks ties.
        waitingEmergencies = new PriorityQueue<Emergency>(
            new Comparator<Emergency>() {
                @Override
                public int compare(Emergency a, Emergency b) {
                    int severityComparison = Integer.compare(a.severity, b.severity);
                    if (severityComparison != 0) {
                        return severityComparison;
                    }
                    return Long.compare(a.arrivalOrder, b.arrivalOrder);
                }
            }
        );

        nextArrivalOrder = 1;
        nextEmergencyId = 1;
    }

    // -------------------------- EMERGENCY FLOW -----------------------------

    private void addEmergency(String location, int severity) {
        if (!city.containsLocation(location)) {
            System.out.println("Location " + location + " not found.");
            return;
        }
        if (severity < 1 || severity > 3) {
            System.out.println("Invalid severity. Enter 1 (Critical), 2 (Moderate), or 3 (Minor).");
            return;
        }

        location = normalizeLocation(location);
        String id = String.format("E%03d", nextEmergencyId++);
        Emergency emergency = new Emergency(id, location, severity, nextArrivalOrder++);
        waitingEmergencies.offer(emergency);
        System.out.println("Emergency " + id + " registered at " + location + ", Severity " + severity + ".");

        dispatchEmergency();
    }

    /**
     * Dispatches waiting requests in priority order while ambulances are available.
     * If no ambulance is available, the emergency remains in the priority queue.
     */
    private void dispatchEmergency() {
        while (!waitingEmergencies.isEmpty()) {
            Emergency nextEmergency = waitingEmergencies.peek();
            AmbulanceChoice choice = findNearestAmbulance(nextEmergency);

            if (choice == null) {
                System.out.println("No ambulance available. Request queued.");
                return;
            }

            waitingEmergencies.poll();
            assignAmbulance(nextEmergency, choice);

            // Stop quietly when all ambulances have just been assigned.
            // Any remaining emergencies stay in the priority queue.
            if (!hasAvailableAmbulance()) {
                return;
            }
        }
    }

    private boolean hasAvailableAmbulance() {
        for (Ambulance ambulance : ambulances.values()) {
            if (ambulance.status == AmbulanceStatus.AVAILABLE) {
                return true;
            }
        }
        return false;
    }

    /** Finds the nearest AVAILABLE ambulance by running Dijkstra from each one. */
    private AmbulanceChoice findNearestAmbulance(Emergency emergency) {
        AmbulanceChoice bestChoice = null;

        for (Ambulance ambulance : ambulances.values()) {
            if (ambulance.status != AmbulanceStatus.AVAILABLE) {
                continue;
            }

            PathResult result = city.dijkstra(ambulance.currentLocation, emergency.location);
            if (!result.isReachable()) {
                continue;
            }

            if (bestChoice == null
                || result.distance < bestChoice.pathResult.distance
                || (result.distance == bestChoice.pathResult.distance
                    && ambulance.id.compareTo(bestChoice.ambulance.id) < 0)) {
                bestChoice = new AmbulanceChoice(ambulance, result);
            }
        }
        return bestChoice;
    }

    private void assignAmbulance(Emergency emergency, AmbulanceChoice choice) {
        Ambulance ambulance = choice.ambulance;
        ambulance.status = AmbulanceStatus.BUSY;
        ambulance.assignedEmergency = emergency;
        ambulance.assignedPath = choice.pathResult;
        emergency.status = EmergencyStatus.DISPATCHED;

        System.out.println("Nearest ambulance: " + ambulance.id);
        if (choice.pathResult.distance == 0) {
            System.out.println("Distance: 0 km");
        } else {
            System.out.println("Path: " + formatPath(choice.pathResult.path)
                + " | Distance: " + choice.pathResult.distance + " km");
        }
        System.out.println(ambulance.id + " status: Busy");
    }

    // -------------------------- TRIP COMPLETION ----------------------------

    private void completeTrip(String ambulanceId) {
        Ambulance ambulance = ambulances.get(ambulanceId);
        if (ambulance == null) {
            System.out.println("Ambulance " + ambulanceId + " not found.");
            return;
        }
        if (ambulance.status != AmbulanceStatus.BUSY || ambulance.assignedEmergency == null) {
            System.out.println(ambulanceId + " is not currently on a trip.");
            return;
        }

        Emergency completedEmergency = ambulance.assignedEmergency;
        String oldLocation = ambulance.currentLocation;

        // Assignment rule: after a trip, the ambulance is at the emergency location.
        ambulance.currentLocation = completedEmergency.location;
        ambulance.status = AmbulanceStatus.AVAILABLE;
        ambulance.assignedEmergency = null;
        ambulance.assignedPath = null;
        completedEmergency.status = EmergencyStatus.COMPLETED;

        System.out.println(ambulanceId + " completed its trip.");
        System.out.println("Location updated: " + oldLocation + " -> " + ambulance.currentLocation);
        System.out.println(ambulanceId + " is now Available.");

        // Immediately try to serve the highest-priority waiting emergency.
        if (!waitingEmergencies.isEmpty()) {
            System.out.println("Checking waiting requests...");
            dispatchEmergency();
        }
    }

    // ------------------------------ STATUS ---------------------------------

    private void showStatus() {
        System.out.println("\n------------- AMBULANCE STATUS -------------");
        for (Ambulance ambulance : ambulances.values()) {
            System.out.println(ambulance.id + " | Location: " + ambulance.currentLocation
                + " | Status: " + (ambulance.status == AmbulanceStatus.AVAILABLE ? "Available" : "Busy"));
            if (ambulance.status == AmbulanceStatus.BUSY && ambulance.assignedEmergency != null) {
                System.out.println("   Assigned emergency: " + ambulance.assignedEmergency.id
                    + " at " + ambulance.assignedEmergency.location);
                if (ambulance.assignedPath != null) {
                    System.out.println("   Route: " + formatPath(ambulance.assignedPath.path)
                        + " | Distance: " + ambulance.assignedPath.distance + " km");
                }
            }
        }
        System.out.println("---------------------------------------------\n");
    }

    private void showWaitingRequests() {
        System.out.println("\n---------- WAITING EMERGENCY REQUESTS ----------");
        if (waitingEmergencies.isEmpty()) {
            System.out.println("No waiting requests.");
        } else {
            // PriorityQueue iteration is not sorted, so use a copy and poll it.
            PriorityQueue<Emergency> ordered = new PriorityQueue<Emergency>(waitingEmergencies);
            while (!ordered.isEmpty()) {
                Emergency emergency = ordered.poll();
                System.out.println(emergency.id + " | Location: " + emergency.location
                    + " | Severity: " + emergency.severity
                    + " | Arrival order: " + emergency.arrivalOrder);
            }
        }
        System.out.println("------------------------------------------------\n");
    }

    private String normalizeLocation(String input) {
        String candidate = input.trim();
        for (String location : city.getLocations()) {
            if (location.equalsIgnoreCase(candidate)) {
                return location;
            }
        }
        return candidate;
    }

    private static String formatPath(List<String> path) {
        if (path == null || path.isEmpty()) {
            return "No route";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            builder.append(path.get(i));
            if (i < path.size() - 1) {
                builder.append(" -> ");
            }
        }
        return builder.toString();
    }

    // ------------------------- INPUT VALIDATION ----------------------------

    private static int readInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private void printMenu() {
        System.out.println("\n======= AMBULANCE DISPATCH SIMULATOR =======");
        System.out.println("1. Add emergency / Dispatch request");
        System.out.println("2. Complete ambulance trip");
        System.out.println("3. Show ambulance status");
        System.out.println("4. Show waiting emergency requests");
        System.out.println("5. Display city road map");
        System.out.println("6. Run Test Case 1 - Normal input");
        System.out.println("7. Run Test Case 2 - Minimum input");
        System.out.println("8. Run Test Case 3 - Invalid location");
        System.out.println("9. Run Test Case 4 - Waiting priority scenario");
        System.out.println("0. Exit");
        System.out.println("============================================");
    }

    // ----------------------- UNIVERSITY TEST CASES -------------------------

    private void runTestCase1() {
        initializeSystem();
        System.out.println("\n========== TEST CASE 1: NORMAL INPUT ==========");
        addEmergency("Sector2", 2);
        showStatus();
    }

    private void runTestCase2() {
        initializeSystem();
        System.out.println("\n===== TEST CASE 2: BOUNDARY / MINIMUM INPUT =====");
        addEmergency("Hospital", 1);
        showStatus();
    }

    private void runTestCase3() {
        initializeSystem();
        System.out.println("\n========== TEST CASE 3: INVALID INPUT ==========");
        addEmergency("Sector9", 1);
        showStatus();
    }

    private void runTestCase4() {
        initializeSystem();
        System.out.println("\n===== TEST CASE 4: DIFFERENT VALID SCENARIO =====");

        // Re-create the state left by Test Case 1:
        // AMB2 was dispatched from Sector3 to Sector2; AMB1 is still at Hospital.
        Ambulance amb2 = ambulances.get("AMB2");
        Emergency previousTestCaseEmergency = new Emergency("E000", "Sector2", 2, 0);
        previousTestCaseEmergency.status = EmergencyStatus.DISPATCHED;
        amb2.status = AmbulanceStatus.BUSY;
        amb2.assignedEmergency = previousTestCaseEmergency;
        amb2.assignedPath = city.dijkstra("Sector3", "Sector2");

        System.out.println("Precondition: AMB2 is Busy (state carried over from Test Case 1).");
        showStatus();

        System.out.println("(a) Emergency: Sector3, Severity 3");
        addEmergency("Sector3", 3);

        System.out.println("(b) Emergency: Sector1, Severity 2");
        addEmergency("Sector1", 2);

        System.out.println("(c) Emergency: Sector2, Severity 1");
        addEmergency("Sector2", 1);

        System.out.println("(d) AMB1 completes its trip");
        completeTrip("AMB1");

        showStatus();
        showWaitingRequests();
    }

    // ------------------------------- MAIN ----------------------------------

    private void run() {
        initializeSystem();
        System.out.println("Welcome to the Ambulance Dispatch Simulator.");
        System.out.println("Locations: Hospital, Sector1, Sector2, Sector3");
        System.out.println("Severity: 1 = Critical, 2 = Moderate, 3 = Minor");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInteger("Enter your choice: ");

            switch (choice) {
                case 1: {
                    String location = readText("Enter emergency location: ");
                    int severity = readInteger("Enter severity (1-Critical, 2-Moderate, 3-Minor): ");
                    addEmergency(location, severity);
                    break;
                }
                case 2: {
                    String ambulanceId = readText("Enter ambulance ID (AMB1 / AMB2): ").toUpperCase(Locale.ROOT);
                    completeTrip(ambulanceId);
                    break;
                }
                case 3:
                    showStatus();
                    break;
                case 4:
                    showWaitingRequests();
                    break;
                case 5:
                    city.displayMap();
                    break;
                case 6:
                    runTestCase1();
                    break;
                case 7:
                    runTestCase2();
                    break;
                case 8:
                    runTestCase3();
                    break;
                case 9:
                    runTestCase4();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the simulator.");
                    break;
                default:
                    System.out.println("Invalid menu choice. Please select 0 to 9.");
            }
        }
    }

    public static void main(String[] args) {
        AmbulanceDispatchSimulator simulator = new AmbulanceDispatchSimulator();
        simulator.run();
    }
}
