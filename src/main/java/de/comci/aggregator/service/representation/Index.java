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
public class Index {

    @JsonProperty
    String name;

    @JsonProperty
    int elements;

    public Index() {
        // jackson
    }

    public Index(String name, int elements) {
        this.name = name;
        this.elements = elements;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + this.elements;
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
        final Index other = (Index) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.elements != other.elements) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", name, elements);
    }
    
}
