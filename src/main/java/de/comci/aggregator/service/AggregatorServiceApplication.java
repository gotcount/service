/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service;

import de.comci.aggregator.service.configuration.Datastore;
import de.comci.aggregator.service.health.BasicHealthCheck;
import de.comci.aggregator.service.resources.QueryResource;
import de.comci.bitmap.BitMapCollection;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AggregatorServiceApplication extends Application<AggregatorServiceConfiguration> {

    private final Map<String, BitMapCollection> indices = new HashMap<>();

    public static void main(String[] args) throws Exception {
        new AggregatorServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<AggregatorServiceConfiguration> bootstrap) {
        // nop
    }

    @Override
    public void run(AggregatorServiceConfiguration configuration,
            Environment environment) {

        // create bit map indices
        for (Datastore store : configuration.getDatastores()) {
            try {
                Connection connect = DriverManager.getConnection(
                        store.getJdbc(),
                        store.getUser(),
                        store.getPwd());
                            
                indices.put(store.getTable(), BitMapCollection.create(connect, store.getTable(), store.getColumns().split(",")).get());
            } catch (SQLException ex) {
                Logger.getLogger(AggregatorServiceApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // resources
        final QueryResource qResource = new QueryResource(indices);
        environment.jersey().register(qResource);

        // health checks
        final BasicHealthCheck bHealth = new BasicHealthCheck();
        environment.healthChecks().register("basic", bHealth);

    }

}
