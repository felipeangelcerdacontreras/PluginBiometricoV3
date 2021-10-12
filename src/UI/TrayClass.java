/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CrearServicioWindows.CrearServicio;
import DB.Conexion;
import Helper.Utils;
import ds.desktop.notify.DesktopNotify;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ing. Angel Contreras
 */
public class TrayClass {

    static TrayIcon trayIcon;
    static ArrayList<String> listNav = new ArrayList<>();
    static String activeNav = "";
    static String cr = "Chrome";
    static String mz = "Mozilla";
    static String ed = "Edge";
    static String exp = "Explorer";

    public static void show() throws IOException {

        if (trayIcon == null) {
            if (!SystemTray.isSupported()) {
                System.exit(0);
            }
            trayIcon = new TrayIcon(createIcon("/Imagenes/tryicon.png", "Icon"));
            trayIcon.setToolTip("Sensor Biometrico");
            final SystemTray tray = SystemTray.getSystemTray();
            final PopupMenu menu = new PopupMenu();
            addMenuBrowser(menu);
            menu.addSeparator();
            addMenuConfig(menu);
            addMenuUpdateConfig(menu);
            MenuItem Addregister = new MenuItem("Crear Inicio Automatico");
            MenuItem Deleteregister = new MenuItem("Eliminar Inicio Automatico");
            MenuItem close = new MenuItem("Cerrar");

            Addregister.addActionListener((e) -> {
                File f = new File("PluginBiometricoV3.exe");
                String ruta = f.getAbsolutePath();
                String r = CrearServicio.addServicesOnWindows("PluginBiometricoV3.exe", "", ruta);
                if (!r.equals("")) {
                    DesktopNotify.showDesktopMessage("Aviso..!", r.trim() + "\nLa aplicación ahora iniciara con el sistema operativo",
                            DesktopNotify.SUCCESS, 4000L);
                } else {
                    DesktopNotify.showDesktopMessage("Aviso..!", "La aplicación ya inicia con el sistema opoerativo..!",
                            DesktopNotify.INFORMATION, 3000L);
                }
            });

            Deleteregister.addActionListener((e) -> {
                String r = CrearServicio.removeServicesOnWindows("PluginBiometricoV3.exe", "");
                if (!r.equals("")) {
                    DesktopNotify.showDesktopMessage("Aviso..!", r.trim() + "\nLa aplicación ya no iniciará con el sistema operativo",
                            DesktopNotify.SUCCESS, 4000L);
                } else {
                    DesktopNotify.showDesktopMessage("Aviso..!", "No hay registro de auto inicio..!",
                            DesktopNotify.INFORMATION, 3000L);
                }

            });
            close.addActionListener((e) -> {
                System.exit(0);
            });
            menu.addSeparator();
            menu.add(Addregister);
            menu.add(Deleteregister);
            menu.addSeparator();
            menu.add(close);
            trayIcon.setPopupMenu(menu);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("Error " + e);
            }
        }
    }

    private static Image createIcon(String imagen, String icon) {
        Image imageIcon = new javax.swing.ImageIcon(TrayClass.class.getResource(imagen)).getImage();
        return (new ImageIcon(imageIcon, icon)).getImage();
    }

    private static void getListaNav() {
        listNav.clear();
        Conexion cn = new Conexion();
        try {
            ResultSet rs = cn.selectNavActivo();
            while (rs.next()) {
                if (rs.getString("estado").equals("activo")) {
                    activeNav = rs.getString("browser");
                }
                listNav.add(rs.getString("browser"));
            }
        } catch (SQLException ex) {
            System.out.println("erorr " + ex);
        } finally {
            cn.close();
            cn = null;
        }
    }

    private static void addMenuBrowser(PopupMenu menu) {
        geActiveNav();
        System.out.println("menu add brouser");
        Menu Browser = new Menu("Navegador");
        MenuItem Chrome = new MenuItem(cr);
        MenuItem Mozilla = new MenuItem(mz);
        MenuItem Edge = new MenuItem(ed);
        MenuItem Explorer = new MenuItem(exp);
        Chrome.addActionListener((ActionEvent e) -> {
            Chrome.setLabel("✓ Chrome");
            Mozilla.setLabel("Mozilla");
            Edge.setLabel("Edge");
            Explorer.setLabel("Explorer");
            Conexion con = new Conexion();
            con.update("Chrome");
            con = null;
            Utils.restartApplication();
        });
        Mozilla.addActionListener((ActionEvent e) -> {
            Chrome.setLabel("Chrome");
            Mozilla.setLabel("✓ Mozilla");
            Edge.setLabel("Edge");
            Explorer.setLabel("Explorer");
            Conexion con = new Conexion();
            con.update("Mozilla");
            con = null;
            Utils.restartApplication();
        });
        Edge.addActionListener((ActionEvent e) -> {
            Chrome.setLabel("Chrome");
            Mozilla.setLabel("Mozilla");
            Edge.setLabel("✓ Edge");
            Explorer.setLabel("Explorer");
            Conexion con = new Conexion();
            con.update("Edge");
            con = null;
            Utils.restartApplication();
        });
        Explorer.addActionListener((ActionEvent e) -> {
            Chrome.setLabel("Chrome");
            Mozilla.setLabel("Mozilla");
            Edge.setLabel("Edge");
            Explorer.setLabel("✓ Explorer");
            Conexion con = new Conexion();
            con.update("Explorer");
            con = null;
            Utils.restartApplication();
        });
        for (int i = 0; i < listNav.size(); i++) {
            if (listNav.get(i).equals("Chrome")) {
                Browser.add(Chrome);
            }
            if (listNav.get(i).equals("Mozilla")) {
                Browser.add(Mozilla);
            }
            if (listNav.get(i).equals("Edge")) {
                Browser.add(Edge);
            }
            if (listNav.get(i).equals("Explorer")) {
                Browser.add(Explorer);
            }
        }
        menu.add(Browser);
    }

    private static void addMenuUpdateConfig(PopupMenu menu) {
        geActiveNav();
        Menu UpdateConfig = new Menu("Actualizar Configuración");
        MenuItem ChromeUpd = new MenuItem("Chrome");
        MenuItem MozillaUpd = new MenuItem("Mozilla");
        MenuItem EdgeUpd = new MenuItem("Edge");
        MenuItem ExplorerUpd = new MenuItem("Explorer");
        ChromeUpd.addActionListener((ActionEvent e) -> {
            updateConfig("Chrome");
        });
        MozillaUpd.addActionListener((ActionEvent e) -> {
            updateConfig("Mozilla");
        });
        EdgeUpd.addActionListener((ActionEvent e) -> {
            updateConfig("Edge");
        });
        ExplorerUpd.addActionListener((ActionEvent e) -> {
            updateConfig("Explorer");
        });
        for (int i = 0; i < listNav.size(); i++) {
            if (listNav.get(i).equals("Chrome")) {
                UpdateConfig.add(ChromeUpd);
            }
            if (listNav.get(i).equals("Mozilla")) {
                UpdateConfig.add(MozillaUpd);
            }
            if (listNav.get(i).equals("Edge")) {
                UpdateConfig.add(EdgeUpd);
            }
            if (listNav.get(i).equals("Explorer")) {
                UpdateConfig.add(ExplorerUpd);
            }
        }
        menu.add(UpdateConfig);
    }

    private static void addMenuConfig(PopupMenu menu) {
        MenuItem Config = new MenuItem("Nueva Configuración");
        menu.add(Config);
        Config.addActionListener((e) -> {
            Conexion con = new Conexion();
            try {
                String urlHabSensor = "";
                String urlRestApi = "";
                String browser = "";
                ResultSet rs2 = con.currentConfig("");
                ArrayList<String> listaBr = new ArrayList<>();
                while (rs2.next()) {
                    if (rs2.getString("estado").equals("activo")) {
                        urlHabSensor = rs2.getString("urlHabSensor");
                        urlRestApi = rs2.getString("urlRestApi");
                        browser = rs2.getString("browser");
                    }
                    listaBr.add(rs2.getString("browser"));
                }
                ConfigForm cf = new ConfigForm();
                cf.setLocationRelativeTo(null);
                cf.setVisible(true);
                cf.txtHblSensor.setText(urlHabSensor);
                cf.txtRestApi.setText(urlRestApi);
                cf.cboBrowser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Seleccione", "Chrome", "Mozilla", "Edge", "Explorer"}));
                for (String string : listaBr) {
                    cf.cboBrowser.removeItem(string);
                }
                cf = null;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            } finally {
                con.close();
                con = null;
            }
        });
    }

    private static void geActiveNav() {
        getListaNav();
        switch (activeNav) {
            case "Chrome":
                cr = "✓ Chrome";
                break;
            case "Mozilla":
                mz = "✓ Mozilla";
                break;
            case "Edge":
                ed = "✓ Edge";
                break;
            case "Explorer":
                exp = "✓ Explorer";
                break;
        }
    }

    private static void updateConfig(String browser) {
        Conexion con = new Conexion();
        ConfigForm cf = new ConfigForm();
        try {
            String urlHabSensor = "";
            String urlRestApi = "";
            ResultSet rs2 = con.currentConfig("limit 1");
            while (rs2.next()) {
                urlHabSensor = rs2.getString("urlHabSensor");
                urlRestApi = rs2.getString("urlRestApi");
            }
            cf.setLocationRelativeTo(null);
            cf.setVisible(true);
            cf.txtHblSensor.setText(urlHabSensor);
            cf.txtRestApi.setText(urlRestApi);
            cf.cboBrowser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{browser}));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            con.close();
            con = null;
            cf = null;
        }
    }

}
