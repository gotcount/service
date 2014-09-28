/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.comci.aggregator.service.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.comci.bitmap.Operation;
import de.comci.bitmap.SortDirection;
import org.hibernate.validator.constraints.*;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class Query {
    
    private Operation operation = Operation.COUNT;
    
    @Length(min = 1)
    private String dimension;
    
    @NotEmpty
    private int limit = 10;
        
    private SortDirection sort = SortDirection.DESCENDING;
    
    public Query() {
        // Jackson deserialization
    }

    public Query(String dimension, int limit, SortDirection sort) {
        this(dimension, limit, sort, Operation.COUNT);
    }
    
    public Query(String dimension, int limit, SortDirection sort, Operation operation) {
        this.dimension = dimension;
        this.limit = limit;
        this.sort = sort;
        this.operation = operation;
    }
    
    @JsonProperty
    public String getDimension() {
        return dimension;
    }

    @JsonProperty
    public int getLimit() {
        return limit;
    }

    @JsonProperty
    public SortDirection getSort() {
        return sort;
    }

    @JsonProperty
    public Operation getOperation() {
        return operation;
    }
        
}
