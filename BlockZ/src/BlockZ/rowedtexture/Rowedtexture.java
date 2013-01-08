/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.rowedtexture;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import BlockZ.Vector3i;

/**
 *
 * @author Christopher
 */
public class Rowedtexture {


    private int rows;
    private final float ONE_SIXTH = 1f/6f;

    public Rowedtexture(int rows) {
        this.rows = rows;
    }  
    
    public Vector2f[] getTextureCoordinates(int row, int column) {
        Vector2f[] result = new Vector2f[4];
        float rowth = (1f/rows);
        result[0] = new Vector2f(column *ONE_SIXTH, ((float)rows- (float)(row+1)) * rowth );
        result[1] = new Vector2f(ONE_SIXTH * (float)(column+1), ((float)rows-(float)(row+1)) * rowth);
        result[2] = new Vector2f(column*ONE_SIXTH , (rows-(row)) * rowth);
        result[3] = new Vector2f(ONE_SIXTH * (float)(column+1), (rows-(row)) * rowth);
        return result;
    }
}
