package se.walkercrou.places;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TypeParamTest {

    private static final String ARBITRARY_NAME = "name";
    private static final String ARBITRARY_VALUE_1 = "test";
    private static final String ARBITRARY_VALUE_2 = "test2";

    private TypeParam defaultTypeParam;
    private TypeParam defaultMultiValueParam;

    @Before
    public void setUp() {
        defaultTypeParam = (TypeParam)TypeParam.name(ARBITRARY_NAME).value(ARBITRARY_VALUE_1);
        defaultMultiValueParam = (TypeParam)TypeParam.name(ARBITRARY_NAME).value(Arrays.asList(ARBITRARY_VALUE_1, ARBITRARY_VALUE_2));
    }

    @Test
    public void givenTwoTypeParamsWithTheSameNameAndValueThenTheyAreEqual() {
        TypeParam secondTypeParam = (TypeParam)TypeParam.name(ARBITRARY_NAME).value(ARBITRARY_VALUE_1);

        assertEquals(defaultTypeParam, secondTypeParam);
    }

    @Test
    public void givenTwoTypeParamsWithDifferentNamesThenTheyShouldNotBeEqual() {
        TypeParam secondParam = (TypeParam)TypeParam.name("DIFFERENT NAME").value(ARBITRARY_VALUE_1);

        assertNotEquals(defaultTypeParam, secondParam);
    }

    @Test
    public void givenTwoTypeParamsWithTheSameMultipleValuesThenTheyShouldBeEqual() {
        TypeParam secondMultiValueParam = (TypeParam)TypeParam.name("DIFFERENT NAME").value(Arrays.asList(ARBITRARY_VALUE_1, ARBITRARY_VALUE_2));

        assertNotEquals(defaultMultiValueParam, secondMultiValueParam);
    }

    @Test
    public void givenTwoTypeParamsWithTheSameNameAndDifferentValuesThenTheyShouldNotBeEqual() {
        TypeParam secondParam = (TypeParam)TypeParam.name(ARBITRARY_NAME).value("DIFFERENT VALUES");

        assertNotEquals(defaultTypeParam, secondParam);
    }

    @Test
    public void givenANameThenItCanBeRetrieved() {
        defaultTypeParam = TypeParam.name(ARBITRARY_NAME);

        assertEquals(defaultTypeParam.getName(), ARBITRARY_NAME);
    }

    @Test
    public void givenAValueThenItCanBeRetrieved() {
        defaultTypeParam = (TypeParam)TypeParam.name(ARBITRARY_NAME).value(ARBITRARY_VALUE_1);

        assertEquals(defaultTypeParam.getValue(), ARBITRARY_VALUE_1);
    }
}
