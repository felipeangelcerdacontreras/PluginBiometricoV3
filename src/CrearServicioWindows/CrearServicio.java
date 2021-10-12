/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CrearServicioWindows;

import Windows.CMD;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Ing. Angel Contreras
 */
public class CrearServicio {

    public static String comando_add = "REG ADD HKEY_CURRENT_USER";
    public static String comando_remove = "REG DELETE HKEY_CURRENT_USER";

    public static String addServicesOnWindows(String nameService, String SO, String rutaFile) {
        String commandRegister = "";
        String response = "";
        switch (SO) {
            case "XP":
                commandRegister = "\\Software\\" + nameService + " /v " + nameService + " /t REG_SZ /d";
                break;
            default:
                commandRegister = "\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v " + nameService + " /t REG_SZ /d";
                break;
        }
        Runtime cmd = Runtime.getRuntime();
        try {
            String comando = comando_add + commandRegister + " " + "\"" + rutaFile + "\" /f";
            Process proceso = cmd.exec(comando);
            BufferedReader read = new BufferedReader(new InputStreamReader(proceso.getInputStream(), CMD.Detectar_Windows()));
            String linea;
            while ((linea = read.readLine()) != null) {
                response += linea + "\n";
            }
        } catch (IOException ex) {
            response = ex.getLocalizedMessage();
        }
        return response;
    }

    public static String removeServicesOnWindows(String nameService, String SO) {
        String commandRemove = "";
        String response = "";
        switch (SO) {
            case "XP":
                commandRemove = "\\Software\\Pepsi /v " + nameService + " /f";
                break;
            default:
                commandRemove = "\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v " + nameService + " /f";
                break;
        }
        Runtime cmd = Runtime.getRuntime();
        try {
            String comando = comando_remove + commandRemove;
            Process proceso = cmd.exec(comando);
            BufferedReader read = new BufferedReader(new InputStreamReader(proceso.getInputStream(), CMD.Detectar_Windows()));
            String linea;
            while ((linea = read.readLine()) != null) {
                response += linea + "\n";
            }
        } catch (IOException ex) {
            response = ex.getLocalizedMessage();
        }
        return response;
    }
}
