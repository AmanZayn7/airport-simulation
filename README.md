# ✈️ Concurrent Airport Operations Simulation (Java)

A multithreaded airport traffic control simulation built in Java.
This project models constrained runway access, gate allocation, refueling limitations, passenger handling, and emergency landing scenarios using core Java concurrency primitives.

## Overview

This system simulates a small airport environment with strict operational constraints:

Single runway,
Maximum of 3 aircraft on ground,
Limited gate capacity,
Single refueling truck,
Random aircraft arrival intervals,
Emergency landing scenario under congestion,
Performance statistics tracking.

The objective is to demonstrate coordination of multiple threads competing for shared resources in a realistic operational model.

# 🧠 Concurrency Concepts Implemented
## Synchronization (synchronized)

Used to ensure atomic access to shared state:

Recording aircraft wait times

Updating served aircraft counters

Tracking passengers boarded

Managing emergency landing flags

Prevents race conditions and ensures data consistency.

## Semaphores

Semaphores are used to control access to limited shared resources.

Resource	Type	Constraint Modeled

Runway	Binary Semaphore	Only one aircraft can land or take off at a time

Gates	Counting Semaphore	Limited number of aircraft may dock

Refueling Truck	Binary Semaphore	Only one aircraft may refuel at a time

## wait() and notifyAll()

Used to coordinate landing permissions when ground capacity is full.

Aircraft wait if maximum ground limit is reached

Threads resume when space becomes available

Enables coordinated landing and takeoff operations

## Thread Lifecycle Management

Each aircraft runs as an independent thread

Refueling and supply operations are handled in separate threads

Passenger operations occur concurrently across aircraft

## Aircraft Operational Flow

Each aircraft follows a structured lifecycle:

Request landing permission

Acquire runway access

Dock at gate

Disembark passengers

Refuel and restock supplies

Board new passengers

Taxi to runway

Take off

All operations respect concurrency constraints.

## Emergency Landing Scenario

The simulation includes a stressed operational case:

Multiple aircraft waiting to land

Gates fully occupied

A new aircraft requests emergency landing due to low fuel

This scenario tests prioritization logic and system coordination under pressure.

# Statistics & Output

At the end of the simulation, the system reports:

Total aircraft served

Total passengers boarded

Minimum waiting time

Maximum waiting time

Average waiting time

Sanity check confirming all gates are empty

# How to Run
Option 1 — IDE (Recommended)

Import as a Java project into IntelliJ or Eclipse

Run SimulationMain.java

Option 2 — Command Line
javac -d out src/asiaPacificAirport/*.java
java -cp out asiaPacificAirport.SimulationMain
