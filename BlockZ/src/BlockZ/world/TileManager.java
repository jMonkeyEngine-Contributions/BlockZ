/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.world;

import com.jme3.app.Application;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import BlockZ.Axis;
import BlockZ.Vector3i;
import BlockZ.rowedtexture.Rowedtexture;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import java.security.InvalidParameterException;

/**
 *
 * @author Christopher
 */
public class TileManager {

    private static TileManager instance = new TileManager();
    private PhysicsSpace physicSpace;
    public static Application main;
    private Vector3i startPoint = new Vector3i(0, 0, 0);
    public Node tileManagerRootNode;
    private int size;
    private Tile[][][] tiles = null;
    private ArrayList<Tile> containingTiles = new ArrayList<Tile>();
    private boolean initialized = false;
    private static final Logger logger = Logger.getLogger(TileManager.class.getName());
    private Rowedtexture rtex;
    private Material blockMaterial;
    private int tilesize;
    private int autoBufferTileDistance = 2;

    public static TileManager getInstance() {
        if (!instance.initialized) {
            throw new RuntimeException("TileManager is not intialized! Call init with non-null values before calling getInstance");
        }
        return instance;
    }

    public static void init(PhysicsSpace physicSpace, Node tileManagerRootNode, Application app, Rowedtexture texture, Material blockMaterial, int tileBufferSize, int tilesize) {

        if (tilesize <= 0 || tileBufferSize <= 0) {
            throw new InvalidParameterException("tilesize and tileBufferSize have to be greater than 0");
        }
        if (physicSpace != null && tileManagerRootNode != null && app != null && texture != null && blockMaterial != null) {
            instance.physicSpace = physicSpace;
            instance.tileManagerRootNode = tileManagerRootNode;
            instance.initialized = true;
            main = app;
            instance.rtex = texture;
            instance.blockMaterial = blockMaterial;
            instance.size = tileBufferSize;
            instance.tilesize = tilesize;
            instance.tiles = new Tile[instance.size][instance.size][instance.size];
        } else {
            throw new NullPointerException("None of the parameters must not be Null!");
        }
    }

    private TileManager() {
    }

    public void addTile(Vector3i v) {
        if (indexExists(v.x - startPoint.x) && indexExists(v.y - startPoint.y) && indexExists(v.z - startPoint.z)) {
            tiles[v.x - startPoint.x][v.y - startPoint.y][v.z - startPoint.z] = new Tile(new Vector3i(v.x * tilesize, v.y, v.z * tilesize), rtex);
            Tile addedTile = getTile(v);

            containingTiles.add(addedTile);
            addedTile.updateAll();
//            addedTile.setRigid(new RigidBodyControl(0f));
//            addedTile.getGeometry().addControl(addedTile.getRigid());
//            if (addedTile.hasVisibleFaces()) {
//                physicSpace.add(addedTile.getRigid());
//
////                updateCollisionShapeOfTile(addedTile.getGeometry());
//            }
        }
        if (getTile(v) != null && tiles[v.x - startPoint.x][v.y - startPoint.y][v.z - startPoint.z] == getTile(v)) {
            tileManagerRootNode.attachChild(getTile(v).getGeometry());
        } else {
            logger.log(Level.WARNING, "could not add to tmRootNode because Tile == null");
        }
    }

