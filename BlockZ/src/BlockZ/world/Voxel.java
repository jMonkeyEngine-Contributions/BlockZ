/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.world;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import BlockZ.MeshData;
import BlockZ.Vector3i;
import BlockZ.ninepatch.NinePatch;
import BlockZ.ninepatch.Patches;
import BlockZ.rowedtexture.Rowedtexture;

/**
 *
 * @author Christopher
 */
public class Voxel {

    private boolean opac = true;
    private static final float blocksize = 1; // block size
//    float halfblocksize = blocksize / 2;
    public final static int xFace = 0;
    public final static int xiFace = 1;
    public final static int yFace = 2;
    public final static int yiFace = 3;
    public final static int zFace = 4;
    public final static int ziFace = 5;
    private Vector3i position;
    private final static Vector3f yAxis = Vector3f.UNIT_Y;
    private final static Vector3f yAxisI = new Vector3f(0, -1, 0);
    private final static Vector3f xAxis = Vector3f.UNIT_X;
    private final static Vector3f xAxisI = new Vector3f(-1, 0, 0);
    private final static Vector3f zAxis = Vector3f.UNIT_Z;
    private final static Vector3f zAxisI = new Vector3f(0, 0, -1);
    private VoxelType type;
    private Rowedtexture tex;

    public Voxel(Vector3i position, VoxelType type, Rowedtexture tex) {
        this.position = position;
        this.type = type;
        this.tex = tex;
    }

    public void createMesh(boolean[] faces, MeshData data) {
   
            
            ArrayList<Vector3f> vertices = data.getVertices();
            ArrayList<Vector3f> normals = data.getNormals();
            ArrayList<Vector2f> texCoord = data.getTexCoord(); // tex cords
            ArrayList<Integer> indexes = data.getIndices(); // indexes

            int verticesSize = vertices.size();

            float bx, by, bz;
            bx = position.x * blocksize;
            by = position.y * blocksize;
            bz = position.z * blocksize;




            Vector3f pa = new Vector3f(bx, by, bz + blocksize);
            Vector3f pb = new Vector3f(bx + blocksize, by, bz + blocksize);
            Vector3f pc = new Vector3f(bx, by + blocksize, bz + blocksize);
            Vector3f pd = new Vector3f(bx + blocksize, by + blocksize, bz + blocksize);

            Vector3f pe = new Vector3f(bx, by, bz);
            Vector3f pf = new Vector3f(bx + blocksize, by, bz);
            Vector3f pg = new Vector3f(bx, by + blocksize, bz);
            Vector3f ph = new Vector3f(bx + blocksize, by + blocksize, bz);


            // x = +
            if (faces[xFace]) {
                vertices.add(pb);
                vertices.add(pf);
                vertices.add(pd);
                vertices.add(ph);
                normals.add(xAxis);
                normals.add(xAxis);
                normals.add(xAxis);
                normals.add(xAxis);


                //7
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(), 0);
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);

                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }

            // x = -
            if (faces[xiFace]) {
                verticesSize = vertices.size();

                vertices.add(pe);
                vertices.add(pa);
                vertices.add(pg);
                vertices.add(pc);
                normals.add(xAxisI);
                normals.add(xAxisI);
                normals.add(xAxisI);
                normals.add(xAxisI);
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(), 2);
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);
                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }
            //y = +
            if (faces[yFace]) {
                verticesSize = vertices.size();

                vertices.add(pc);
                vertices.add(pd);
                vertices.add(pg);
                vertices.add(ph);
                normals.add(yAxis);
                normals.add(yAxis);
                normals.add(yAxis);
                normals.add(yAxis);
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(), 4);
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);
                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }
            //y = -
            if (faces[yiFace]) {
                verticesSize = vertices.size();

                vertices.add(pe);
                vertices.add(pf);
                vertices.add(pa);
                vertices.add(pb);
                normals.add(yAxisI);
                normals.add(yAxisI);
                normals.add(yAxisI);
                normals.add(yAxisI);
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(), 5);
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);
                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }

            if (faces[zFace]) {
                verticesSize = vertices.size();

                vertices.add(pa);
                vertices.add(pb);
                vertices.add(pc);
                vertices.add(pd);
                normals.add(zAxis);
                normals.add(zAxis);
                normals.add(zAxis);
                normals.add(zAxis);
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(),1 );
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);
                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }

            if (faces[ziFace]) {
                verticesSize = vertices.size();

                vertices.add(pf);
                vertices.add(pe);
                vertices.add(ph);
                vertices.add(pg);
                normals.add(zAxisI);
                normals.add(zAxisI);
                normals.add(zAxisI);
                normals.add(zAxisI);
                Vector2f[] cords = tex.getTextureCoordinates(type.getTexturetype(), 3);
                texCoord.add(cords[0]);
                texCoord.add(cords[1]);
                texCoord.add(cords[2]);
                texCoord.add(cords[3]);
                indexes.add(verticesSize + 2);
                indexes.add(verticesSize + 0);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 1);
                indexes.add(verticesSize + 3);
                indexes.add(verticesSize + 2);


            }
        

    }

    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public boolean isOpac() {
        return opac;
    }

    public void setOpac(boolean opac) {
        this.opac = opac;
    }

    public Vector3i getPosition() {
        return position;
    }

    public VoxelType getType() {
        return type;
    }
    
}
