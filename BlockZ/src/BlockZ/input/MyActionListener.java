/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.input;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import BlockZ.Axis;
import BlockZ.Main;
import BlockZ.world.Tile;
import BlockZ.world.TileManager;
import BlockZ.Vector3i;
import BlockZ.world.VoxelType;


/**
 *
 * @author Christopher
 */
public class MyActionListener implements ActionListener {

    private CharackterControlHelper charHelper;
    private PhysicsSpace space;
    private Node items;
    private Node shootables;
    private Node root;
    private Camera cam;
    private Geometry mark;
    private InputManager inputManager;

    public MyActionListener(CharackterControlHelper charHelper, PhysicsSpace space, Node shootables, Node root, Camera cam, InputManager inputManager, AssetManager assetmanager) {
        this.charHelper = charHelper;
        this.space = space;
        this.items = items;
        this.shootables = shootables;
        this.root = root;
        this.cam = cam;
        this.inputManager = inputManager;

        initMark(assetmanager);
    }

    /**
     * A red ball that marks the last spot that was "hit" by the "shot".
     */
    private final void initMark(AssetManager assetManager) {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    private VoxelType[][][][] createPagingBlock() {
        System.out.println("Page!");
        int tBufferSize= TileManager.getInstance().getSize();
        VoxelType[][][][] tiles = new VoxelType[tBufferSize][tBufferSize][Tile.getSize()][Tile.getSize()];
        for (int i = 0; i < tBufferSize; i++) {
            for (int l = 0; l < tBufferSize; l++) {
                for (int j = 0; j < Tile.getSize(); ++j) {
                    for (int k = 0; k < Tile.getSize(); ++k) {
                        tiles[i][l][j][k] = new VoxelType(FastMath.nextRandomInt(0, 3));
                    }
                }



                
 
 
           }
        }
        return tiles;
    }

    public void onAction(String binding, boolean value, float tpf) {
 
        TileManager tm = TileManager.getInstance();
        if (binding.equals("pagetilesx") && !value) {

            tm.reload(Axis.x, createPagingBlock());

        }

        if (binding.equals("pagetilesxi") && !value) {

            tm.reload(Axis.xi, createPagingBlock());

        }
        if (binding.equals("pagetilesy") && !value) {

            tm.reload(Axis.y, createPagingBlock());
        }

        if (binding.equals("pagetilesyi") && !value) {

            tm.reload(Axis.yi, createPagingBlock());

        }


        if (binding.equals("pagetilesz") && !value) {

            tm.reload(Axis.z, createPagingBlock());
        }

        if (binding.equals("pagetileszi") && !value) {

            tm.reload(Axis.zi, createPagingBlock());

        }
//        if (binding.equals("ItemRain") && !value) {
//            CollisionResults results = new CollisionResults();
//            Vector2f click2d = inputManager.getCursorPosition();
//
//            Vector3f click3d = cam.getWorldCoordinates(
//                    new Vector2f(click2d.x, click2d.y), 0f).clone();
//            Vector3f dir = cam.getWorldCoordinates(
//                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
//            System.out.println(click2d + "  " + click3d + "  " + dir);
//            Ray ray = new Ray(click3d, dir);
//            shootables.collideWith(ray, results);
//
//            if (results.size() > 0) {
//                CollisionResult closest = results.getClosestCollision();
//                Vector3f c = closest.getContactPoint();
//                c.y = c.y + 5;
//
//
//                for (int i = 0; i < 10; i++) {
//                    InWorldItem item = new InWorldItem(c, 0);
//                    space.add(item.getRigid());
//                    space.add(item.getPickUpGhost());
//                    items.attachChild(item.getGeo());
//                }
//            }
//        }

        if (binding.equals("Add") && !value) {
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();

            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            System.out.println(click2d + "  " + click3d + "  " + dir);
            Ray ray = new Ray(click3d, dir);
            shootables.collideWith(ray, results);

            if (results.size() > 0) {
                System.out.println("Adding");
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                Vector3f c = closest.getContactPoint();

//                    Voxel v = tm.getVoxel(c);
                System.out.println("Collision: " + c);
//                    if (v != null) {

//                    rootNode.detachChild(tm.getTileFromGlobalCoords(c).getGeometry());
                tm.addVoxel(new VoxelType(FastMath.nextRandomInt(0, 3)), c);

//                    rootNode.attachChild(t.getGeometry());

//                        System.out.println(" Voxel: " + v.getPosition());
//                    }

//                    tile.removeVoxel((int) c.x, (int) c.z);
//                    bulletAppState.getPhysicsSpace().remove(rigid);
//                    geo.setMesh(tile.getMesh());
//                    geo.removeControl(rigid);
//                    rigid = new RigidBodyControl(0.0f);
//                    geo.addControl(rigid);
//                    bulletAppState.getPhysicsSpace().add(rigid);

                // Let's interact - we mark the hit with a red dot.
                mark.setLocalTranslation(closest.getContactPoint());
                root.attachChild(mark);
            } else {
                // No hits? Then remove the red mark.
                root.detachChild(mark);
            }
        }

        if (binding.equals("Shoot") && !value) {

            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            System.out.println(click2d + "  ");
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            System.out.println(click2d + "  " + click3d + "  " + dir);
            Ray ray = new Ray(click3d, dir);
            shootables.collideWith(ray, results);

            System.out.println("----- Collisions? " + results.size() + "-----");
//
//                for (int i = 0; i < results.size(); i++) {
//
//                    // For each hit, we know distance, impact point, name of geometry.
//                    float dist = results.getCollision(i).getDistance();
//
//                    Vector3f pt = results.getCollision(i).getContactPoint();
//                    String hit = results.getCollision(i).getGeometry().getName();
//                    System.out.println("* Collision #" + i);
//                    System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
//                }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                System.out.println("Removing");
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                Vector3f c = closest.getContactPoint();

//                    Voxel v = tm.getVoxel(c);
                System.out.println("Collision: " + c);
                int type = tm.getVoxelOnCollisionPoint(c).getType().getTexturetype();
                tm.removeVoxel(c);
//                InWorldItemFactory.createItem(c, type, space, items);

                // Let's interact - we mark the hit with a red dot.
                mark.setLocalTranslation(closest.getContactPoint());
                root.attachChild(mark);
            } else {
                // No hits? Then remove the red mark.
                root.detachChild(mark);
            }

        }
        if (binding.equals("CharLeft")) {
            charHelper.setLeft(value);
        } else if (binding.equals("CharRight")) {
            charHelper.setRight(value);
        } else if (binding.equals("CharUp")) {
            charHelper.setUp(value);
        } else if (binding.equals("CharDown")) {
            charHelper.setDown(value);
        }
    }
}
