# Quiz Leaderboard System

A Spring Boot application built for the Bajaj Finserv Health Java Qualifier Assignment (SRM, April 2026).

## Problem Statement

The system simulates a distributed quiz show where multiple participants receive scores across rounds. The validator API delivers quiz events across 10 polls, and the same event data may appear in multiple polls. The core challenge is deduplicating events correctly and computing an accurate leaderboard.

## Objective

Poll the validator API 10 times, deduplicate events using a composite key of `roundId + participant`, aggregate scores per participant, and submit the final leaderboard once.

## Approach

**Polling**
The application polls the API exactly 10 times with poll indices 0 through 9. A mandatory delay of 5 seconds is maintained between each poll request, resulting in a total runtime of approximately 50 seconds.

**Deduplication**
Each quiz event is identified by a composite key formed from `roundId` and `participant`. A `HashSet` tracks all seen keys. If an event with the same key appears again in a later poll, it is silently ignored. This prevents double-counting scores in a distributed system where the same event can be delivered multiple times.

**Score Aggregation**
Scores are accumulated per participant using a `HashMap`. Only unique events contribute to the total. After all polls are complete, the map is converted to a sorted leaderboard in descending order of total score.

**Submission**
The leaderboard is submitted exactly once to the validator's POST endpoint after all 10 polls are complete.

## Project Structure

```
src/
└── main/
    └── java/com/srmquiz/quizleaderboard/
        ├── QuizLeaderboardApplication.java
        ├── model/
        │   ├── LeaderboardEntry.java
        │   ├── PollResponse.java
        │   └── QuizEvent.java
        ├── runner/
        │   └── QuizRunner.java
        └── service/
            └── QuizService.java
```

## Tech Stack

- Java 21
- Spring Boot 4.0.6
- Maven
- Lombok

## How to Run

**Prerequisites**
- Java 21 or higher
- Maven 3.6 or higher

**Steps**

```bash
git clone https://github.com/Radha-7git/distributed-quiz-processor.git
cd distributed-quiz-processor
mvn spring-boot:run
```

The application will poll the API 10 times, print the leaderboard to the console, and submit it automatically.

## API Reference

**Base URL:** `https://devapigw.vidalhealthtpa.com/srm-quiz-task`

**GET /quiz/messages**

Fetches quiz events for a given poll index.

| Parameter | Type   | Description              |
|-----------|--------|--------------------------|
| regNo     | String | Registration number      |
| poll      | int    | Poll index, 0 through 9  |

Sample response:
```json
{
  "regNo": "RA2311003020407",
  "setId": "SET_1",
  "pollIndex": 0,
  "events": [
    { "roundId": "R1", "participant": "Alice", "score": 10 },
    { "roundId": "R1", "participant": "Bob", "score": 20 }
  ]
}
```

**POST /quiz/submit**

Submits the final leaderboard.

```json
{
  "regNo": "RA2311003020407",
  "leaderboard": [
    { "participant": "George", "totalScore": 795 },
    { "participant": "Hannah", "totalScore": 750 },
    { "participant": "Ivan",   "totalScore": 745 }
  ]
}
```

## Output

```
=== LEADERBOARD ===
George -> 795
Hannah -> 750
Ivan   -> 745
Total Score: 2290
===================

Submission response: {regNo=RA2311003020407, totalPollsMade=30, submittedTotal=2290, attemptCount=3}
```

## Requirements Checklist

| Requirement                              | Status |
|------------------------------------------|--------|
| Poll API exactly 10 times (index 0-9)    | Done   |
| 5-second delay between polls             | Done   |
| Deduplicate using roundId + participant  | Done   |
| Aggregate scores per participant         | Done   |
| Sort leaderboard by total score          | Done   |
| Submit leaderboard exactly once          | Done   |

## Author

Registration No: RA2311003020407  
Institution: SRM Institute of Science and Technology  
Assignment: Bajaj Finserv Health Java Qualifier, April 2026
