package se.walkercrou.places;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ParamTest {

    public static final String ARBITRARY_NAME = "name";
    public static final String ARBITRARY_VALUE = "value";
    private Param defaultParam;

    @Before
    public void setUp() {
        defaultParam = Param.name(ARBITRARY_NAME).value(ARBITRARY_VALUE);
    }

    @Test
    public void givenTwoParamsWithTheSameNameAndValueThenTheyShouldBeEqual() {
        Param secondParam = Param.name(ARBITRARY_NAME).value(ARBITRARY_VALUE);

        assertEquals(defaultParam, secondParam);
    }

    @Test
    public void givenTwoParamsWithDifferentNamesThenTheyShouldNotBeEqual() {
        Param secondParam = Param.name("DIFFERENT NAME").value(ARBITRARY_VALUE);

        assertNotEquals(defaultParam, secondParam);
    }

    @Test
    public void givenTwoParamsWithTheSameNameAndDifferentValuesThenTheyShouldNotBeEqual() {
        Param secondParam = Param.name(ARBITRARY_NAME).value("DIFFERENT VALUES");

        assertNotEquals(defaultParam, secondParam);
    }

    @Test
    public void givenANameThenItCanBeRetrieved() {
        defaultParam = Param.name(ARBITRARY_NAME);

        assertEquals(defaultParam.getName(), ARBITRARY_NAME);
    }

    @Test
    public void givenAValueThenItCanBeRetrieved() {
        this.defaultParam = Param.name(ARBITRARY_NAME).value(ARBITRARY_VALUE);

        assertEquals(defaultParam.getValue(), ARBITRARY_VALUE);
    }
}
