package Lab04.characters.properties;

public enum Expectation {
    NOTHING_HAPPENED("ничего не произойдёт"),
    EMERGENCE_WEIGHTLESSNESS("возникнет невесомость"),
    EVERYTHING_EXPLODE("всё взорвётся");

    private final String expectation;

    Expectation(String expectation) {
        this.expectation = expectation;
    }

    public String getExpectation() {
        return expectation;
    }

    @Override
    public String toString() {
        return expectation;
    }
}
