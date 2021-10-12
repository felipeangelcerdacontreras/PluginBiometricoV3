/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CrearServicioWindows.CrearServicio;
import Helper.Utils;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ing. Angel Contreras
 */
public class Start {

    public static long timestamp = (new Date().getTime() / 1000);
    public static String srn;
    public static String indexUrl;
    public static Timer timer = new Timer();

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            Properties props = new Properties();
            props.put("logoString", "M-Systems");
            AcrylLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("error confiig form");
        }

        File archivo = new File("src/DB/Config.db");
        if (!archivo.exists()) {
            ConfigForm cf = new ConfigForm();
            cf.setLocationRelativeTo(null);
            cf.setVisible(true);
            return;
        }

        if (!Utils.isServicesAdd()) {
            File f = new File("PluginBiometricoV3.exe");
            String ruta = f.getAbsolutePath();
            CrearServicio.addServicesOnWindows("PluginBiometricoV3.exe", "", ruta);
            Utils.addServiceConfig();
        }

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                HabilitarLector hbs = null;
                try {
                    srn = Utils.getKeyConfig("uniqueId");
                    System.out.println("srn " + srn);
                    hbs = new HabilitarLector();
                    timestamp = hbs.sendGet(timestamp, srn);
                } catch (AWTException | IOException e) {
                    System.out.println("Error habilitando el sensor " + e.getMessage());
                } finally {
                    hbs = null;
                }
            }
        };
        timer.schedule(tarea, 0, 1000);
        TrayClass.show();

    }
}
