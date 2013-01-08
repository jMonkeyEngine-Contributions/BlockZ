/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ;

import BlockZ.world.VoxelType;
import BlockZ.world.Voxel;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Christopher
 */
public class Fragment {

    public static final int size = 10;
    private boolean[] testNeighbours = {true, true, true, true, true, true};
    private static Voxel[][][] fragment = new Voxel[size][size][size];
    private ArrayList<Voxel> containingVoxels = new ArrayList<Voxel>();

    public Voxel getVoxel(int x, int y, int z) {
        return fragment[x][y][z];
    }

    public boolean isOccupied(int x, int y, int z) {
        if (fragment[x][y][z] == null || !fragment[x][y][z].isOpac()) {
            return false;
        }
        return true;
    }

    public void setVoxel(Voxel vox, int x, int y, int z) {
        fragment[x][y][z] = vox;
        containingVoxels.add(vox);
    }

    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public Mesh getMesh() {

        Mesh result = new Mesh();
        MeshData data = new MeshData();
        for (Voxel voxel : containingVoxels) {
            voxel.createMesh(whichNeighboursNotOccupied(voxel), data);
        }
        Vector3f[] v3 = data.getVertices().toArray(new Vector3f[data.getVertices().size()]);
        /*Vector4f[] c4 = verticesColor.toArray(new Vector4f[verticesColor.size()]);*/
        Vector3f[] n3 = data.getNormals().toArray(new Vector3f[data.getNormals().size()]);
        Vector2f[] v2 = data.getTexCoord().toArray(new Vector2f[data.getTexCoord().size()]);
        int indx[] = convertIntegers(data.getIndices());

        result.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(v3));
        result.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(n3));
        result.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(v2));
        result.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indx));
        result.updateBound();
      
        return result;
    }

    public void generate(VoxelType[][] data) {
    }

    public void generate(VoxelType type) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
//                    fragment[i][j][k] = new Voxel(new Vector3i(i, j, k),type);
                    containingVoxels.add(fragment[i][j][k]);
                   
                }
            }
        }
    }

    private boolean[] whichNeighboursNotOccupied(Voxel v) {
        boolean[] result = new boolean[6];
        Arrays.fill(result, true);
        Vector3i position = v.getPosition();
        int x = position.x;
        int y = position.y;
        int z = position.z;
        if (indexExists(size, (x + 1))) {
            if (isOccupied(x + 1, y, z)) {
                result[Voxel.xFace] = false;
            }
        }

        if (indexExists(size, x - 1)) {
            if (isOccupied(x - 1, y, z)) {
                result[Voxel.xiFace] = false;
            }
        }

        if (indexExists(size, y + 1)) {
            if (isOccupied(x, y + 1, z)) {
                result[Voxel.yFace] = false;
            }
        }


        if (indexExists(size, y - 1)) {
            if (isOccupied(x, y - 1, z)) {
                result[Voxel.yiFace] = false;
            }
        }

        if (indexExists(size, z + 1)) {
            if (isOccupied(x, y, z + 1)) {
                result[Voxel.zFace] = false;
            }
        }

        if (indexExists(size, z - 1)) {
            if (isOccupied(x, y, z - 1)) {
                result[Voxel.ziFace] = false;
            }
        }
        return result;
    }

    public void removeVoxel(int x, int y, int z){
        
        Voxel v = getVoxel(x, y, z);
        containingVoxels.remove(v);
        fragment[x][y][z]=null;
    }
    public boolean indexExists(int size, int index) {

        if (index >= 0 && index < size) {
            return true;
        }
        return false;
    }
}
