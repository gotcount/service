/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.representation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Predicate;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class Filter implements Map<String, Predicate> {

    Map<String, Predicate> filters = new HashMap<>();

    private Filter() {
        // only through fromString()
    }

    /**
     * Special Chars: :!,;()[]~
     *
     * @param input
     * @return
     */
    public static Filter fromString(String input) {
        Filter f = new Filter();
        StringTokenizer st = new StringTokenizer(input, ":!,;()[]~", true);
        
        String field = null;
        boolean not = false;        
        Operation op = Operation.EQUALS;
        List<String> values = new LinkedList<>();
        int state = 0;
        
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            switch (token) {
                case ":": break;
                case "!": not = true; break;
                case ",": break;
                case ";": 
                    Predicate p = op.getPredicate(values.toArray());
                    if (not) {
                        f.filters.put(field, v -> !p.test(v));
                    } else {
                        f.filters.put(field, p);
                    }
                    state = 0; not = false; field = null; op = null; values.clear(); break;
                case "(": state++; break;
                case ")": state--; break;
                case "[": state++; break;
                case "]": state--; break;
                case "~": op = Operation.BETWEEN; break;
                default:
                    switch (state) {
                        case 0: field = token; state++; break;
                    }
            }
        }
        return f;
    }
    
    @Override
    public int size() {
        return filters.size();
    }

    @Override
    public boolean isEmpty() {
        return filters.isEmpty();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean containsKey(Object key) {
        return filters.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return filters.containsValue(value);
    }

    @Override
    public Predicate get(Object key) {
        return filters.get(key);
    }

    @Override
    public Predicate put(String key, Predicate value) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public Predicate remove(Object key) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Predicate> m) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(filters.keySet());
    }

    @Override
    public Collection<Predicate> values() {
        return Collections.unmodifiableCollection(filters.values());
    }

    @Override
    public Set<Entry<String, Predicate>> entrySet() {
        return Collections.unmodifiableSet(filters.entrySet());
    }

}
