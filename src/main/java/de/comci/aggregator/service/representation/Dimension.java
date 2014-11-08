/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class Dimension {
    
    @JsonProperty
    String name;
    
    @JsonProperty
    Class type;
    
    @JsonProperty
    long cardinality;
    
    public Dimension() {
        // jackson
    }

    public Dimension(String name, long cardinality, Class type) {
        this.name = name;
        this.cardinality = cardinality;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + (int) (this.cardinality ^ (this.cardinality >>> 32));
        hash = 67 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dimension other = (Dimension) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.cardinality != other.cardinality) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s:%s(%d)", type.getSimpleName(), name, cardinality);
    }
            
}
