/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author archer
 */
public class DatabaseAdapter implements ContentProvider {
    
    private Connection conn = null;
    private Statement stmt = null;
    private final String prefix = "jdbc:postgresql://";
    private final String hostname = "localhost";
    private final String port = "5432";
    private final String dbname = "notte";
    private final String username = "postgres";
    private final String password = "1234";
    private final String driver = "org.postgresql.Driver";
    
    private final String INSERTION_VALUES_GUEST = "registerId, fullname, cardnumber, country";
    private final String INSERTION_VALUES_ORDER = "checkin, checkout, totalbill, roomid, guestid";
    private final String INSERTION_VALUES_ROOM = "typeid";
    
    public DatabaseAdapter() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(prefix + hostname + ":" + port + "/" + dbname, username, password);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception has been occured: " + ex.getMessage());
        }
    }
    
    public void destructor() {
        try {
            this.conn.close();
            System.out.println("Database closed");
        } catch (SQLException ex) {
            System.out.println("Error found when closing db connection: " + ex.getMessage());
        }
    }
    
    @Override
    public ResultSet query(String table, String projections, String where) {
        try {
            String qry = "SELECT " + projections + " FROM " + table + " WHERE " + where;
            stmt = this.conn.createStatement();
            return stmt.executeQuery(qry);
        } catch (SQLException ex) {
            System.out.println("Error occured while querying data: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean insert(String table, String args) {
        String qry = null;
        String qryPrefix = "INSERT INTO " + table + "(";
        String qryPostfix = ") VALUES(" + args + ")";
        
        switch(table) {
            case "guests":
                qry = qryPrefix + INSERTION_VALUES_GUEST + qryPostfix;
                break;
            case "orders":
                qry = qryPrefix + INSERTION_VALUES_ORDER + qryPostfix;
                break;
            case "rooms":
                qry = qryPrefix + INSERTION_VALUES_ROOM + qryPostfix;
                break;
        }
        
        try {
            stmt = this.conn.createStatement();
            return (stmt.executeUpdate(qry) > 0) ? true : false;
        } catch (SQLException ex) {
            System.out.println("Exception occured during insertion operation: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(String table, String where) {
        try {
            String qry = "DELETE FROM " + table + " WHERE " + where;
            stmt = this.conn.createStatement();
            return (stmt.executeUpdate(qry) > 0) ? true : false;
        } catch (SQLException ex) {
           System.out.println("Error occured while deleting data: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(String table, String where, String set) {
        try {
            String qry = "UPDATE " + table + " SET " + set + " WHERE " + where;
            stmt = this.conn.createStatement();
            return (stmt.executeUpdate(qry) > 0) ? true : false;
        } catch (SQLException ex) {
            System.out.println("Problem occured while updating data: " + ex.getMessage());
        }
        return false;
    }
    
}
