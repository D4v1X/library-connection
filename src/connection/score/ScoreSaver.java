/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection.score;

import nodrawable.Ranking;
import nodrawable.Score;

/**
 *
 * @author davidsantiagobarrera
 */
public interface ScoreSaver {

    public Ranking saveScore(Score score);
}
