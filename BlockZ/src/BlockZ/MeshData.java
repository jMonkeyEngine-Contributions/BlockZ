/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author MichealZuegg
 */
public class MeshData {

    ArrayList<Vector3f> vertices;
    ArrayList<Vector3f> normals;
    ArrayList<Vector2f> texCoord;
    ArrayList<Integer> indices;

    public MeshData() {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        texCoord = new ArrayList<Vector2f>();
        indices = new ArrayList<Integer>();
    }

    public void addVertex(Vector3f v) {
        this.vertices.add(v);
    }

    public void addNormal(Vector3f v) {
        this.normals.add(v);
    }

    public void addTexCoord(Vector2f v) {
        this.texCoord.add(v);
    }

    public ArrayList<Vector3f> getNormals() {
        return normals;
    }

    public ArrayList<Vector2f> getTexCoord() {
        return texCoord;
    }

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Integer> getIndices() {
        return indices;
    }
    
    public boolean dataAvailable(){
        if(!vertices.isEmpty()){
            return true;
        }
        if(!indices.isEmpty()){
            return true;
        }      
        if(!texCoord.isEmpty()){
            return true;
        }
        if(!normals.isEmpty()){
            return true;
        }
        return false;
    }
    
}
