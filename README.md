# 🚑 Ambulance Dispatch Simulator

### A Java-Based DSA Project Using Weighted Graphs, Dijkstra's Algorithm and Priority Queues

![Java](https://img.shields.io/badge/Language-Java-orange)
![DSA](https://img.shields.io/badge/Subject-Data%20Structures%20%26%20Algorithms-blue)
![Application](https://img.shields.io/badge/Type-Console%20Application-green)
![Academic Project](https://img.shields.io/badge/Project-Academic-success)

---

## 📌 Project Overview

The **Ambulance Dispatch Simulator** is a console-based Java application that simulates an emergency ambulance dispatch system.

The system automatically identifies and dispatches the nearest available ambulance to an emergency location using Dijkstra's Shortest Path Algorithm.

Each emergency is assigned a severity level:

- **1 – Critical**
- **2 – Moderate**
- **3 – Minor**

Higher-priority emergencies are processed first. When all ambulances are busy, incoming emergency requests are stored in a Priority Queue and dispatched automatically when an ambulance becomes available.

The project demonstrates the practical application of fundamental Data Structures and Algorithms concepts to solve a real-world problem.

---

## 🎯 Project Objectives

- Represent a city's road network using a weighted graph.
- Implement Dijkstra's Algorithm to calculate shortest routes.
- Use Priority Queues for emergency prioritisation and shortest-path calculations.
- Identify and dispatch the nearest available ambulance.
- Maintain ambulance availability and current locations.
- Handle multiple emergency requests based on severity.
- Automatically process waiting requests after trip completion.
- Demonstrate the practical application of Java Collections and DSA concepts.

---

## 🗺️ City Road Network

The simulator uses a predefined city map consisting of four locations connected through weighted roads.

### Locations

1. Hospital
2. Sector1
3. Sector2
4. Sector3

### Road Connections

| Road Connection | Distance |
|---|---:|
| Hospital ↔ Sector1 | 4 km |
| Sector1 ↔ Sector2 | 3 km |
| Sector2 ↔ Sector3 | 5 km |
| Hospital ↔ Sector3 | 10 km |

All road connections are bidirectional.

### Initial Ambulance Configuration

| Ambulance ID | Initial Location | Initial Status |
|---|---|---|
| AMB1 | Hospital | Available |
| AMB2 | Sector3 | Available |

---

## 🧠 DSA Concepts Used

| Data Structure / Algorithm | Application |
|---|---|
| Weighted Graph | Represents city locations and road distances |
| Adjacency List | Stores connected locations and road weights |
| Dijkstra's Algorithm | Finds the shortest route between locations |
| Priority Queue (Min-Heap) | Selects the minimum-distance node in Dijkstra |
| Priority Queue | Manages emergency requests by severity |
| HashMap | Stores shortest distances and previous locations |
| LinkedHashMap | Maintains graph connections and ambulance records |
| ArrayList | Stores connected roads and reconstructed routes |
| Comparator | Defines custom priority ordering |
| Greedy Approach | Selects the next minimum-distance location and nearest ambulance |

### Two Priority Queues

The project uses two separate Priority Queues for different purposes.

**1. Dijkstra Priority Queue**

- Maintains locations according to their tentative distances.
- Processes the location with the smallest distance first.
- Helps calculate the shortest route efficiently.

**2. Emergency Priority Queue**

- Prioritises emergency requests based on severity.
- Lower severity numbers indicate higher priority.
- Uses arrival order as a tie-breaker when severity levels are equal.

---

## ⚙️ Key Features

### 1. Emergency Registration

- Register an emergency by entering its location and severity.
- Validate the entered location and severity.
- Generate a unique emergency ID.
- Automatically add the request to the priority queue.

### 2. Nearest Ambulance Selection

- Identify all available ambulances.
- Calculate the shortest route from each available ambulance.
- Compare the calculated distances.
- Select the ambulance with the minimum reachable distance.

### 3. Emergency Prioritisation

- Critical emergencies are processed first.
- Moderate emergencies are processed next.
- Minor emergencies are processed last.
- Equal-severity requests follow First Come, First Served (FCFS) ordering.

### 4. Ambulance Management

- Maintain ambulance availability.
- Track current ambulance locations.
- Mark dispatched ambulances as BUSY.
- Update ambulance locations after trip completion.
- Mark completed ambulances as AVAILABLE.

### 5. Automatic Re-dispatch

- Store emergency requests when all ambulances are busy.
- Check the waiting priority queue after trip completion.
- Automatically dispatch the highest-priority pending request when an ambulance becomes available.

### 6. Status and Map Display

- Display current ambulance locations and availability.
- View assigned emergency details.
- Display selected routes and distances.
- View waiting emergency requests.
- Display the city road network.

---

## 🔄 Program Workflow

1. Initialize the city graph and ambulance records.
2. Receive an emergency request.
3. Validate the emergency location and severity.
4. Insert the emergency into the priority queue.
5. Check available ambulances.
6. Run Dijkstra's Algorithm from each available ambulance.
7. Compare shortest distances.
8. Dispatch the nearest available ambulance.
9. Mark the ambulance as BUSY.
10. Complete the trip and update its location.
11. Mark the ambulance as AVAILABLE.
12. Automatically process waiting emergency requests.

---

## 💻 Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Java Collections Framework | Implementation of data structures |
| PriorityQueue | Emergency scheduling and Dijkstra |
| HashMap / LinkedHashMap | Data storage and retrieval |
| ArrayList | Graph adjacency lists and route storage |
| Comparator | Custom priority ordering |
| Command Prompt / Terminal | Application execution |

**Note:** The project uses standard Java libraries and does not require external frameworks, a database or API integration.

---

## 📂 Project Structure

The entire application is implemented in a single Java file using nested classes.

```text
Ambulance-Dispatch-Simulator/
│
├── AmbulanceDispatchSimulator.java
│
└── README.md
```

### Major Classes

| Class | Responsibility |
|---|---|
| AmbulanceDispatchSimulator | Main application and dispatch management |
| Ambulance | Stores ambulance details and status |
| Emergency | Stores emergency information and priority |
| Road | Represents road connections and distances |
| CityGraph | Maintains the weighted graph and Dijkstra algorithm |
| NodeDistance | Stores location and tentative distance |
| PathResult | Stores the shortest route and distance |
| AmbulanceChoice | Stores the selected ambulance and route |

---

## 🚀 How to Run the Project

### Prerequisites

- Java Development Kit (JDK 8 or later).
- Command Prompt / Terminal or any Java-supported IDE.
- Basic knowledge of Java.

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
```

### Step 2: Navigate to the Project Directory

```bash
cd Ambulance-Dispatch-Simulator
```

### Step 3: Compile the Java File

```bash
javac AmbulanceDispatchSimulator.java
```

### Step 4: Run the Application

```bash
java AmbulanceDispatchSimulator
```

---

## 🖥️ Console Menu

After execution, the application displays the following menu:

```text
======= AMBULANCE DISPATCH SIMULATOR =======

1. Add emergency / Dispatch request
2. Complete ambulance trip
3. Show ambulance status
4. Show waiting emergency requests
5. Display city road map
6. Run Test Case 1 - Normal input
7. Run Test Case 2 - Minimum input
8. Run Test Case 3 - Invalid location
9. Run Test Case 4 - Waiting priority scenario
0. Exit

============================================
```

Select the required option to perform the corresponding operation.

---

## 🧪 University Test Cases

The application includes all four test cases specified in the university project description.

| Test Case | Description | Expected Result |
|---|---|---|
| Test Case 1 | Emergency at Sector2, Severity 2 | AMB2 dispatched at 5 km |
| Test Case 2 | Emergency at Hospital, Severity 1 | AMB1 selected at 0 km |
| Test Case 3 | Emergency at Sector9, Severity 1 | Invalid location message |
| Test Case 4 | Both ambulances busy with waiting requests | Severity 1 request served first after AMB1 completes its trip |

### Example: Test Case 1

**Input:**

```text
Emergency Location: Sector2
Severity: 2
```

**Distance Calculation:**

```text
AMB1: Hospital -> Sector1 -> Sector2
Distance: 7 km

AMB2: Sector3 -> Sector2
Distance: 5 km
```

**Output:**

```text
Nearest ambulance: AMB2

Path: Sector3 -> Sector2
Distance: 5 km

AMB2 status: Busy
```

### Example: Test Case 2

**Input:**

```text
Emergency Location: Hospital
Severity: 1
```

**Output:**

```text
Nearest ambulance: AMB1
Distance: 0 km
AMB1 status: Busy
```

### Example: Test Case 3

**Input:**

```text
Emergency Location: Sector9
Severity: 1
```

**Output:**

```text
Location Sector9 not found.
```

### Example: Test Case 4

When both ambulances are busy:

```text
Emergency: Sector1
Severity: 2

No ambulance available. Request queued.

Emergency: Sector2
Severity: 1

No ambulance available. Request queued.
```

After AMB1 completes its trip:

```text
AMB1 completed its trip.
Location updated: Hospital -> Sector3
AMB1 is now Available.

Checking waiting requests...

Nearest ambulance: AMB1
Path: Sector3 -> Sector2
Distance: 5 km

AMB1 status: Busy
```

The system serves the Severity 1 emergency before the older Severity 2 request.

---

## ⏱️ Time Complexity Analysis

Let:

- V = Number of vertices (locations).
- E = Number of edges (roads).
- A = Number of available ambulances.
- Q = Number of waiting emergency requests.

| Operation | Time Complexity |
|---|---|
| Graph insertion | O(1) average per location or road insertion |
| Dijkstra's Algorithm | O((V + E) log V) |
| Priority Queue insertion | O(log Q) |
| Priority Queue removal | O(log Q) |
| Priority Queue peek | O(1) |
| HashMap lookup | O(1) average |
| Nearest ambulance selection | O(A × (V + E) log V) |
| Display ambulance status | O(A) |
| Display waiting requests in priority order | O(Q log Q) |

The graph requires O(V + E) space for its adjacency-list representation.

Dijkstra's algorithm uses additional O(V + E) space for distance tracking, previous-location tracking and the priority queue.

Emergency storage requires O(Q) space, while ambulance records require O(A) space.

---

## 🔮 Future Scope

The project can be extended with the following features:

- Graphical User Interface (GUI).
- Larger city road networks.
- Support for multiple ambulances.
- Dynamic traffic simulation.
- GPS-based ambulance tracking.
- Multiple hospital locations and hospital selection.
- Database integration for storing emergency records.
- Advanced emergency prioritisation.

These features are possible future enhancements and are not included in the current implementation.

---

## 📚 Project Documentation

The following documents provide additional information about the project.

| Document | Link |
|---|---|
| Project Interim Report | [View Project Report](https://drive.google.com/file/d/1YBsQnL5zPs26L2w7EiAllxS0B3aKOtma/view?usp=drive_link) |
| Presentation PPT | [View Presentation](https://drive.google.com/file/d/16x_vgRYM-Ygmx3Dhn2kxYcF8I1FeexL9/view?usp=drive_link) |

---

## 🎓 Academic Information

**Project Title:** Ambulance Dispatch Simulator

**Problem Statement Number:** 83

**Subject:** Data Structures and Algorithms (DSA)

**Programming Language:** Java

**Institution:** Rungta International Skills University, Bhilai, Chhattisgarh

**Academic Session:** 2026–27

**Project Type:** Academic DSA Project

---

## 👨‍💻 Author

Developed as part of the Data Structures and Algorithms academic curriculum.

---

## ⭐ Acknowledgement

This project demonstrates the practical application of fundamental Data Structures and Algorithms concepts in solving an emergency ambulance dispatch problem.

Special thanks to the faculty members of the School of Computer Science and Engineering, Rungta International Skills University, for their guidance and support.

---

**Ambulance Dispatch Simulator | Java | Data Structures & Algorithms**
