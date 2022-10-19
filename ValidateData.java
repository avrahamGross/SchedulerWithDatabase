package sample;

import java.time.ZonedDateTime;

/**
 * A Functional Interface to confirm valid input ZonedDateTime
 */
public interface ValidateData {
    /**
     * Test input data
     * @param t
     * @return boolean if valid data
     */
    public boolean test(ZonedDateTime t);
}
