package se.walkercrou.places;

/**
 * Represents an extra, optional parameter that can be specified.
 */
public class Param {
    protected final String name;
    protected String value;

    public Param(String name) {
        this.name = name;
    }

    /**
     * Returns a new param with the specified name.
     *
     * @param name to create Param from
     * @return new param
     */
    public static Param name(String name) {
        return new Param(name);
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the value of the Param.
     *
     * @param value of param
     * @return this param
     */
    public Param value(Object value) {
        this.value = value.toString();
        return this;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Param param = (Param) o;

        if (name != null ? !name.equals(param.name) : param.name != null) return false;
        if (value != null ? !value.equals(param.value) : param.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
