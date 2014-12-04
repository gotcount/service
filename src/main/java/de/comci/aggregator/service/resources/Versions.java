/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.resources;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class Versions {
    
    @GET
    public List<String> versions() {
        return Arrays.asList("v1");
    }
    
}