    public Voxel getVoxelOnCollisionPoint(Vector3f position) {
        Vector3f temp = position.clone();

        if (isOnVoxelBorder(temp.x)) {
            temp.x = Math.round(temp.x);
            Vector3f temp2 = temp.clone();
            temp2.x = temp2.x - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                temp.x = temp.x - 1;
            }
        }
        if (isOnVoxelBorder(temp.y)) {
            temp.y = Math.round(temp.y);
            Vector3f temp2 = temp.clone();
            temp2.y = temp2.y - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                System.out.println(" moving to y-1");
                temp.y = temp.y - 1;
            }
        }
        if (isOnVoxelBorder(temp.z)) {
            temp.z = Math.round(temp.z);
            Vector3f temp2 = temp.clone();
            temp2.z = temp2.z - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                temp.z = temp.z - 1;
            }
        }
        return getVoxel(temp);
    }

    public Vector3f removeVoxel(Vector3f position) {
        Vector3f temp = position.clone();

        if (isOnVoxelBorder(temp.x)) {
            temp.x = Math.round(temp.x);
            Vector3f temp2 = temp.clone();
            temp2.x = temp2.x - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                temp.x = temp.x - 1;
            }
        }
        if (isOnVoxelBorder(temp.y)) {
            temp.y = Math.round(temp.y);
            Vector3f temp2 = temp.clone();
            temp2.y = temp2.y - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                System.out.println(" moving to y-1");
                temp.y = temp.y - 1;
            }
        }
        if (isOnVoxelBorder(temp.z)) {
            temp.z = Math.round(temp.z);
            Vector3f temp2 = temp.clone();
            temp2.z = temp2.z - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 == null && v2 != null) {
                temp.z = temp.z - 1;
            }
        }
        Vector3i v = globalPositionToTileBufferPosition(temp);
        logger.log(Level.WARNING, "Getting Tile: " + v + " for temp: " + temp);
        Tile t = getTile(v);
        Vector3i inTile = globalPositionToLocalInTilePosition(temp);
        logger.log(Level.WARNING, "inTile: " + inTile);
        if (t != null && inTile != null) {

            int x = inTile.x;
            int z = inTile.z;



            t.removeVoxel(x, z);
            t.updateAll();
            updateCollisionShapeOfTile(tiles[v.x - startPoint.x][v.y - startPoint.y][v.z - startPoint.z]);

            if ((x) == 0) {
                updateTile(new Vector3i(v.x - 1, v.y, v.z));
            }
            if ((x) == tilesize - 1) {
                updateTile(new Vector3i(v.x + 1, v.y, v.z));


            }
            if ((z) == 0) {
                updateTile(new Vector3i(v.x, v.y, v.z - 1));

            }
            if ((z) == tilesize - 1) {
                updateTile(new Vector3i(v.x, v.y, v.z + 1));

            }

            updateTile(new Vector3i(v.x, v.y + 1, v.z));


            updateTile(new Vector3i(v.x, v.y - 1, v.z));



        } else {
            logger.log(Level.WARNING, "No Tilefound");
        }

        return getCenterOfVoxel(temp);

    }

    public Vector3f getCenterOfVoxel(Vector3f v){
        Vector3f result =v.clone();
        result.x =  Math.round(result.x) + 0.5f;
        result.y =  Math.round(result.y) + 0.5f;
        result.z =  Math.round(result.z) + 0.5f;
        return result;
    }
    private void updateTile(Vector3i position) {
        Tile t = getTile(position);
        if (t != null) {
            logger.log(Level.WARNING, "updateTile: Updating Tile" + t.getPosition());
            updateGeoAndCollisionShapeOfTile(t);
        } else {
            logger.log(Level.WARNING, "updateTile: Tile Null");
        }
    }

    public void updateAllTiles() {
        for (Tile tile : containingTiles) {

            tile.updateAll();
        }
    }

    public void setRigidsforAllTiles() {
        for (Tile tile : containingTiles) {
            updateCollisionShapeOfTile(tile);

        }
    }

    public Vector3i globalPositionToTileBufferPosition(Vector3f position) {

        Vector3f temp = position.clone();

        logger.log(Level.WARNING, "position " + position);

        Vector3i result = new Vector3i((int) (temp.x / tilesize), (int) temp.y, (int) (temp.z / tilesize));
        if (position.x < 0) {
            result.x = result.x - 1;
        }
        if (position.z < 0) {
            result.z = result.z - 1;
        }
        return result;
    }

    public Vector3i globalPositionToLocalInTilePosition(Vector3f position) {
        Vector3i tileBufferPosition = globalPositionToTileBufferPosition(position);

        Tile t = getTile(tileBufferPosition);
        if (t != null) {
            Vector3i bufferWorldPosition = t.getPosition();
            return new Vector3i(position.x - bufferWorldPosition.x, position.y - bufferWorldPosition.y, position.z - bufferWorldPosition.z);
        }
        return null;
    }

    public void addVoxel(VoxelType type, Vector3f position) {
        Vector3f temp = position.clone();


        if (isOnVoxelBorder(temp.x)) {
            temp.x = Math.round(temp.x);
            Vector3f temp2 = temp.clone();
            temp2.x = temp2.x - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 != null && v2 == null) {
                temp.x = temp.x - 1;
            }
        }
        if (isOnVoxelBorder(temp.y)) {
            temp.y = Math.round(temp.y);
            Vector3f temp2 = temp.clone();
            temp2.y = temp2.y - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 != null && v2 == null) {
                temp.y = temp.y - 1;
            }
        }
        if (isOnVoxelBorder(temp.z)) {
            temp.z = Math.round(temp.z);
            Vector3f temp2 = temp.clone();
            temp2.z = temp2.z - 1;
            Voxel v1 = getVoxel(temp);
            Voxel v2 = getVoxel(temp2);

            if (v1 != null && v2 == null) {
                temp.z = temp.z - 1;
            }
        }


        Vector3i tilePosition = globalPositionToTileBufferPosition(temp);
        Tile t = getTile(tilePosition);
        logger.log(Level.WARNING, "Getting Tile for adding Voxel: " + tilePosition + " for worldCoord: " + temp);
        if (t == null) {

            addTile(tilePosition);
            t = getTile(tilePosition);
            logger.log(Level.WARNING, "Tileref: " + t);
            Vector3i inTile = globalPositionToLocalInTilePosition(temp);
            logger.log(Level.WARNING, "Addgin new Tile to Add Voxel inTile: " + inTile);
            VoxelType[][] types = new VoxelType[tilesize][tilesize];
            if (inTile != null) {
                int xCheck = inTile.x;
                int zCheck = inTile.z;
                for (int i = 0; i < types.length; i++) {
                    for (int j = 0; j < types.length; j++) {
                        if (i == xCheck && j == zCheck) {
                            types[i][j] = type;
                        } else {
                            types[i][j] = null;
                        }
                    }
                }
                t.generate(types);
                t.getGeometry().setMaterial(blockMaterial);

                updateGeoAndCollisionShapeOfTile(t);
            } else {
                logger.log(Level.WARNING, "inTile Null");
            }
        } else {
            logger.log(Level.WARNING, "Tileref: " + t);
            Vector3i inTile = globalPositionToLocalInTilePosition(temp);
            logger.log(Level.WARNING, "Adding Voxel in Tile inTile: " + inTile);
            t.addVoxel(type, inTile.x, inTile.z);
            updateGeoAndCollisionShapeOfTile(t);
        }

    }

    public boolean isOnVoxelBorder(float f) {
        float testValue = f % 1;
        if (testValue > 0.99999f || testValue < 0.0001f) {
            return true;
        }
        return false;
    }

    public Voxel getVoxel(Vector3f position) {
        Vector3i v = globalPositionToTileBufferPosition(position);
        logger.log(Level.WARNING, "Getting Tile for Voxel: " + v);
        Tile t = getTile(v);
        Voxel result = null;

        if (t != null) {
            Vector3i pos = globalPositionToLocalInTilePosition(position);
//            int x = (int) Math.round(position.x) - t.getPosition().x;
//            int z = (int) Math.round(position.z) - t.getPosition().z;
            result = t.getVoxel(pos.x, pos.z);

        }


        return result;
    }

    public Vector3i getPostion(Tile t) {
        Vector3i tilePosition = t.getPosition();
        return new Vector3i(Math.round(tilePosition.x / tilesize), tilePosition.y, Math.round(tilePosition.z / tilesize));
    }

    public Tile getTile(Vector3i v) {
        if (indexExists(v.x - startPoint.x) && indexExists(v.y - startPoint.y) && indexExists(v.z - startPoint.z)) {
            return tiles[v.x - startPoint.x][v.y - startPoint.y][v.z - startPoint.z];
        }
        return null;
    }

    public ArrayList<Tile> getAllTiles() {
        return containingTiles;
    }

    private boolean indexExists(int index) {

        if (index >= 0 && index < size) {
            return true;
        }
        return false;
    }

