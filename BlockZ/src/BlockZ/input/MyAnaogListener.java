/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.input;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import BlockZ.world.TileManager;
import BlockZ.world.VoxelType;

/**
 *
 * @author Christopher
 */
public class MyAnaogListener implements AnalogListener {

    private CharacterControl character;
    private Node shootables;
    private Node root;
    private Camera cam;
    private Geometry mark;
    private InputManager inputManager;

    public MyAnaogListener(CharacterControl character, Node shootables, Node root, Camera cam, Geometry mark, InputManager inputManager, AssetManager assetmanager) {
        this.character = character;
        this.shootables = shootables;
        this.root = root;
        this.cam = cam;
        this.mark = mark;
        this.inputManager = inputManager;
        initMark(assetmanager);
    }
    private final void initMark(AssetManager assetManager) {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }
    public void onAnalog(String binding, float value, float tpf) {

        if ("CharSpace".equals(binding)) {

            character.jump();
        }
    }
}
