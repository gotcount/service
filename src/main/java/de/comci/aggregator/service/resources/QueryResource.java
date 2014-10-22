/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources;

import com.codahale.metrics.annotation.Timed;
import de.comci.aggregator.service.representation.AggregateResult;
import de.comci.aggregator.service.representation.Query;
import de.comci.bitmap.BitMapCollection;
import de.comci.bitmap.BitMapDimension;
import de.comci.bitmap.SortDirection;
import de.comci.gotcount.query.Filter;
import java.util.HashMap;
import java.util.LinkedList;
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
@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {

    private final Map<String, BitMapCollection> indices;

    public QueryResource(Map<String, BitMapCollection> indices) {
        this.indices = indices;
    }

    @GET
    @Timed
    public GenericEntity<List<String>> list() {
        return new GenericEntity<>(new LinkedList<>(indices.keySet()), List.class);
    }

    /**
     * Central function to retrieve a single collection from storage. Needed to
     * ensure we throw the same exception whenever a collection does not exist.
     *
     * @param index
     * @return
     */
    BitMapCollection getCollection(String index) {

        if (!indices.containsKey(index)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        return indices.get(index);
    }

    @GET
    @Path("/{index}")
    @Timed
    public GenericEntity<List<String>> getDimensions(
            @PathParam("index") String index) {

        return new GenericEntity<>(getCollection(index).getDimensions().stream().map(d -> d.getName()).collect(Collectors.toList()), List.class);

    }

    @GET
    @Path("/{index}/{dimension}")
    @Timed
    public <T> AggregateResult getDimensionHistogram(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @QueryParam("query") @DefaultValue("") Filter query) {

        return getDimensionHistogram(index, dimension, 10, query);

    }

    @GET
    @Path("/{index}/{dimension}/{topN}")
    @Timed
    public <T> AggregateResult getDimensionHistogram(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @PathParam("topN") @DefaultValue("10") int limit,
            @QueryParam("query") @DefaultValue("") Filter query) {

        if (getCollection(index).getDimensions().stream().noneMatch(d -> d.getName().equals(dimension))) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        Map<String, Predicate> filters = new HashMap<>();
        getCollection(index).getDimensions().stream().filter(p -> query.getDimensions().contains(p.getName())).forEach(d -> {
            filters.put(d.getName(), p -> query.getPredicate(d.getName()).test(p));
        });

        return new AggregateResult(
                new Query(dimension, Math.abs(limit), (limit > 0) ? SortDirection.DESCENDING : SortDirection.ASCENDING),
                getCollection(index).histogram(dimension, filters, limit));

    }

}
