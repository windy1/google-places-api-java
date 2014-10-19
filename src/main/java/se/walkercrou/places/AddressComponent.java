package se.walkercrou.places;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single component in a Place's full address.  For example, the address "111 8th Avenue, New York, NY"
 * contains separate address components for "111" (the street number, "8th Avenue" (the route), "New York" (the city)
 * and "NY" (the US state).
 */
public class AddressComponent {
    private final List<String> types = new ArrayList<>();
    private String longName, shortName;

    protected AddressComponent() {
    }

    /**
     * Returns the long name of the component.
     *
     * @return long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets the long name of the component.
     *
     * @param longName of component
     * @return this
     */
    protected AddressComponent setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    /**
     * Returns the short name of the component. For example, "New York" might be abbreviated as "NY".
     *
     * @return short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name of the component. For example, "New York" might be abbreviated as "NY".
     *
     * @param shortName of component
     * @return this
     */
    protected AddressComponent setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    /**
     * Adds a collection of types to this components list of types.
     *
     * @param types to add
     * @return this
     */
    protected AddressComponent addTypes(Collection<String> types) {
        this.types.addAll(types);
        return this;
    }

    /**
     * Adds a type to this components list of types
     *
     * @param type to add
     * @return this
     */
    protected AddressComponent addType(String type) {
        types.add(type);
        return this;
    }

    /**
     * Removes the specified type from the list of types.
     *
     * @param type to remove
     * @return this
     */
    protected AddressComponent removeType(String type) {
        types.remove(type);
        return this;
    }

    /**
     * Clears all of this components types.
     *
     * @return this
     */
    protected AddressComponent clearTypes() {
        types.clear();
        return this;
    }

    /**
     * Returns an unmodifiable list of this components types.
     *
     * @return types
     */
    public List<String> getTypes() {
        return Collections.unmodifiableList(types);
    }
}
