/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.comci.aggregator.service.representation.AggregateResult;
import de.comci.aggregator.service.representation.Filter;
import de.comci.aggregator.service.representation.Query;
import de.comci.bitmap.BitMapCollection;
import de.comci.bitmap.SortDirection;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

    @GET
    @Path("/{index}")
    @Timed
    public GenericEntity<List<String>> getDimensions(
            @PathParam("index") String index) {

        if (!indices.containsKey(index)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        return new GenericEntity<>(indices.get(index).getDimensions().stream().map(d -> d.getName()).collect(Collectors.toList()), List.class);

    }

    @GET
    @Path("/{index}/{dimension}")
    @Timed
    public <T> AggregateResult getDimensionHistogram(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @Context UriInfo ui) {

        return getDimensionHistogram(index, dimension, 10, ui);

    }

    @GET
    @Path("/{index}/{dimension}/{topN}")
    @Timed
    public <T> AggregateResult getDimensionHistogram(
            @PathParam("index") String index,
            @PathParam("dimension") String dimension,
            @PathParam("topN") @DefaultValue("10") int limit,
            @Context UriInfo ui) {

        if (!indices.containsKey(index)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        if (indices.get(index).getDimensions().stream().noneMatch(d -> d.getName().equals(dimension))) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        Map<String, Predicate> filters = new HashMap<>();
        indices.get(index).getDimensions().stream().forEach(d -> {
            final List<String> values = ui.getQueryParameters().get(d.getName());
            if (values != null && !values.isEmpty()) {
                filters.put(d.getName(), p -> values.contains(p));
            }
        });

        return new AggregateResult(
                new Query(dimension, Math.abs(limit), (limit > 0) ? SortDirection.DESCENDING : SortDirection.ASCENDING),
                indices.get(index).histogram(dimension, filters, limit));

    }

}
