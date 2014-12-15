/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service;

import de.comci.aggregator.service.configuration.Datastore;
import de.comci.aggregator.service.health.BasicHealthCheck;
import de.comci.aggregator.service.resources.Versions;
import de.comci.aggregator.service.resources.version1.DataResourceV1;
import de.comci.aggregator.service.resources.version1.HistogramResourceV1;
import de.comci.aggregator.service.resources.version1.SizeResourceV1;
import de.comci.aggregator.service.resources.version1.V1;
import de.comci.bitmap.BitMapCollection;
import de.comci.bitmap.JooqDimensionBuilder;
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

                JooqDimensionBuilder dim = new JooqDimensionBuilder(
                        connect,
                        store.getTable()
                );
                
                store.getColumns().stream().forEach(c -> dim.dimension(c.getName(), c.getType(), c.getPrecision()));
                indices.put(store.getTableLabel(), dim.getCollectionBuilder().build());
                
            } catch (SQLException ex) {
                Logger.getLogger(AggregatorServiceApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // resources
        final Versions versionOverview = new Versions();
        environment.jersey().register(versionOverview);
        
        final V1 version1Info = new V1();
        environment.jersey().register(version1Info);
        
        final HistogramResourceV1 histogramResource = new HistogramResourceV1(indices);
        environment.jersey().register(histogramResource);
        
        final SizeResourceV1 sizeResource = new SizeResourceV1(indices);
        environment.jersey().register(sizeResource);
        
        final DataResourceV1 dataResource = new DataResourceV1(indices);
        environment.jersey().register(dataResource);

        // health checks
        final BasicHealthCheck bHealth = new BasicHealthCheck();
        environment.healthChecks().register("basic", bHealth);

    }

}
