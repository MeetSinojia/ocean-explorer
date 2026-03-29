# Ocean Explorer Kata

A remotely controlled submersible probe API built in Java 21.

## Quick Start

```bash
mvn clean test          # run all tests
mvn compile exec:java -Dexec.mainClass=com.natwest.oceanexplorer.OceanExplorerApp   # run demo
```

## Commands

| Character | Action |
|-----------|--------|
| `F`       | Move forward one step |
| `B`       | Move backward one step |
| `L`       | Turn left (counter-clockwise 90°) |
| `R`       | Turn right (clockwise 90°) |

Commands are case-insensitive and whitespace is ignored (`"F F L"` == `"FFL"`).

## Design Decisions

### Command Pattern
Each command maps to a dedicated handler (`MoveForwardHandler`, `TurnLeftHandler`, etc.).
Adding new commands requires zero changes to existing code — just add a handler and register it in `CommandHandlerFactory`.

### Immutable Position (Java Record)
`Position` is a Java 21 record — immutable, with value equality built in.
Every move produces a *new* Position rather than mutating state, making history tracking straightforward.

### Direction Enum owns rotation logic
`NORTH.turnRight()` returns `EAST`. All circular rotation maths lives in one place.

### Separation of concerns
- `Navigator` — calculates the *candidate* next position (pure maths, no validation)
- `BoundaryValidator` — decides if that position is *legal* (boundary + obstacle checks)
- `ProbeController` — orchestrates; dispatches commands, handles exceptions, returns `MissionResult`
- `MissionReporter` — formats output; can evolve independently of domain logic

### Graceful halt on obstacle or boundary
The probe stops at its last safe position and the `MissionResult` records `haltedEarly=true` with a descriptive reason. Commands after the halt point are not executed.

## Project Structure

```
src/main/java/com/natwest/oceanexplorer/
├── OceanExplorerApp.java       # Entry point / demo
├── model/                      # Value objects and enums
│   ├── Position.java
│   ├── Direction.java
│   ├── Command.java
│   └── Grid.java
├── probe/                      # Probe state and orchestration
│   ├── Probe.java
│   ├── ProbeController.java
│   ├── MovementHistory.java
│   └── MissionResult.java
├── command/                    # Command pattern handlers
│   ├── CommandHandler.java
│   ├── CommandParser.java
│   ├── CommandHandlerFactory.java
│   ├── MoveForwardHandler.java
│   ├── MoveBackwardHandler.java
│   ├── TurnLeftHandler.java
│   └── TurnRightHandler.java
├── navigation/
│   ├── Navigator.java
│   └── BoundaryValidator.java
├── report/
│   └── MissionReporter.java
└── exception/
    ├── ObstacleEncounteredException.java
    ├── OutOfBoundsException.java
    └── InvalidCommandException.java
```

## Tech Stack

| Concern | Choice |
|---------|--------|
| Language | Java 21 |
| Build | Maven |
| Testing | JUnit 5 + Mockito + AssertJ |
| Logging | SLF4J + Logback |
| Boilerplate reduction | Lombok |
