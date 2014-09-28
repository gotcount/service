/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class BasicHealthCheck extends HealthCheck {

    public BasicHealthCheck() {
    }

    @Override
    protected Result check() throws Exception {
        if (1 == 0) {
            return Result.unhealthy("1 == 0");
        }
        return Result.healthy();
    }
    
}
