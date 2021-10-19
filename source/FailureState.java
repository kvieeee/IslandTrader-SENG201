package main;

/**
 * Enum to represent failures to send back to the user if they attempt certain actions
 */
public enum FailureState {
    SUCCESS("Success"),
    NOMONEY("You don't have enough money"),
    NOTIME("You don't have enough time"),
    NOSPACE("You don't have enough space for that Item"),
    NOITEM("You don't have that to sell"),
	MUSTREPAIR("You need to repair your ship damage before you sail"),
	UNKNOWN("OPPSIE OPPS OPPS"),
	GAMEOVER_SOFT("You don't have enough money or time to sail anywhere"),
	GAMEOVER_HARD("GAMEOVER. Time to sleep with the fishes");

    public final String name;

    FailureState(String name) {
        this.name = name;
    }
}