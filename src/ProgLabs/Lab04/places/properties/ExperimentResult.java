package Lab04.places.properties;

public enum ExperimentResult {
    NOTHING_HAPPENED("ничего не произошло"),
    EMERGENCE_WEIGHTLESSNESS("возникла невесомость"),
    BLACK_HOLE_APPEARED("возникла чёрная дыра"),
    EVERYTHING_EXPLODE("всё взорвалось");

    private final String result;

    ExperimentResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result;
    }
}
