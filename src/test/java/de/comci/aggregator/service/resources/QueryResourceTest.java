/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources;

import de.comci.aggregator.service.representation.Index;
import de.comci.bitmap.BitMapCollection;
import de.comci.bitmap.Dimension;
import de.comci.gotcount.query.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class QueryResourceTest {

    private QueryResource instance;
    private BitMapCollection singleColumnMock;

    public QueryResourceTest() {
    }

    @Before
    public void setUp() {

        singleColumnMock = mock(BitMapCollection.class);
        when(singleColumnMock.getDimensions()).thenReturn(Arrays.asList(new Dimension<String>() {

            @Override
            public String getName() {
                return "d0";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }

            @Override
            public long getCardinality() {
                return 1l;
            }

        }));

        Map<String, BitMapCollection> maps = new HashMap<>();
        maps.put(SINGLE_COLUMN, singleColumnMock);

        instance = new QueryResource(maps);
    }
    private static final String SINGLE_COLUMN = "single column";

    @After
    public void tearDown() {
    }

    @Test
    public void listInstancesOnEmptyStorage() {
        QueryResource empty = new QueryResource(new HashMap<>());
        assertThat(empty.list().getEntity()).isEmpty();
    }

    @Test
    public void listSingleInstance() {
        final HashMap<String, BitMapCollection> hashMap = new HashMap<>();
        hashMap.put("test", BitMapCollection.create().dimension("test", String.class).add("123").build());
        QueryResource single = new QueryResource(hashMap);
        assertThat(single.list().getEntity()).containsOnly(new Index("test", 1));
    }

    @Test
    public void listMultipleInstance() {
        final HashMap<String, BitMapCollection> hashMap = new HashMap<>();

        BitMapCollection bmc = BitMapCollection.create().dimension("test", String.class).add("123").build();

        hashMap.put("test0", bmc);
        hashMap.put("test1", bmc);
        hashMap.put("test2", bmc);
        hashMap.put("test3", bmc);
        QueryResource single = new QueryResource(hashMap);
        assertThat(single.list().getEntity()).containsOnly(
                new Index("test0", 1),
                new Index("test1", 1),
                new Index("test2", 1),
                new Index("test3", 1)
        );
    }

    @Test
    public void dimensionsFromMissingCollection() {
        try {
            instance.getDimensions("missing");
            fail("missing WebApplicationException");
        } catch (WebApplicationException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(404);
        }
    }

    @Test
    public void getDimensionsForCollection() {
        assertThat(instance.getDimensions(SINGLE_COLUMN).getEntity()).containsOnly(new de.comci.aggregator.service.representation.Dimension("d0", 1l, String.class));
    }

    @Test
    public void histogramFromMissingCollection() {
        try {
            instance.getDimensionHistogram("missing", "123", Filter.fromString(SINGLE_COLUMN));
            fail("missing WebApplicationException");
        } catch (WebApplicationException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(404);
        }
    }

    @Test
    public void histogramFromMissingDimension() {
        try {
            instance.getDimensionHistogram(SINGLE_COLUMN, "dimension", Filter.fromString(""));
            fail("missing WebApplicationException");
        } catch (WebApplicationException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(404);
        }
    }

    @Test
    public void histogramFromDimension() {

        instance.getDimensionHistogram(SINGLE_COLUMN, "d0", Filter.fromString(""));
    }

}
