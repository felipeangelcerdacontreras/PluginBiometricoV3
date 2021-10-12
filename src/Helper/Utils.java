/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import DB.Conexion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Ing. Angel Contreras
 */
public class Utils {

    public Utils() {
    }

    public static final boolean setKeyConfig(String urlIndexFile, String urlHabSensor, String urlRestApi, String uniqueId, String browser, String action) {
        boolean response = false;
        Conexion con = new Conexion();
        if (action.equals("add")) {
            con.createTable();
            response = con.insert(urlHabSensor, urlRestApi, uniqueId, browser);
        } else {
            response = con.updateConfig(urlHabSensor, urlRestApi, uniqueId, browser);
        }
        con = null;
        return response;
    }

    public static final String getKeyConfig(String key) throws FileNotFoundException, IOException {
        String keyVaue = "";
        Conexion con = new Conexion();
        keyVaue = con.select(key);
        con = null;
        return keyVaue;
    }

    public static final boolean isServicesAdd() throws FileNotFoundException, IOException {
        boolean response = false;
        Conexion con = new Conexion();
        response = con.isServices();
        con = null;
        return response;
    }

    public static final boolean addServiceConfig() throws FileNotFoundException, IOException {
        boolean response = false;
        Conexion con = new Conexion();
        response = con.insertService();
        con = null;
        return response;
    }

    public static void restartApplication() {
        try {
            String current = new java.io.File(".").getCanonicalPath();
            String nameapp = "PluginBiometricoV3.exe";
            File archivo = new File(current + "\\" + nameapp);
            if (!archivo.exists()) {
                nameapp = "PluginBiometricoV3.jar";
            }
            new ProcessBuilder("cmd", "/c start /min " + current + "\\" + nameapp + " ^& exit").start();
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Error reiniciando " + ex);
        }
    }
}