//    public void reload(int type, VoxelType[][] typse) {
//
//        for (int i = 1; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                for (int k = 0; k < size; k++) {
//
//                    tiles[i - 1][j][k] = tiles[i][j][k];
//                }
//            }
//        }
//    }
    public void reload(Axis type, VoxelType[][][][] newTiles) {
        if (type == Axis.x) {
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[0][l][m] != null) {

                        containingTiles.remove(tiles[0][l][m]);
                        if (tiles[0][l][m].getRigid() != null) {
                            physicSpace.remove(tiles[0][l][m].getRigid());
                        }
                        tileManagerRootNode.detachChild(tiles[0][l][m].getGeometry());
                    }
                }
            }
            for (int i = 1; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {
                        tiles[i - 1][j][k] = tiles[i][j][k];

                    }
                }
            }
            startPoint.x = startPoint.x + 1;
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(size + startPoint.x - 1, l + startPoint.y, m + startPoint.z);
                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);


                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }
                        tileManagerRootNode.attachChild(t.getGeometry());
                    }

                }
            }

            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[size - 1][l][m] != null) {
                        updateGeoAndCollisionShapeOfTile(tiles[size - 1][l][m]);
                        updateGeoAndCollisionShapeOfTile(tiles[size - 2][l][m]);
//                        .updateAll();

                    }
                }
            }
        } else if (type == Axis.xi) {
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[size - 1][l][m] != null) {

                        containingTiles.remove(tiles[size - 1][l][m]);
                        if (tiles[size - 1][l][m].getRigid() != null) {
                            physicSpace.remove(tiles[size - 1][l][m].getRigid());
                        } else {
                            logger.log(Level.WARNING, "no rigidbodycontrol");
                        }
                        tileManagerRootNode.detachChild(tiles[size - 1][l][m].getGeometry());
                    } else {
                        logger.log(Level.WARNING, "tile null");
                    }

                }
            }

            for (int i = size - 1; i > 0; i--) {

                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {

                        tiles[i][j][k] = tiles[i - 1][j][k];
                    }
                }
            }

            startPoint.x = startPoint.x - 1;
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(0 + startPoint.x, l + startPoint.y, m + startPoint.z);
                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);
                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }
                        tileManagerRootNode.attachChild(t.getGeometry());
                    }
                }
            }


            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[0][l][m] != null) {
                        updateGeoAndCollisionShapeOfTile(tiles[0][l][m]);
                        updateGeoAndCollisionShapeOfTile(tiles[1][l][m]);
                    }
                }

            }
        }
        if (type == Axis.y) {

            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][0][m] != null) {

                        containingTiles.remove(tiles[l][0][m]);
                        if (tiles[l][0][m].getRigid() != null) {
                            physicSpace.remove(tiles[l][0][m].getRigid());
                        }
                        tileManagerRootNode.detachChild(tiles[l][0][m].getGeometry());
                    }
                }
            }
            for (int i = 1; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {
                        tiles[j][i - 1][k] = tiles[j][i][k];

                    }
                }
            }
            startPoint.y = startPoint.y + 1;

            for (int l = 0; l < size; l++) {
                for (int m = 0; m < size; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(l + startPoint.x, size + startPoint.y - 1, m + startPoint.z);

                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);
                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }
                        tileManagerRootNode.attachChild(t.getGeometry());
                    }

                }
            }
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][size - 1][m] != null) {
                        updateGeoAndCollisionShapeOfTile(tiles[l][size - 1][m]);
                        updateGeoAndCollisionShapeOfTile(tiles[l][size - 2][m]);
                    }
                }
            }

        } else if (type == Axis.yi) {
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][size - 1][m] != null) {

                        containingTiles.remove(tiles[l][size - 1][m]);
                        if (tiles[l][size - 1][m].getRigid() != null) {
                            physicSpace.remove(tiles[l][size - 1][m].getRigid());
                        }
                        tileManagerRootNode.detachChild(tiles[l][size - 1][m].getGeometry());
                    }
                }
            }
