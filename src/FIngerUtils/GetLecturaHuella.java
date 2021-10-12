/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FIngerUtils;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

/**
 *
 * @author Ing. Angel Contreras
 */
public class GetLecturaHuella {

    public static LecturaHuella lh;
    public static int BARRA_DE_ESTADO = 40;
    public static  Robot r;

    public static LecturaHuella getLecturarHuella() throws AWTException {
        if (r == null) {
            r = new Robot();
        }
        if (lh == null) {
            try {
                lh = new LecturaHuella();
                int sizeX = lh.getWidth() + 4 ;
                int sizeY = lh.getHeight();
                int maxSizeX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                int maxSizeY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                lh.setLocation(maxSizeX - sizeX, maxSizeY - sizeY - BARRA_DE_ESTADO);
                lh.setAlwaysOnTop(true);
                lh.requestFocus();
                r.mouseMove(maxSizeX + 230 - sizeX, maxSizeY - sizeY + 10);
                r.mousePress(InputEvent.BUTTON1_MASK);
                r.mouseRelease(InputEvent.BUTTON1_MASK);
                lh.getCursor();
                lh.setVisible(true);
//                lh.setOpacity(0.03f);
            } catch (HeadlessException | SecurityException e) {
                System.out.println("Error " + e.getMessage());
            }
        } else {            
            if (!lh.isVisible()) {
                lh.setVisible(true);
            }
            int tamX = lh.getWidth();
            int tamy = lh.getHeight();
            int maxX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            int maxY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            r.mouseMove(maxX + 250 - tamX, maxY - tamy + 10);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        return lh;
    }

    public static void setLecturarHuella() {
        lh = null;
    }

}
