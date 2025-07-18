# Asgardus Stats API Usage Guide

## Overview
The Asgardus Stats API provides a robust and thread-safe way to manage RPG-style player statistics, including support for temporary stat boosts and percentage-based modifications.

## Getting Started

### Accessing the API
All stat operations are performed via the static methods in `AsgStatsAPI`. You can use either a `UUID` or a `Player` object for all API calls.

### Example Usage
```java
// Add 10 to the STRENGTH stat for a player
AsgStatsAPI.addStat(player, StatType.STRENGTH, 10);

// Set the DEXTERITY stat to 50
AsgStatsAPI.setStat(player, StatType.DEXTERITY, 50);

// Add a temporary boost of +20 to INTELLIGENCE for 30 seconds
AsgStatsAPI.addTempStat(player, StatType.INTELLIGENCE, 20, 30);

// Add a 150% increase to the VITALITY stat
AsgStatsAPI.addStatPercent(player, StatType.VITALITY, 150);

// Clear all temporary stat boosts
AsgStatsAPI.clearAllTempStats(player);
```

## Thread Safety
- All stat operations are thread-safe. You can safely call API methods from asynchronous tasks or event handlers.
- Temporary stat boosts are managed internally and are reverted automatically after their duration expires.

## Best Practices
- Always use the API methods instead of accessing `PlayerData` directly.
- Use `addTempStat` for buffs/debuffs that should expire automatically.
- Use `clearAllTempStats` to remove all active temporary boosts (e.g., on player logout or death).
- Avoid direct manipulation of the underlying stat map to ensure thread safety and consistency.

## Troubleshooting
- If you encounter issues with stats not updating as expected, ensure you are using the correct `Player` or `UUID` reference.
- For plugin developers: always pass the main plugin instance where required.

## API Reference
See the JavaDoc comments in `AsgStatsAPI.java` and `PlayerData.java` for detailed method documentation and usage notes.

---
