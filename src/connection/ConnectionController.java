/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import drawable.Drawable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author David
 */
@SuppressWarnings("serial")
public class ConnectionController implements ConnectionManager, Serializable {

    @Override
    public String sendScene(Drawable scene, String usuario, String operacion) {
        // Connection
        URL servletUrl;
        URLConnection servletConnection;
        String sms = null;
        try {
            // Direccion con el Uri del recurso a invocar:
            String location = "http://localhost:8080/PongUnlimitedEnterprise-war/SceneServlet";
            servletUrl = new URL(location);

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

            // (W1)Escribimos mensaje para el server de quien lo va hacer 
            bufferSalida.writeObject(usuario);

            // (W2)Escribimos mensaje para el server lo que vamos hacer
            bufferSalida.writeObject(operacion);

            // (W3)Escribimos objeto datos y los enviamos
            bufferSalida.writeObject(scene);
            bufferSalida.flush();

            // --------------------
            // Creamos el Stream de Lectura:
            InputStream instream = servletConnection.getInputStream();
            ObjectInputStream bufferentrada = new ObjectInputStream(instream);

            // Creamos la Referencia que recogera los datos:
            // (R1)leemos la respuesta y leemos el texto
            sms = (String) bufferentrada.readObject();
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
        return sms;
    }

    @Override
    public Drawable receiveScene(String usuario, String operacion) {
        // Connection
        URL servletUrl;
        URLConnection servletConnection;
        Drawable scene = null;
        try {
            // Direccion con el Uri del recurso a invocar:
            String location = "http://localhost:8080/PongUnlimitedEnterprise-war/SceneServlet";
            servletUrl = new URL(location);

            // Intentamos abrir la conexion
            servletConnection = servletUrl.openConnection();
            System.out.println("Cliente SendScene: Servlet Conectado a URL");

            // Activamos la Entrada
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            servletConnection.setUseCaches(false);
            servletConnection.setDefaultUseCaches(false);
            servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

            // ----------------------------------
            // Creamos el Stream de Salida
            OutputStream outstream = servletConnection.getOutputStream();
            ObjectOutputStream bufferSalida = new ObjectOutputStream(outstream);

            // (W1)Escribimos mensaje para el server de quien lo va hacer 
            bufferSalida.writeObject(usuario);

            // (W2)Escribimos mensaje para el server la operacion
            bufferSalida.writeObject(operacion);

            // (W3)Enviamos el mensaje
            bufferSalida.flush();
            // ----------------------------------
            // Creamos el Stream de Entrada
            InputStream instream = servletConnection.getInputStream();
            ObjectInputStream bufferentrada = new ObjectInputStream(instream);

            // (R1)Recogemos datos
            scene = (Drawable) bufferentrada.readObject();

            bufferSalida.close();
            bufferentrada.close();

        } catch (MalformedURLException e) {
            System.out.println("Catch formato url: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error IO en Applet(Recibiendo): "
                    + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error en Applet Clase no Encontrada: "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Desconocido (Recibiendo): "
                    + e.getMessage());
        }
        return scene;
    }
}
