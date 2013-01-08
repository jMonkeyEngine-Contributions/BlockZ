/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.ninepatch;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;

/**
 *
 * @author Christopher
 */
public class NinePatch {

    private static final Vector2f t00 = new Vector2f(0, 0);
    private  static final Vector2f t01 = new Vector2f(FastMath.ONE_THIRD, 0);
    private static final Vector2f t02 = new Vector2f(FastMath.ONE_THIRD * 2, 0);
    private static final Vector2f t03 = new Vector2f(1.0f, 0);
    private static final Vector2f t10 = new Vector2f(0, FastMath.ONE_THIRD);
    private static final Vector2f t11 = new Vector2f(FastMath.ONE_THIRD, FastMath.ONE_THIRD);
    private static final Vector2f t12 = new Vector2f(FastMath.ONE_THIRD * 2, FastMath.ONE_THIRD);
    private static final Vector2f t13 = new Vector2f(1.0f, FastMath.ONE_THIRD);
    private static final Vector2f t20 = new Vector2f(0, FastMath.ONE_THIRD * 2);
    private static final Vector2f t21 = new Vector2f(FastMath.ONE_THIRD, FastMath.ONE_THIRD * 2);
    private static final Vector2f t22 = new Vector2f(FastMath.ONE_THIRD * 2, FastMath.ONE_THIRD * 2);
    private static final Vector2f t23 = new Vector2f(1.0f, FastMath.ONE_THIRD * 2);
    private static final Vector2f t30 = new Vector2f(0, 1.0f);
    private static final Vector2f t31 = new Vector2f(FastMath.ONE_THIRD, 1.0f);
    private static final Vector2f t32 = new Vector2f(FastMath.ONE_THIRD * 2, 1.0f);
    private static final Vector2f t33 = new Vector2f(1.0f, 1.0f);


   

    public static Vector2f[] getPatches(Patches patches) {
        Vector2f[] faces = new Vector2f[4];
        switch (patches) {
            case UpperLeft:
                faces[0] = t20;
                faces[1] = t21;
                faces[2] = t30;
                faces[3] = t31;
                break;
            case UpperMiddle:
                faces[0] = t21;
                faces[1] = t22;
                faces[2] = t31;
                faces[3] = t32;
                break;
            case UpperRight:
                faces[0] = t22;
                faces[1] = t23;
                faces[2] = t32;
                faces[3] = t33;
                break;
            case MiddleLeft:
                faces[0] = t10;
                faces[1] = t11;
                faces[2] = t20;
                faces[3] = t21;
                break;
            case MiddleMiddle:
                faces[0] = t11;
                faces[1] = t12;
                faces[2] = t21;
                faces[3] = t22;
                break;
            case MiddleRight:
                faces[0] = t12;
                faces[1] = t13;
                faces[2] = t22;
                faces[3] = t23;
                break;
            case LowerLeft:
                faces[0] = t00;
                faces[1] = t01;
                faces[2] = t10;
                faces[3] = t11;
                break;
            case LowerMiddle:
                faces[0] = t01;
                faces[1] = t02;
                faces[2] = t11;
                faces[3] = t12;
                break;
            case LowerRight:
                faces[0] = t02;
                faces[1] = t03;
                faces[2] = t12;
                faces[3] = t13;
                break;
            default:
                throw new AssertionError(patches.name());

        }
        return faces;
    }
}
