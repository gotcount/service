/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources;

import com.codahale.metrics.annotation.Timed;
import de.comci.aggregator.service.representation.Dimension;
import de.comci.aggregator.service.representation.Index;
import de.comci.bitmap.BitMapCollection;
import de.comci.gotcount.query.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public abstract class DefaultResourceV1 {
    
    final Map<String, BitMapCollection> indices;

    public DefaultResourceV1(Map<String, BitMapCollection> indices) {
        this.indices = indices;
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
    @Path(value = "/{index}")
    @Timed
    public GenericEntity<List<Dimension>> getDimensions(@PathParam(value = "index") String index) {
        final List<Dimension> list = getCollection(index).getDimensions().stream().map((de.comci.bitmap.Dimension d) -> new Dimension(d.getName(), d.getCardinality(), d.getType())).collect(Collectors.toList());
        return new GenericEntity<>(list, List.class);
    }

    @GET
    @Timed
    public GenericEntity<List<Index>> list() {
        List<Index> ixs = indices.entrySet().stream().map((Map.Entry<String, BitMapCollection> e) -> new Index(e.getKey(), e.getValue().size())).collect(Collectors.toList());
        return new GenericEntity<>(ixs, List.class);
    }

    Map<String, Predicate> filterToMap(final BitMapCollection indexCollection, Filter query) {
        Map<String, Predicate> filters = new HashMap<>();
        indexCollection.getDimensions().stream().filter((p) -> query.getDimensions().contains(p.getName())).forEach((d) -> {
            filters.put(d.getName(), (p) -> query.getPredicate(d.getName()).test(p));
        });
        return filters;
    }
    
}
