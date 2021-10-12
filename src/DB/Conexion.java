/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Ing. Angel Contreras
 */
public class Conexion {

    String url = "src/DB/Config.db";
    Connection connect;
    Statement stmt = null;
    ResultSet rs;

    public Conexion() {
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createTable() {
        try {
            connect();
            stmt = connect.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CONFIG "
                    + "(uniqueId CHAR(100) PRIMARY KEY     NOT NULL,"
                    + " urlHabSensor           TEXT    NOT NULL, "
                    + " urlRestApi            TEXT     NOT NULL, "
                    + " browser        CHAR(100), "
                    + " estado         CHAR(20))";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.close();
            System.out.println("Tabla creada");
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
    }

    public boolean insert(String urlHabSensor, String urlRestApi, String uniqueId, String browser) {
        boolean response = false;
        try {
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            String sql = "INSERT INTO CONFIG (uniqueId,urlHabSensor,urlRestApi,browser,estado) "
                    + "VALUES ('" + uniqueId + "', '" + urlHabSensor + "', '" + urlRestApi + "', '" + browser + "', 'activo' );";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.commit();
            connect.close();
            update(browser);
            System.out.println("Records created successfully");
            response = true;
        } catch (SQLException ex) {
            System.out.println("Error al crear el registrro Insert " + ex);
        }
        return response;
    }

    public boolean updateConfig(String urlHabSensor, String urlRestApi,String uniqueId, String browser) {
        boolean response = false;
        try {
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            String sql = "UPDATE CONFIG set urlHabSensor = '" + urlHabSensor + "', urlRestApi = '" + urlRestApi + "', uniqueId = '" + uniqueId + "' where browser ='" + browser + "';";
            JOptionPane.showMessageDialog(null, sql);
            stmt.executeUpdate(sql);
            stmt.close();
            connect.commit();
            connect.close();
            System.out.println("Records created successfully");
            response = true;
        } catch (SQLException ex) {
            System.out.println("Error al crear el registro Update" + ex.getMessage());
        }
        return response;
    }

    public String select(String field) {
        String fieldReturn = "";
        try {
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT " + field + " FROM CONFIG where estado = 'activo';");
            while (rs.next()) {
                fieldReturn = rs.getString(field);
            }
            rs.close();
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return fieldReturn;
    }

    public ResultSet currentConfig(String limit) {
        try {
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CONFIG " + limit);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return rs;
    }

    public boolean update(String browser) {
        boolean response = false;
        try {
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            String sql = "UPDATE CONFIG set estado = 'inactivo' where browser not in ('" + browser + "');";
            stmt.executeUpdate(sql);
            sql = "UPDATE CONFIG set estado = 'activo' where browser = '" + browser + "';";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.commit();
            connect.close();
            System.out.println("Records created successfully");
            response = true;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar  el registro " + ex);
        }
        return response;
    }

    public boolean delete(String browser) {
        boolean response = false;
        try {
            connect();
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            String sql = "DELETE FROM CONFIG where browser not in ('" + browser + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.commit();
            connect.close();
            response = true;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar  el registro " + ex);
        }
        return response;
    }

    public ResultSet selectNavActivo() {
        ResultSet result = null;
        try {
            connect();
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            result = stmt.executeQuery("SELECT * FROM CONFIG;");
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            connect.close();
        } catch (SQLException ex) {
            System.out.println("error " + ex);
        }
    }

    public void createTableServicio() {
        try {
            connect();
            stmt = connect.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SERVICE "
                    + "(id CHAR(1) PRIMARY KEY  NOT NULL,"
                    + " estado  CHAR(20))";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.close();
            System.out.println("Tabla creada");
        } catch (SQLException ex) {
            System.out.println("Error " + ex);
        }
    }

    public boolean insertService() {
        boolean response = false;
        try {
            createTableServicio();
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            String sql = "INSERT INTO SERVICE (id,estado) "
                    + "VALUES ('1', 'OK');";
            stmt.executeUpdate(sql);
            stmt.close();
            connect.commit();
            connect.close();
            System.out.println("Records created successfully");
            response = true;
        } catch (SQLException ex) {
            System.out.println("Error al crear el registrro Insert " + ex);
        }
        return response;
    }

    public boolean isServices() {
        boolean response = false;
        try {
            createTableServicio();
            connect();
            connect.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM SERVICE where id = '1';");
            while (rs.next()) {
                response = true;
            }
            rs.close();
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return response;
    }
}
