/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service;

import de.comci.aggregator.service.configuration.Datastore;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class AggregatorServiceConfiguration extends Configuration {

    @JsonProperty
    private ImmutableList<Datastore> datastores;

    public ImmutableList<Datastore> getDatastores() {
        return datastores;
    }

    public void setDatastores(ImmutableList<Datastore> datastores) {
        this.datastores = datastores;
    }
    
    
    
}
