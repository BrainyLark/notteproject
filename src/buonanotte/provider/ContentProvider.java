/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.provider;

import java.sql.ResultSet;

/**
 *
 * @author archer
 */
public interface ContentProvider {

    public ResultSet query(String table, String projections, String where);

    public boolean insert(String table, String args);

    public boolean delete(String table, String where);

    public boolean update(String table, String where, String set);

}
