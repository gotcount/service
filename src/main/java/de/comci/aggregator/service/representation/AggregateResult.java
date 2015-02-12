/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.comci.aggregator.service.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.comci.bitmap.Value;
import de.comci.histogram.Histogram;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class AggregateResult {
    
    private Query query;
    
    private Histogram<Value> result;

    public AggregateResult() {
        // Jackson deserialization
    }

    public AggregateResult(Query query, Histogram<Value> histogram) {
        this.query = query;
        this.result = histogram;
    }

    @JsonProperty
    public Query getQuery() {
        return query;
    }

    @JsonProperty
    public Map<String, Integer> getAggregate() {
        return result
                .stream()
                //.sorted((a,b) -> b.getCount().compareTo(a.getCount()))
                .collect(
                    Collectors.toConcurrentMap(
                        e -> e.getKey().getLabel(), 
                        e -> e.getCount()
                    )
                );
    }
    
}
