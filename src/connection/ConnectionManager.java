/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import drawable.Drawable;

/**
 *
 * @author David
 */
public interface ConnectionManager {

    String sendScene(Drawable scene, String usuario, String operacion);

    Drawable receiveScene(String usuario, String operacion);
}
