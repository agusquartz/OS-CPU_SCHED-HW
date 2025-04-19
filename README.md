# CPU Scheduling Simulator [OS-CPU_SCHED-HW]

## Overview

This repository contains solutions and experiments related to CPU scheduling algorithms as part of an Operating Systems homework assignment. It includes implementations of common scheduling techniques such as FCFS, SJF, and RR, focusing on understanding how different strategies affect process execution order and overall performance.

This project provides a Java-based CPU scheduling simulator with a graphical user interface (GUI). It supports the following scheduling algorithms:
- First Come First Served (FCFS)
- Shortest Job First Expulsive (SJFE)
- Shortest Job First (SJF) [Non-Expulsive]
- Round Robin (RR)
- Priority
- Highest Response Ratio Next (HRRN)

## Project Structure
```
src/
├── Main.java           # Entry point and orchestration of simulation
├── View.java           # Swing GUI for selecting algorithms and running simulation
├── Algorithm.java      # Interface for scheduling algorithms
├── RR.java             # Round Robin implementation
├── FCFS.java           # FCFS implementation
├── SJF.java            # SJF (non-expulsive) implementation
├── SJFE.java           # SJF (expulsive) implementation
├── Priority.java       # Priority scheduling implementation
├── HRRN.java           # HRRN implementation
├── Dispatcher.java     # Simulates the CPU time loop and builds the Gantt chart
├── BCP.java            # Process Control Block class
├── Gantt.java          # Gantt chart representation
├── GanttEntry.java     # Single entry in a Gantt chart
├── FileManager.java    # Singleton for reading input and writing CSV output
└── Parser.java         # Singleton for parsing input CSV into BCP list
```

## Requirements
- Java SE Development Kit (JDK) 21 (used in the project, recommended) or later
- Maven or Make (optional, for build automation)

## Manual Compilation
1. Ensure JDK 21+ is installed and javac is in your PATH.
2. From the project root, compile all .java files:
```
javac src/*.java
```

3. Run the application:
```
cd src/ && java Main
```

## Usage
1. Prepare an input CSV file named test.csv in the working directory. The expected columns are:
    - `id` (integer)
    - `arrival` (integer timestamp)
    - `burst` (integer CPU burst time)
    - `priority` (integer priority level)
2. Launch the application. A GUI window will appear.
3. In the Normal section, check one or more algorithms you wish to run in series.
4. In the Round Robin option, enter the time-slice quantum.
5. In the Multi-Level Queue section, select an algorithm for each of the three levels.
    - If you choose Round Robin for a level, a quantum field will appear for that level.
6. Click "Ejecutar" (Execute).
7. The console will display which algorithms were selected and their parameters.
8. For each scheduling run, a new CSV file (appended) will be generated via FileManager. The output file has the name "gantt.csv"

## Input and Output
- Input: test.csv containing one row per process.
- Output: For each algorithm run, the simulator appends a CSV representation of its Gantt chart to an output file. Each line in the output includes time slices and process IDs in order.

## How It Works
1. Main reads test.csv file, then makes it LinkedList<BCP> via Parser.
2. View presents the GUI to the user for algorithm selection.
3. Upon execution, Main.programLogic(...) is called with the selected algorithms.
4. executeSerial(...) runs normal-mode algorithms one after another:
    - Each algorithm is instantiated (e.g. new FCFS()), then passed along with the process list to startJob(...).
    - Dispatcher.run(...) simulates the CPU clock, repeatedly calling Algorithm.apply(...), building a Gantt chart.
    - After completion, the Gantt chart is exported via FileManager.appendToFile(...), then the process list is reset to its initial state.

5. executeMultiLevel(...) does the same for each of three priority queues:
    - Processes are filtered by priority level.
    - Each queue runs its assigned algorithm, with its own quantum if Round Robin.

## Extending the Simulator
To add a new scheduling algorithm:
1. Create a new class implementing the Algorithm interface (defining BCP apply(LinkedList<BCP>, int currentTime)).
2. Update View.EnumAlgo (if you want GUI selection) and add a new checkbox or combo option.
3. Add corresponding case in Main.executeSerial(...) or executeMultiLevel(...) to instantiate and run your new algorithm.

## License
This project is provided under the GNU General Public License v3.0. See LICENSE file for details.