//
            for (int i = size - 1; i > 0; i--) {

                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {
//                        System.out.println("Shifting Tile from: " + i + " " + j + " " + k + " to  " + (i - 1) + " " + j + " " + k + " ");
                        tiles[j][i][k] = tiles[j][i - 1][k];
                    }
                }
            }
            startPoint.y = startPoint.y - 1;
            for (int l = 0; l < size; l++) {
                for (int m = 0; m < size; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(l + startPoint.x, startPoint.y, m + startPoint.z);

                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);
                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }
                        tileManagerRootNode.attachChild(t.getGeometry());
                    }

                }
            }

            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][0][m] != null) {
                        updateGeoAndCollisionShapeOfTile(tiles[l][0][m]);
                        updateGeoAndCollisionShapeOfTile(tiles[l][1][m]);
                    }
                }
            }

        }
        if (type == Axis.z) {
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][m][0] != null) {

                        containingTiles.remove(tiles[l][m][0]);
                        if (tiles[l][m][0].getRigid() != null) {
                            physicSpace.remove(tiles[l][m][0].getRigid());
                        }
                        tileManagerRootNode.detachChild(tiles[l][m][0].getGeometry());
                    }
                }
            }
            for (int i = 1; i < tiles.length; i++) {
                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {
                        tiles[j][k][i - 1] = tiles[j][k][i];

                    }
                }
            }
            startPoint.z = startPoint.z + 1;
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(l + startPoint.x, m + startPoint.y, size + startPoint.z - 1);
                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);
                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }
                        tileManagerRootNode.attachChild(t.getGeometry());
                    }

                }
            }

            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][m][size - 1] != null) {
                        updateGeoAndCollisionShapeOfTile(tiles[l][m][size - 1]);
                        updateGeoAndCollisionShapeOfTile(tiles[l][m][size - 2]);
                    }
                }
            }
        } else if (type == Axis.zi) {
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][m][size - 1] != null) {

                        containingTiles.remove(tiles[l][m][size - 1]);
                        if (tiles[l][m][size - 1].getRigid() != null) {
                            physicSpace.remove(tiles[l][m][size - 1].getRigid());
                        } else {
                            logger.log(Level.WARNING, "no rigidbodycontrol");
                        }
                        tileManagerRootNode.detachChild(tiles[l][m][size - 1].getGeometry());
                    } else {
                        logger.log(Level.WARNING, "tile null");
                    }

                }
            }

            for (int i = size - 1; i > 0; i--) {

                for (int j = 0; j < tiles.length; j++) {
                    for (int k = 0; k < tiles.length; k++) {

                        tiles[j][k][i] = tiles[j][k][i - 1];
                    }
                }
            }

            startPoint.z = startPoint.z - 1;
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (newTiles[l][m] != null) {
                        Vector3i tilePOsition = new Vector3i(l + startPoint.x, m + startPoint.y, startPoint.z);
                        addTile(tilePOsition);
                        Tile t = getTile(tilePOsition);
                        t.generate(newTiles[l][m]);
                        t.updateAll();
                        t.getGeometry().setMaterial(blockMaterial);
                        if (t.getRigid() != null) {
                            physicSpace.add(t.getRigid());
                        }

                        tileManagerRootNode.attachChild(t.getGeometry());
                    }
                }
            }
            for (int l = 0; l < newTiles.length; l++) {
                for (int m = 0; m < newTiles.length; m++) {
                    if (tiles[l][m][0] != null) {

                        updateGeoAndCollisionShapeOfTile(tiles[l][m][0]);
                        updateGeoAndCollisionShapeOfTile(tiles[l][m][1]);

                    }
                }
            }

        }

    }

    public Vector3i getStartPoint() {
        return startPoint;
    }

    public int getSize() {
        return size;
    }

    private void updateGeoAndCollisionShapeOfTile(Tile t) {
        if (t != null) {

            t.updateAll();

            updateCollisionShapeOfTile(t);
        }

    }
    public static final ScheduledThreadPoolExecutor collisionShapeProcessor = new ScheduledThreadPoolExecutor(4);

    public static void updateCollisionShapeOfTile(final Tile t) {
        if (t.getRigid() == null) {
//            logger.log(Level.WARNING, "No rigidBody; Adding now");
            RigidBodyControl rigid = new RigidBodyControl(0);
            t.setRigid(rigid);

        }
//        if (t.hasVisibleFaces() && t.getRigid().getPhysicsSpace() == null) {
//            System.out.println("vis nospace");
//            if (t.getGeometry().getControl(RigidBodyControl.class) == null) {
//                System.out.println("geometry has no rigidBody " + t.getRigid());
//                t.getGeometry().addControl(t.getRigid());
//            }
//            instance.physicSpace.add(t.getRigid());
//
//        }
//        if (!t.hasVisibleFaces() && t.getRigid().getPhysicsSpace() != null) {
//            System.out.println("!vis space");
//            instance.physicSpace.remove(t.getRigid());
//        }
        if (t.getRigid() != null) {
            instance.physicSpace.remove(t.getRigid());
        }
//        if (t.hasVisibleFaces() && t.getRigid().getPhysicsSpace() != null) {
//            System.out.println("vis space");
//            if (t.getGeometry().getControl(RigidBodyControl.class) == null) {
//                System.out.println("geometry has no rigidBody " + t.getRigid());
//                t.getGeometry().addControl(t.getRigid());
//            }
//        }
        if (t.hasVisibleFaces()) {
            if (t.getGeometry().getControl(RigidBodyControl.class) == null) {
//                System.out.println("geometry has no rigidBody " + t.getRigid());
                t.getGeometry().addControl(t.getRigid());
            }
            RigidBodyControl rigid = new RigidBodyControl(0);
            t.setRigid(rigid);
            if (t.hasVisibleFaces()) {
                t.getGeometry().addControl(rigid);
                instance.physicSpace.add(rigid);
            }
        }
//            final CollisionShape shape = CollisionShapeFactory.createMeshShape(t.getGeometry());
//            t.getRigid().setCollisionShape(shape);

        //execute creation on external thread
//        collisionShapeProcessor.execute(new Runnable() {
//            public void run() {
//                final CollisionShape shape = CollisionShapeFactory.createMeshShape(t.getGeometry());
//                //now send this back to render thread (or to physics thread directly, but it won’t make much of a difference)
//                main.enqueue(new Callable() {
//                    public Object call() throws Exception {
//                        t.getRigid().setCollisionShape(shape);
//                        return null;
//                    }
//                });
//            }
//        });
    }

    public Vector3f centralOfBufferInWorldCoords() {
        return new Vector3f(startPoint.x * tilesize + ((tilesize * size) / 2), startPoint.y + (size / 2), startPoint.z * tilesize + ((tilesize * size) / 2));
    }

    public void updateMe(CharacterControl control, WorldProvider provider) {
        Vector3f central = centralOfBufferInWorldCoords();
        Vector3f charPos = control.getPhysicsLocation();
        float dist = charPos.distance(central);
        if ((dist / tilesize) > autoBufferTileDistance) {
            Vector3f distaces = central.subtract(control.getPhysicsLocation());
//             central = centralOfBufferInWorldCoords();
            int xD = (int) distaces.x / tilesize;
            int yD = (int) distaces.y;
            int zD = (int) distaces.z / tilesize;
            int xDp = Math.abs(xD);
            int yDp = Math.abs(yD);
            int zDp = Math.abs(zD);

            for (int i = 0; i < xDp; i++) {
                if (xD > 0) {
                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(startPoint.x + size + 1, j, k));
                        }

                    }

                    reload(Axis.xi, newTiles);
                } else {

                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(startPoint.x - 1, j, k));
                        }

                    }

                    reload(Axis.x, newTiles);
                }
            }


            for (int i = 0; i < yDp; i++) {
                if (yD > 0) {
                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(j, startPoint.y - 1, k));
                        }

                    }

                    reload(Axis.yi, newTiles);
                } else {

                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(j, startPoint.y + size + 1, k));
                        }

                    }

                    reload(Axis.y, newTiles);
                }
            }

            for (int i = 0; i < zDp; i++) {
                if (zD > 0) {
                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(j, k, startPoint.z + size + 1));
                        }

                    }

                    reload(Axis.zi, newTiles);
                } else {

                    VoxelType[][][][] newTiles = new VoxelType[size][size][][];
                    for (int j = 0; j < size; j++) {
                        for (int k = 0; k < size; k++) {
                            newTiles[j][k] = provider.getVoxelTypeForTile(new Vector3i(j, k, startPoint.z - 1));
                        }

                    }

                    reload(Axis.z, newTiles);
                }
            }

        }
    }
}
