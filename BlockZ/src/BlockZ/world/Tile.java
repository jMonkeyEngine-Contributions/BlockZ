/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.world;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import BlockZ.MeshData;
import BlockZ.Vector3i;
import BlockZ.rowedtexture.Rowedtexture;
import com.jme3.renderer.queue.RenderQueue.Bucket;

/**
 *
 * @author Christopher
 */
public class Tile {

    private static final int size = 16;
    private Vector3i position;
    private boolean[] testNeighbours = {true, true, true, true, true, true};
    private Voxel[][] tile = new Voxel[size][size];
    private ArrayList<Voxel> containingVoxels = new ArrayList<Voxel>();
    private Geometry geometry;
    private RigidBodyControl rigid;
    private boolean hasVisibleFaces;
    private boolean firstUpdate = true;
    private Rowedtexture rtex;
    private static final Logger logger = Logger.getLogger(TileManager.class.getName());

    public Tile(Vector3i position, Rowedtexture ext) {
        geometry = new Geometry("Tile " + position);
        geometry.setQueueBucket(Bucket.Transparent);
        this.position = position;
//        this.rigid = new RigidBodyControl(0.0f);
        rtex = ext;

    }

    public Voxel getVoxel(int x, int z) {
        if (indexExists(x) && indexExists(z)) {
            return tile[x][z];
        }
        return null;
    }

    public boolean isOccupied(int x, int z) {
//        System.err.println("occ: " + x + " " + z);
        if (indexExists(x) && indexExists(z)) {
            if (tile[x][z] == null || !tile[x][z].isOpac()) {
                return false;
            }
        }
        return true;
    }

    public void addVoxel(VoxelType vox, int x, int z) {

        if (indexExists(x) && indexExists(z)) {
            Voxel v = new Voxel(new Vector3i(position.x + x, position.y, position.z + z), vox, rtex);
            tile[x][z] = v;
            containingVoxels.add(v);
        }
    }

    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    private Mesh getMesh() {

        Mesh result = new Mesh();
        MeshData data = new MeshData();
        for (Voxel voxel : containingVoxels) {
            voxel.createMesh(whichNeighboursNotOccupied(voxel), data);
        }
        hasVisibleFaces = data.dataAvailable();
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

    private void updateGeometry() {
        this.geometry.setMesh(getMesh());
    }

    public void updateAll() {
        updateGeometry();
//        if (hasVisibleFaces) {
//            if (firstUpdate) {
//                rigid = new RigidBodyControl(0);
//                geometry.addControl(rigid);
//                firstUpdate = false;
//            }
//            this.geometry.addControl(rigid);
//        } else {
//            logger.log(Level.FINE, "updateAll: no visibleFaces");
//        }
    }

//    private void updateRigid() {
//        geometry.removeControl(rigid);
//        rigid = new RigidBodyControl(0);
//        geometry.addControl(rigid);
//    }

    public void generate(VoxelType[][] data) {

        for (int i = 0; i < size; i++) {

            for (int k = 0; k < size; k++) {
                if (data[i][k] != null) {
                    tile[i][k] = new Voxel(new Vector3i(position.x + i, position.y, position.z + k), data[i][k], rtex);
                    containingVoxels.add(tile[i][k]);
                }
            }

        }

    }

    public void generate(VoxelType type) {
        for (int i = 0; i < size; i++) {

            for (int k = 0; k < size; k++) {
                tile[i][k] = new Voxel(new Vector3i(position.x + i, position.y, position.z + k), type, rtex);
                containingVoxels.add(tile[i][k]);

            }

        }
    }

    private boolean[] whichNeighboursNotOccupied(Voxel v) {
        boolean[] result = new boolean[6];
        Arrays.fill(result, true);
        Vector3i voxelPosition = v.getPosition();
        int x = voxelPosition.x - position.x;
        int y = voxelPosition.y - position.y;
        int z = voxelPosition.z - position.z;
        TileManager tm = TileManager.getInstance();
        Vector3i tmPosition = tm.getPostion(this);
        if (indexExists((x + 1))) {
            if (isOccupied(x + 1, z)) {
                result[Voxel.xFace] = false;
            }
        } else {
            Tile tx = tm.getTile(new Vector3i(tmPosition.x + 1, tmPosition.y, tmPosition.z));
            if (tx != null) {
                result[Voxel.xFace] = !tx.isOccupied(0, z);
            }
        }

        if (indexExists(x - 1)) {
            if (isOccupied(x - 1, z)) {
                result[Voxel.xiFace] = false;
            }
        } else {
            Tile txi = tm.getTile(new Vector3i(tmPosition.x - 1, tmPosition.y, tmPosition.z));
            if (txi != null) {
                result[Voxel.xiFace] = !txi.isOccupied(Tile.size - 1, z);
            }
        }


        Tile ty = tm.getTile(new Vector3i(tmPosition.x, tmPosition.y + 1, tmPosition.z));
        if (ty != null) {
            result[Voxel.yFace] = !ty.isOccupied(x, z);
        }

        tmPosition = tm.getPostion(this);

        Tile tyi = tm.getTile(new Vector3i(tmPosition.x, tmPosition.y - 1, tmPosition.z));
        if (tyi != null) {
            result[Voxel.yiFace] = !tyi.isOccupied(x, z);
        }


        if (indexExists(z + 1)) {
            if (isOccupied(x, z + 1)) {
                result[Voxel.zFace] = false;
            }
        } else {
            Tile tz = tm.getTile(new Vector3i(tmPosition.x, tmPosition.y, tmPosition.z + 1));
            if (tz != null) {
                result[Voxel.zFace] = !tz.isOccupied(x, 0);
            }
        }

        if (indexExists(z - 1)) {
            if (isOccupied(x, z - 1)) {
                result[Voxel.ziFace] = false;
            }
        } else {
            Tile tzi = tm.getTile(new Vector3i(tmPosition.x, tmPosition.y, tmPosition.z - 1));
            if (tzi != null) {
                result[Voxel.ziFace] = !tzi.isOccupied(x, size - 1);
            }
        }
        return result;
    }

    public void removeVoxel(int x, int z) {


        Voxel v = getVoxel(x, z);
        if (v != null) {
            containingVoxels.remove(v);
            tile[x][z] = null;
        } else {
            logger.log(Level.FINE, "no Voxol at " + x + " " + z);
        }


    }

    public boolean indexExists(int index) {

        if (index >= 0 && index < size) {
            return true;
        }
        return false;
    }

    public Vector3i getPosition() {
        return position;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public RigidBodyControl getRigid() {
        return rigid;
    }

    public void setRigid(RigidBodyControl rigid) {
        this.rigid = rigid;
//        this.geometry.addControl(rigid);
    }

    public boolean hasVisibleFaces() {
        return hasVisibleFaces;
    }

    public static int getSize() {
        return size;
    }
    
    
    
}
