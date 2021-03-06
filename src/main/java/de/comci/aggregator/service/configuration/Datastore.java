/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.comci.aggregator.service.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Sebastian Maier (sebastian.maier@comci.de)
 */
public class Datastore {

    @NotNull
    @NotEmpty
    @JsonProperty
    private String jdbc;

    @NotNull
    @NotEmpty
    @JsonProperty
    private String user;

    @JsonProperty
    private String pwd = "";

    @NotNull
    @NotEmpty
    @JsonProperty
    private String table;

    @JsonProperty
    private String tableAlias;

    @JsonProperty
    private List<Column> columns;

    public Datastore() {
        // jackson        
    }

    public String getJdbc() {
        return jdbc;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getTable() {
        return table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public String getTableLabel() {
        return (tableAlias != null && !tableAlias.isEmpty()) ? tableAlias : table;
    }

}
