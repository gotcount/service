/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources.version1;

import com.codahale.metrics.annotation.Timed;
import de.comci.bitmap.BitMapCollection;
import de.comci.gotcount.query.Filter;
import java.util.Map;
import java.util.function.Predicate;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
@Path("/v1/count")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class SizeResourceV1 extends DefaultResourceV1 {

    public SizeResourceV1(Map<String, BitMapCollection> indices) {
        super(indices);
    }
    
    @GET
    @Path("/{index}")
    @Timed
    public int getFilteredSize(
            @PathParam("index") String index,
            @QueryParam("query") @DefaultValue("") Filter query) {

        final BitMapCollection indexCollection = getCollection(index);
        
        Map<String, Predicate> filters = filterToMap(indexCollection, query);
        
        return getCollection(index).size(filters);
        
    }
            
}
