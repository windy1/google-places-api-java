package se.walkercrou.places;

import java.util.List;

/**
 * Represents an extra, optional type parameter that restricts the results to places matching at least one of the specified types.
 */
public class TypeParam extends Param {

    private TypeParam(String name) {
        super(name);
    }

    /**
     * Returns a new type param with the specified name.
     *
     * @param name to create TypeParam from
     * @return new param
     */
    public static TypeParam name(String name) {
        return new TypeParam(name);
    }

    /**
     * Sets the values of the Param.
     *
     * @param values of params
     * @return this params
     */
    public Param value(List<String> values) {
        StringBuilder valuesSb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            valuesSb.append(values.get(i));
            if (i != (values.size() - 1)) {
                valuesSb.append("%7C"); // it represents a pipeline character |
            }
        }
        this.value = valuesSb.toString();
        return this;
    }


}
