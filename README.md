# Ocean Explorer Kata

A remotely controlled submersible probe API built in Java 21 and engineered for safe grid navigation, obstacles avoidance, and multi-probe operation.

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

## What is implemented

- Grid-based probe movement with coordinate-based position (`Position` record)
- Direction rotation model (`Direction` enum) with `turnLeft` / `turnRight`
- Command parsing (`CommandParser`) and command pattern execution (`CommandHandler` implementations)
- Forward and backward movement with wrapping-free boundary checks
- Obstacle detection and mission abort via `ObstacleEncounteredException`
- Out-of-bound detection and graceful halt via `OutOfBoundsException`
- Visit history tracking and later reporting via `MovementHistory` and `MissionReporter`
- Clear results model in `MissionResult` (successful vs halted, final state, path history)

## Interactive Manual Input Mode

`OceanExplorerApp` now includes an interactive prompt after demo scenarios:

- Run:

```bash
mvn compile exec:java -Dexec.mainClass=com.natwest.oceanexplorer.OceanExplorerApp
```

- After initial demo scenarios, you'll see:

```
Interactive mode: enter commands (F/B/L/R), type 'quit' to exit
Commands>
```

- Enter commands to drive the probe from starting position `(0,0)` facing `NORTH`.
- Use moves like:
  - `FFRFF`
  - `B` / `BB`
  - `LL` / `RR`
  - mixed `FFLFRB`
- Commands are case-insensitive; whitespace is accepted.
- Type `quit` or `exit` to stop.

- Each manual command run uses:
  - `Grid` 10x10 with no obstacles
  - `Probe` starts at `(0,0)` north
  - same command pipeline as existing tests.

## README / repo cleanup features we added

- `.gitignore` for:
  - `/target/`, `**/target/`
  - IDE metadata: `.idea/`, `.vscode/`, `*.iml`, etc.
  - OS artifacts: `.DS_Store`, `Thumbs.db`
  - logs/temp: `*.log`, `*.tmp`
- Guidance for running `git rm -r --cached` on tracked generated files
- `OceanExplorerApp` demo + interactive usage explicitly described

## Extra features added

- `ProbeFleetController` for concurrent/multi-probe operations on shared grid:
  - register multiple probes with unique IDs
  - execute missions in serial and asynchronous modes
  - ensure `ProbeCollisionException` when a probe tries entering occupied position

- Enhanced validation path:
  - `MultiProbeBoundaryValidator` extends single-probe validator with collision checks
  - supplier-based occupied position awareness across all registered probes

## Design Decisions

### Command Pattern
Each command maps to a dedicated handler (`MoveForwardHandler`, `TurnLeftHandler`, etc.).
Adding new commands requires zero changes to existing executing pipeline — just add a handler and register in `CommandHandlerFactory`.

### Immutable Position (Java Record)
`Position` is a Java 21 record — immutable, with value equality built in.
Every move produces a *new* Position; state operations are explicit and easier to test.

### Direction Enum owns rotation logic
`NORTH.turnRight()` returns `EAST`. Circular rotation math is centralized and stable.

### Separation of concerns
- `Navigator` — calculates the candidate next position (pure math, no validation)
- `BoundaryValidator` — checks grid boundaries and obstacles
- `ProbeController` — orchestrates command execution, state updates, and error handling
- `MissionReporter` — prints final mission summary

### Graceful halt on boundary/obstacle/collision
The probe stops at its last safe position. `MissionResult` flags `haltedEarly=true` with a reason. Subsequent commands are ignored after halt.

## Project Structure

```
src/main/java/com/natwest/oceanexplorer/
├── OceanExplorerApp.java       # Entry point/demo scenarios
├── model/                      # Value objects and enums
│   ├── Position.java
│   ├── Direction.java
│   ├── Command.java
│   └── Grid.java
├── probe/                      # Probe state and mission orchestration
│   ├── Probe.java
│   ├── ProbeController.java
│   ├── ProbeFleetController.java
│   ├── MovementHistory.java
│   └── MissionResult.java
├── command/                    # Command behavior and factory
│   ├── CommandHandler.java
│   ├── CommandParser.java
│   ├── CommandHandlerFactory.java
│   ├── MoveForwardHandler.java
│   ├── MoveBackwardHandler.java
│   ├── TurnLeftHandler.java
│   └── TurnRightHandler.java
├── navigation/
│   ├── Navigator.java
│   ├── BoundaryValidator.java
│   └── MultiProbeBoundaryValidator.java
├── report/
│   └── MissionReporter.java
└── exception/
    ├── ObstacleEncounteredException.java
    ├── OutOfBoundsException.java
    ├── InvalidCommandException.java
    └── ProbeCollisionException.java
```

## Tests

- `src/test/java/com/natwest/oceanexplorer/model/` - value object behavior
- `src/test/java/com/natwest/oceanexplorer/command/` - command parsing and handler logic
- `src/test/java/com/natwest/oceanexplorer/navigation/` - boundary and navigation tests
- `src/test/java/com/natwest/oceanexplorer/probe/` - probe controller and fleet tests
- `src/test/java/com/natwest/oceanexplorer/integration/` - full scenario e2e behavior

Total tests: 113 (all passing in this environment)

## Tech Stack

| Concern | Choice |
|---------|--------|
| Language | Java 21 |
| Build | Maven |
| Tests | JUnit 5 + Mockito + AssertJ |
| Logging | SLF4J + Logback |
| Boilerplate | Lombok |

