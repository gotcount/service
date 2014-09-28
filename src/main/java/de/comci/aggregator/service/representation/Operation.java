/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.comci.aggregator.service.representation;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public enum Operation {

    EQUALS,
    IN_LIST,
    BETWEEN,
    BETWEEN_LOWER_EXCLUDED,
    BETWEEN_UPPER_EXCLUDED,
    BETWEEN_BOTH_EXCLUDED;
    
    /**
     *
     * @param <T>
     * @param values
     * @return
     */
    public <T> Predicate getPredicate(T... values) {
        
        Comparable lower, upper;
        
        switch (this) {
            case EQUALS:
                final T v = values[0];
                return p -> p.equals(v);
            case IN_LIST:
                return p -> Arrays.asList(values).contains(p);
            case BETWEEN:
                lower = (Comparable)values[0];
                upper = (Comparable)values[1];                    
                return p -> {
                    if (!Comparable.class.isAssignableFrom(p.getClass()))
                        throw new IllegalArgumentException("not comparable");
                    Comparable c = (Comparable)p;                    
                    return lower.compareTo(c) <= 0 && upper.compareTo(c) >= 0;
                };
            case BETWEEN_LOWER_EXCLUDED:
                lower = (Comparable)values[0];
                upper = (Comparable)values[1];                    
                return p -> {
                    if (!Comparable.class.isAssignableFrom(p.getClass()))
                        throw new IllegalArgumentException("not comparable");
                    Comparable c = (Comparable)p;                    
                    return lower.compareTo(c) < 0 && upper.compareTo(c) >= 0;
                };
            case BETWEEN_UPPER_EXCLUDED:
                lower = (Comparable)values[0];
                upper = (Comparable)values[1];                    
                return p -> {
                    if (!Comparable.class.isAssignableFrom(p.getClass()))
                        throw new IllegalArgumentException("not comparable");
                    Comparable c = (Comparable)p;                    
                    return lower.compareTo(c) <= 0 && upper.compareTo(c) > 0;
                };
            case BETWEEN_BOTH_EXCLUDED:
                lower = (Comparable)values[0];
                upper = (Comparable)values[1];                    
                return p -> {
                    if (!Comparable.class.isAssignableFrom(p.getClass()))
                        throw new IllegalArgumentException("not comparable");
                    Comparable c = (Comparable)p;                    
                    return lower.compareTo(c) < 0 && upper.compareTo(c) > 0;
                };
        }
        return null;        
    }
    
}
