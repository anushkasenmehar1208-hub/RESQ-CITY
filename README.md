# 🚨 RESQ CITY

### Smart City Emergency Response Simulator

**RESQ CITY** is an interactive emergency response simulator built with **Java** and **JavaFX**. It models a small city road network, responds to different types of emergencies, and finds the fastest available route for a suitable emergency vehicle.

Block a road, trigger an emergency, and watch the dispatch system adapt. The project combines route optimization, object-oriented design, and animated map visualization in one application.

## ✨ Features

- **🚑 Multi-service emergency response:** Dispatch ambulances, fire trucks, and police cars for medical emergencies, accidents, fires, and crimes.
- **🧭 Intelligent vehicle dispatch:** Selects a compatible, available vehicle based on its route to the emergency.
- **⚡ Fastest-route calculation:** Uses **Dijkstra's algorithm** to find the route with the lowest estimated travel time.
- **🚧 Interactive road closures:** Click roads on the map to block or reopen them. Blocked roads are excluded from route planning.
- **🚦 Travel-time-aware routing:** Road distance, speed limits, and traffic multipliers affect estimated travel time.
- **🗺️ Interactive JavaFX city map:** Visualizes locations, roads, emergency vehicles, and their movement along selected routes.
- **🔄 Emergency lifecycle:** Tracks emergency resolution and returns responding vehicles to available status.
- **↩️ Reset controls:** Restore vehicles to their starting locations for another simulation.
- **💻 Console simulation:** Core routing and vehicle movement can also be demonstrated without the graphical interface.

## 🖼️ How It Works

1. Select an emergency type: `MEDICAL`, `ACCIDENT`, `FIRE`, or `CRIME`.
2. Choose the location where the emergency occurs.
3. The dispatch system checks which available vehicles can handle it.
4. Dijkstra's algorithm calculates the fastest reachable route.
5. The selected vehicle travels along the route on the JavaFX map.
6. When the response finishes, the emergency is marked as resolved.

Try closing a road before dispatching a vehicle to see how route selection changes.

## 🛠️ Tech Stack

| Technology | Purpose |
| --- | --- |
| Java | Simulation logic and object-oriented architecture |
| JavaFX | Interactive map, controls, and vehicle animations |
| Dijkstra's algorithm | Fastest-route pathfinding |
| Maven | Dependency management and application execution |

## 🏗️ Architecture

The simulation logic is organized into focused Java classes rather than being tied to the interface.

| Component | Responsibility |
| --- | --- |
| `Location`, `Road`, `City` | Represent the city and its connected road network |
| `Emergency`, `EmergencyType` | Describe emergency incidents |
| `EmergencyVehicle` | Define shared vehicle data and behavior |
| `Ambulance`, `FireTruck`, `PoliceCar` | Implement service-specific emergency handling |
| `PathFinder` | Calculate fastest reachable routes |
| `DispatchManager` | Choose a suitable available response vehicle |
| `SimulationEngine` | Run console-based vehicle movement |
| `CityMapView` | Render the map, road states, and animations |
| `SimulatorApp` | Provide the interactive JavaFX application |
| `Main` | Demonstrate core functionality in the console |

### Object-Oriented Design

RESQ CITY uses **encapsulation**, **inheritance**, **abstraction**, and **polymorphism**. For example, `Ambulance`, `FireTruck`, and `PoliceCar` inherit from the abstract `EmergencyVehicle` class, while each vehicle type defines which emergencies it can handle. The dispatch system can work with these different vehicle types through their shared parent type.

## 🚀 Getting Started

### Requirements

- A JDK compatible with the project's Maven configuration (**Java 27**)
- **Maven**

### Run the JavaFX simulator

```bash
git clone https://github.com/anushkasenmehar1208-hub/RESQ-CITY.git
cd RESQ-CITY
mvn clean javafx:run
```

### Compile the project

```bash
mvn clean compile
```

> **Note:** The project uses a Maven configuration with JavaFX dependencies and a `src/` source directory. Run the commands from the repository root.

## 📁 Project Structure

```text
RESQ-CITY/
├── src/
│   ├── Ambulance.java
│   ├── City.java
│   ├── CityMapView.java
│   ├── DispatchManager.java
│   ├── Emergency.java
│   ├── EmergencyType.java
│   ├── EmergencyVehicle.java
│   ├── FireTruck.java
│   ├── Location.java
│   ├── Main.java
│   ├── PathFinder.java
│   ├── PoliceCar.java
│   ├── Road.java
│   ├── SimulationEngine.java
│   └── SimulatorApp.java
├── pom.xml
└── .gitignore
```

## 🎯 Project Highlights

- Combines **graph algorithms** with an interactive city simulation.
- Supports **multiple emergency services** with different capabilities.
- Recalculates routes around **blocked roads**.
- Separates the **core Java logic** from the **JavaFX visualization**.
- Demonstrates extensible object-oriented design through a shared emergency vehicle abstraction.

## 👥 Team

Developed as **RESQ CITY, Group 44**:

- M. Muhammadh
- M.A.A. Amar
- A. Gananathan
- S. Vithuran
- P. Lenujan

---

**RESQ CITY** | Java • JavaFX • Dijkstra's Algorithm • Emergency Response Simulation
