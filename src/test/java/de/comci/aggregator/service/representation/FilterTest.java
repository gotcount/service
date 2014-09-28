/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.representation;

import java.util.Arrays;
import java.util.function.Predicate;
import static org.fest.assertions.api.Assertions.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class FilterTest {

    public FilterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void singleFieldSingleValue() {

        String input = "field:value";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test("value")).isTrue();
        assertThat(instance.get("field").test("v")).isFalse();

    }

    @Test
    public void singleFieldSingleValueInList() {

        String input = "field:[value]";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test("value")).isTrue();
        assertThat(instance.get("field").test("v")).isFalse();

    }

    @Test
    public void singleFieldMultipleValuesInList() {

        String input = "field:[value,values,many,more]";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test("value")).isTrue();
        assertThat(instance.get("field").test("values")).isTrue();
        assertThat(instance.get("field").test("many")).isTrue();
        assertThat(instance.get("field").test("more")).isTrue();
        assertThat(instance.get("field").test("much")).isFalse();

    }

    @Test
    public void singleFieldInvertedSingleValue() {

        String input = "field:!value";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test("v")).isTrue();
        assertThat(instance.get("field").test("value")).isFalse();

    }

    @Test
    public void singleFieldInvertedMultipleValuesInList() {

        String input = "field:![value,values,many,more]";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test("value")).isFalse();
        assertThat(instance.get("field").test("values")).isFalse();
        assertThat(instance.get("field").test("many")).isFalse();
        assertThat(instance.get("field").test("more")).isFalse();
        assertThat(instance.get("field").test("much")).isTrue();

    }

    @Test
    public void singleFieldBetweenIncInc() {

        String input = "field:[5~10]";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test(4)).isFalse();
        assertThat(instance.get("field").test(5)).isTrue();
        assertThat(instance.get("field").test(9.99999)).isTrue();
        assertThat(instance.get("field").test(10)).isTrue();
        assertThat(instance.get("field").test(10.001)).isFalse();
        assertThat(instance.get("field").test(11)).isFalse();

    }

    @Test
    public void singleFieldBetweenExcInc() {

        String input = "field:(5~10]";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test(5)).isFalse();
        assertThat(instance.get("field").test(5.001)).isTrue();
        assertThat(instance.get("field").test(6)).isTrue();
        assertThat(instance.get("field").test(9.99999)).isTrue();
        assertThat(instance.get("field").test(10)).isTrue();
        assertThat(instance.get("field").test(10.001)).isFalse();
        assertThat(instance.get("field").test(11)).isFalse();

    }
    
    @Test
    public void singleFieldBetweenExcExc() {

        String input = "field:(5~10)";
        Filter instance = Filter.fromString(input);

        assertThat(instance.containsKey("field")).isTrue();
        assertThat(instance.get("field").test(5)).isFalse();
        assertThat(instance.get("field").test(5.001)).isTrue();
        assertThat(instance.get("field").test(6)).isTrue();        
        assertThat(instance.get("field").test(9)).isTrue();
        assertThat(instance.get("field").test(9.99999)).isTrue();
        assertThat(instance.get("field").test(10)).isFalse();
        assertThat(instance.get("field").test(11)).isFalse();

    }
    
    @Test
    public void twoFieldsSingleValues() {
        
        String input = "one:uno;two:[dos]";
        Filter instance = Filter.fromString(input);
        
        assertThat(instance.containsKey("one")).isTrue();
        assertThat(instance.containsKey("two")).isTrue();
        assertThat(instance.get("one").test("uno")).isTrue();
        assertThat(instance.get("one").test("dos")).isFalse();
        assertThat(instance.get("two").test("uno")).isFalse();
        assertThat(instance.get("two").test("dos")).isTrue();
        
    }

}
