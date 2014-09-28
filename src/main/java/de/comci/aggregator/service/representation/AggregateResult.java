/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.comci.aggregator.service.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Multiset;
import de.comci.bitmap.Value;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class AggregateResult {
    
    private Query query;
    
    private Multiset<Value> result;

    public AggregateResult() {
        // Jackson deserialization
    }

    public AggregateResult(Query query, Multiset<Value> histogram) {
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
        result.entrySet().stream().sorted((e0, e1) -> e1.getCount() - e0.getCount()).forEach(e -> h.put(e.getElement().getLabel(), e.getCount()));
        return h;
    }
    
}
