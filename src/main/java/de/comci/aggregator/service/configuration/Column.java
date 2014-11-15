/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class Column {
    
    @NotNull
    @NotEmpty
    @JsonProperty
    String name;
    
    @JsonProperty
    String alias;
    
    @NotNull
    @NotEmpty
    Class<?> type; 
    
    @JsonProperty
    Double precision = null;

    public Column() {
        // jackson
    }

    public String getName() {
        return name;
    }
    
    @JsonProperty
    public Class<?> getType() {
        return type;
    }
    
    @JsonProperty
    public void setType(String type) throws ClassNotFoundException {
        this.type = Class.forName(type);
    }

    public Double getPrecision() {
        return precision;
    }

    public String getAlias() {
        return alias;
    }
    
    public String getLabel() {
        return (alias != null && !alias.isEmpty()) ? alias : name;
    }
           
}
