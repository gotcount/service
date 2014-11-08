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
        final Map<String, Integer> h = new LinkedHashMap<>();
        result.entrySet(false).stream().filter(e -> e.getValue() > 0).forEach(e -> h.put(e.getKey().getLabel(), e.getValue()));
        return h;
    }
    
}
