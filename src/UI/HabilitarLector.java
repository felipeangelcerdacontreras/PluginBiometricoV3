/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import FIngerUtils.CapturarHuella;
import FIngerUtils.GetCapturarHuella;
import FIngerUtils.GetLecturaHuella;
import FIngerUtils.LecturaHuella;
import Helper.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 *
 * @author Ing. Angel Contreras
 */
public class HabilitarLector {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static String SERVER_PATH;
    private static CapturarHuella frmCapturaHuella;
    private static LecturaHuella frmLecturaHuella;

    public HabilitarLector() {
        try {
            SERVER_PATH = Utils.getKeyConfig("urlHabSensor");
        } catch (IOException e) {
            System.out.println("error " + e);
        }

    }

    public long sendGet(long d, String srn) throws UnsupportedEncodingException, MalformedURLException, IOException, AWTException {
        long timestamp = d;
        System.out.println("d = " + d);
        StringBuilder stringBuilder = new StringBuilder(SERVER_PATH);
        stringBuilder.append("?timestamp=");
        stringBuilder.append(URLEncoder.encode("" + d, "UTF-8"));
        stringBuilder.append("&token=").append(srn);
        stringBuilder.append("&_=").append(System.currentTimeMillis());
        System.out.println(stringBuilder.toString());

        URL obj = new URL(stringBuilder.toString());

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Charset", "UTF-8");
        System.out.println("Response Message Maurcio : " + con.getResponseMessage());
        System.out.println(con.getResponseCode()+" code:");
        int responseCode = con.getResponseCode();

        StringBuilder respuesta;

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        respuesta = new StringBuilder();
        while ((line = in.readLine()) != null) {
            respuesta.append(line);
            System.out.println(respuesta+" Respuesta:");
        }
        con.disconnect();

        if (responseCode == 200) {
            System.err.println("Date = " + new Date());
            JsonParser parser = new JsonParser();
            JsonObject objJson = parser.parse(respuesta.toString()).getAsJsonObject();
            timestamp = objJson.get("fecha_creacion").getAsLong();

            if (objJson.get("opc").getAsString().equals("capturar")) {
                frmLecturaHuella = GetLecturaHuella.getLecturarHuella();
                frmLecturaHuella.stop();
                frmLecturaHuella.dispose();
                frmLecturaHuella = null;
//                if (frmCapturaHuella == null) {
                frmCapturaHuella = GetCapturarHuella.getCapturarHuella();
                frmCapturaHuella.stop();
                frmCapturaHuella.Iniciar();
                frmCapturaHuella.start();
//                }
            }

            if (objJson.get("opc").getAsString().equals("leer")) {
                System.out.println("leyendo huella");
                frmCapturaHuella = GetCapturarHuella.getCapturarHuella();
                frmCapturaHuella.stop();
                frmCapturaHuella.dispose();
                frmCapturaHuella = null;
//                if (frmLecturaHuella == null) {
                frmLecturaHuella = GetLecturaHuella.getLecturarHuella();
                frmLecturaHuella.stop();
                frmLecturaHuella.Iniciar();
                frmLecturaHuella.start();
//                }
            }
        }
        return timestamp;

    }

}
