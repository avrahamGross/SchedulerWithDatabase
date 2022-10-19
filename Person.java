package sample;

/**
 * A Class to make a Person
 * @author Avraham Gross
 * @version 1.0
 */
public class Person {
    private final int id;
    private String name;

    /**
     * Constructor for Person
     * @param id
     * @param name
     */
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Return id
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Return name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
