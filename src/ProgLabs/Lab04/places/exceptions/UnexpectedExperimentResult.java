package Lab04.places.exceptions;

import Lab04.characters.properties.Expectation;
import Lab04.places.properties.ExperimentResult;

public class UnexpectedExperimentResult extends Exception {
    public UnexpectedExperimentResult(Expectation expectation, ExperimentResult experimentResult) {
        super(
                String.format(
                        "Неожиданный результат опыта: Ожидалось: %s, результат: %s",
                        expectation, experimentResult
                )
        );
    }
}
