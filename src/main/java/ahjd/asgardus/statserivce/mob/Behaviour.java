package ahjd.asgardus.statserivce.mob;

public enum Behaviour {
    PASSIVE, //Does not react
    NEUTRAL, //Passive until touched then aggressive until target dies or 10s pass without a target in range
    AGGRESSIVE, //attack on sight
    PROTECTIVE //Passive unless you get too close, does not move too much from spawn location
}