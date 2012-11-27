/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection.score;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JApplet;
import nodrawable.Ranking;
import nodrawable.Score;

/**
 *
 * @author davidsantiagobarrera
 */
public class ScoreConnection implements ScoreSaver, Serializable {

    private URLConnection servletConnection;
    private URL servletUrl;
    private final URL host;
    private final String peticion;

    public ScoreConnection(JApplet ventanaprincipal) {
        host = ventanaprincipal.getCodeBase();
        peticion = "/PongUnlimitedEnterprise-war/ScoreServlet";
    }

    @Override
    public Ranking saveScore(Score score) {
        Ranking ranking = null;
        String sms = "Ranking Recibido";
        try {
            // Direccion con el Uri del recurso a invocar:
            servletUrl = new URL("http://localhost:8080/PongUnlimitedEnterprise-war/ScoreServlet");
            //servletUrl = new URL(host, peticion);
            // Intentamos abrir la conexion
            servletConnection = servletUrl.openConnection();
            System.out.println("Cliente SendScene: Servlet Conectado a URL");

            // Activamos la Salida
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            servletConnection.setUseCaches(false);
            servletConnection.setDefaultUseCaches(false);
            servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

            // Creamos el Stream de Salida
            OutputStream outstream = servletConnection.getOutputStream();
            ObjectOutputStream bufferSalida = new ObjectOutputStream(outstream);

            // (W2)Escribimos objeto datos y los enviamos
            bufferSalida.writeObject(score);
            bufferSalida.flush();

            // --------------------
            // Creamos el Stream de Lectura:
            InputStream instream = servletConnection.getInputStream();
            ObjectInputStream bufferentrada = new ObjectInputStream(instream);

            // Creamos la Referencia que recogera los datos:
            // (R1)leemos la respuesta y leemos el texto
            ranking = (Ranking) bufferentrada.readObject();
            System.out.println("mensaje servidor " + sms);

            bufferentrada.close();
            // --------------------
            bufferSalida.close();

        } catch (MalformedURLException e) {
            System.out.println("Catch formato url" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error IO en Applet (Enviando): "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Desconocido en Applet(Enviando):"
                    + e.getMessage());
        }
        return ranking;
    }
}
