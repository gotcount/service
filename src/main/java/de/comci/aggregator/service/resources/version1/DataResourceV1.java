/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources.version1;

import com.codahale.metrics.annotation.Timed;
import de.comci.aggregator.service.representation.Dimension;
import de.comci.bitmap.BitMapCollection;
import de.comci.gotcount.query.Filter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
@Path("/v1/data")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class DataResourceV1 extends DefaultResourceV1 {

    public DataResourceV1(Map<String, BitMapCollection> indices) {
        super(indices);
    }
    
    @GET
    @Path(value = "/{index}")
    @Timed
    public GenericEntity<List<Dimension>> getDimensions(@PathParam(value = "index") String index) {
        final List<Dimension> list = getCollection(index).getDimensions().stream().map((de.comci.bitmap.Dimension d) -> new Dimension(d.getName(), d.getCardinality(), d.getType())).collect(Collectors.toList());
        return new GenericEntity<>(list, List.class);
    }

    @GET
    @Path("/{index}/{dimension}")
    @Timed
    public <T> Response getData(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @QueryParam("query") @DefaultValue("") Filter query) {

        return getData(index, dimension, query, 0, 100);

    }

    @GET
    @Path("/{index}/{dimension}/{offset},{limit}")
    @Timed
    public <T> Response getData(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @QueryParam("query") @DefaultValue("") Filter query,
            @PathParam("offset") @DefaultValue("0") int offset,
            @PathParam("limit") @DefaultValue("100") int limit) {
        
        final BitMapCollection indexCollection = getCollection(index);

        List<String> dimensions = Arrays.asList(dimension.split(","));
        
        if (indexCollection.getDimensions().stream().noneMatch(d -> dimensions.contains(d.getName()))) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        Map<String, Predicate> filters = filterToMap(indexCollection, query);
        
        return Response.ok(indexCollection.getData(filters, offset, limit, dimensions)).build();
        
    }

}
