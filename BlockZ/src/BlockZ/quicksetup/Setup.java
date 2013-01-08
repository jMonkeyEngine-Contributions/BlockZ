/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.quicksetup;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import BlockZ.Vector3i;
import BlockZ.input.CharackterControlHelper;
import BlockZ.input.MyActionListener;
import BlockZ.input.MyAnaogListener;

import BlockZ.rowedtexture.Rowedtexture;
import BlockZ.world.Tile;
import BlockZ.world.TileManager;
import BlockZ.world.VoxelType;
import com.jme3.font.BitmapText;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

/**
 *
 * @author Christopher
 */
public class Setup {

    InputManager inputManager;
    private ActionListener actionListener;
    private Node shootables = new Node("Shootables");
    private AnalogListener pressedListener;
//    private Node items = new Node("Items");
    private Geometry mark;
    public static Material testMaterial;
    private AbstractHeightMap heightmap = null;
    private AssetManager assetManager;
    private PhysicsSpace space;
    private Rowedtexture rex;
    private ChaseCamera chaseCam;
    private Camera cam;
    private Application app;
    private FlyByCamera flyCam;
    private CharacterControl character;
    private Node model;
    private CharackterControlHelper charHelper = new CharackterControlHelper();
    private Vector3f walkDirection;
    private float airTime;
    private Node rootNode;
    private boolean setupKeys = true;

    public CharacterControl charackterSetup(InputManager inputManager, Node rootNode, PhysicsSpace space, AssetManager assetManager, Camera cam, Application app, FlyByCamera flyCamera) {

        this.assetManager = assetManager;
        this.space = space;
        this.inputManager = inputManager;
        this.cam = cam;
        this.rootNode = rootNode;
        flyCam = flyCamera;
        initMark(assetManager);
        Texture heightMapImage = assetManager.loadTexture("Textures/heightmap.jpg");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();


        initMaterial();
//        shootables.setShadowMode(ShadowMode.CastAndReceive);
        TileManager.init(space, shootables, app, rex, testMaterial, 16, 16);
        createCharacter();
        setupChaseCamera();



        if (actionListener == null) {
            actionListener = new MyActionListener(charHelper, space, shootables, rootNode, cam, inputManager, assetManager);
        }
        pressedListener = new MyAnaogListener(character, shootables, rootNode, cam, mark, inputManager, assetManager);
        loadMapData();
        rootNode.attachChild(shootables);
        initKeys();
        return character;

    }

    public CharacterControl charackterSimpleFloorSetup(InputManager inputManager, Node rootNode, PhysicsSpace space, AssetManager assetManager, Camera cam, Application app, FlyByCamera flyCamera) {
        return charackterSimpleFloorSetup(inputManager, rootNode, space, assetManager, cam, app, flyCamera, 16, 16);
    }

    public CharacterControl charackterSimpleFloorSetup(InputManager inputManager, Node rootNode, PhysicsSpace space, AssetManager assetManager, Camera cam, Application app, FlyByCamera flyCamera, int tileBufferSize, int tileSize) {

        this.assetManager = assetManager;
        this.space = space;
        this.inputManager = inputManager;
        this.cam = cam;
        this.rootNode = rootNode;
        flyCam = flyCamera;
        initMark(assetManager);

        initMaterial();

        TileManager.init(space, shootables, app, rex, testMaterial, tileBufferSize, tileSize);
        createCharacter();
        setupChaseCamera();


        if (actionListener == null) {
            actionListener = new MyActionListener(charHelper, space, shootables, rootNode, cam, inputManager, assetManager);
        }
        pressedListener = new MyAnaogListener(character, shootables, rootNode, cam, mark, inputManager, assetManager);
        generateFloor(0.0f);

        rootNode.attachChild(shootables);
        initKeys();

        return character;
    }

    public void flyCamSetup(InputManager inputManager, Node rootNode, PhysicsSpace space, AssetManager assetManager, Camera cam, Application app, FlyByCamera flyCamera) {

        flyCamSetup(inputManager, rootNode, space, assetManager, cam, app, flyCamera, 16, 16);
    }

    public void flyCamSetup(InputManager inputManager, Node rootNode, PhysicsSpace space, AssetManager assetManager, Camera cam, Application app, FlyByCamera flyCamera, int tileBufferSize, int tileSize) {

        this.assetManager = assetManager;
        this.space = space;
        this.inputManager = inputManager;
        this.cam = cam;
        this.rootNode = rootNode;
        flyCam = flyCamera;
        initMark(assetManager);
        Texture heightMapImage = assetManager.loadTexture("Textures/heightmap.jpg");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        flyCamera.setMoveSpeed(30.0f);
        initMaterial();
        if (actionListener == null) {
            actionListener = new MyActionListener(charHelper, space, shootables, rootNode, cam, inputManager, assetManager);
        }
        pressedListener = new MyAnaogListener(character, shootables, rootNode, cam, mark, inputManager, assetManager);
        TileManager.init(space, shootables, app, rex, testMaterial, tileBufferSize, tileSize);
        loadMapData();
        rootNode.attachChild(shootables);
        initKeys();


    }

    private void setupChaseCamera() {
        flyCam.setEnabled(false);
//        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.1f, 1000f);
//        cam.setFrustumFar(100f);

        chaseCam = new ChaseCamera(cam, model, inputManager);
        chaseCam.setDefaultDistance(15.f);
        chaseCam.setDefaultVerticalRotation((FastMath.PI * 5) / 16);
    }

    private void initMaterial() {
        testMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        testMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/BlockTextures.png"));
//        testMaterial.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        testMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        rex = new Rowedtexture(20);

    }

    private void loadMapData() {
        TileManager tm = TileManager.getInstance();
        int tileCount = heightmap.getSize() / Tile.getSize();
        float maxHeightInMap = heightmap.findMinMaxHeights()[1];
        float scaleFactor = tm.getSize() / maxHeightInMap;

        int maxY = (int) (maxHeightInMap * scaleFactor);

        for (int y = 0; y < maxY; y++) {
            System.out.println("maxY: " + maxY);
            for (int i = 0; i < tileCount; i++) {
                for (int j = 0; j < tileCount; j++) {

                    VoxelType[][] vt = new VoxelType[Tile.getSize()][Tile.getSize()];

                    for (int z = 0; z < Tile.getSize(); z++) {
                        for (int x = 0; x < Tile.getSize(); x++) {
                            float height = (heightmap.getTrueHeightAtPoint(x + i * Tile.getSize(), z + j * Tile.getSize()) * scaleFactor);
                            if (height >= y) {
                                if (height > maxY * 2 / 3) {
                                    vt[x][z] = new VoxelType(2);

                                } else if (height > maxY / 3) {
                                    vt[x][z] = new VoxelType(1);

                                } else {
                                    vt[x][z] = new VoxelType(0);
                                }

                            }
                        }
                    }
                    Vector3i tilePosition = new Vector3i(i, y, j);
                    tm.addTile(tilePosition);
                    Tile t = tm.getTile(tilePosition);
                    t.generate(vt);
                    t.getGeometry().setMaterial(testMaterial);

                }
            }

        }

        tm.updateAllTiles();

        tm.setRigidsforAllTiles();
    }

    private void initKeys() {

        if (setupKeys) {
            inputManager.addMapping("Shoot",
                    new MouseButtonTrigger(0));
            inputManager.addListener(actionListener, "Shoot");
            inputManager.addMapping("Add",
                    new MouseButtonTrigger(1));
            inputManager.addListener(actionListener, "Add");

            inputManager.addMapping("pagetilesx", new KeyTrigger(KeyInput.KEY_Q));
            inputManager.addMapping("pagetilesxi", new KeyTrigger(KeyInput.KEY_E));
            inputManager.addMapping("pagetilesy", new KeyTrigger(KeyInput.KEY_R));
            inputManager.addMapping("pagetilesyi", new KeyTrigger(KeyInput.KEY_F));
            inputManager.addMapping("pagetilesz", new KeyTrigger(KeyInput.KEY_X));
            inputManager.addMapping("pagetileszi", new KeyTrigger(KeyInput.KEY_C));
            inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
            inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
            inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_W));
            inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_S));


            inputManager.addMapping("ItemRain", new KeyTrigger(KeyInput.KEY_T));

            inputManager.addListener(actionListener, "pagetilesxi");
            inputManager.addListener(actionListener, "pagetilesx");
            inputManager.addListener(actionListener, "pagetilesyi");
            inputManager.addListener(actionListener, "pagetilesy");
            inputManager.addListener(actionListener, "pagetileszi");
            inputManager.addListener(actionListener, "pagetilesz");
            inputManager.addListener(actionListener, "ItemRain");
            inputManager.addListener(actionListener, "CharLeft");
            inputManager.addListener(actionListener, "CharRight");
            inputManager.addListener(actionListener, "CharUp");
            inputManager.addListener(actionListener, "CharDown");

        }
        inputManager.addMapping("CharSpace", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(pressedListener, "CharSpace");
    }

    private final void initMark(AssetManager assetManager) {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    private void generateTileNode(Tile t) {
        if (t != null) {
            t.generate((VoxelType) new VoxelType(0));

            t.getGeometry().setMaterial(testMaterial);
            t.updateAll();
//             TileManager tm = TileManager.getInstance();
            TileManager.updateCollisionShapeOfTile(t);
            if (t.hasVisibleFaces()) {
                space.add(t.getRigid());
            }
        }
    }

    private void generateFloor(float level) {
        TileManager tm = TileManager.getInstance();
        int size = tm.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Vector3i tposition = new Vector3i(i, level, j);
                tm.addTile(tposition);
                tm.getTile(tposition);
                generateTileNode(tm.getTile(tposition));
            }
        }

    }

    private void createCharacter() {
        CapsuleCollisionShape collsionShape = new CapsuleCollisionShape(0.5f, 1f);
        walkDirection = new Vector3f();
        character = new CharacterControl(collsionShape, 0.01f);
//        character.set
        character.setJumpSpeed(10);
        model = (Node) new Node("char");

        Geometry charGeo = new Geometry("boxCharackter", new Box(0.5f, 1f, 0.5f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);


        charGeo.setMaterial(mat);
        model.attachChild(charGeo);

        model.addControl(character);
        Vector3f startposition = TileManager.getInstance().centralOfBufferInWorldCoords();
        startposition.y = startposition.y + 10;
        if (heightmap != null) {
            startposition.x = (heightmap.getSize() / 2) + 0.2f;
            startposition.z = (heightmap.getSize() / 2) + 0.2f;
        }

        character.setPhysicsLocation(startposition);
        rootNode.attachChild(model);
        space.add(character);

    }

    public void updateMe(float tpf) {
        if (character != null && walkDirection != null) {
            Vector3f camDir = cam.getDirection().clone().multLocal(0.1f);
            Vector3f camLeft = cam.getLeft().clone().multLocal(0.1f);
            camDir.y = 0;
            camLeft.y = 0;

            walkDirection.set(0, 0, 0);
            if (charHelper.isLeft()) {
                walkDirection.addLocal(camLeft);
            }
            if (charHelper.isRight()) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (charHelper.isUp()) {
                walkDirection.addLocal(camDir);
            }
            if (charHelper.isDown()) {
                walkDirection.addLocal(camDir.negate());
            }
            if (!character.onGround()) {
                airTime = airTime + tpf;
            } else {
                airTime = 0;
            }
            character.setViewDirection(camDir);
            character.setWalkDirection(walkDirection.mult(1f));
        }


    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public Node getCharacterNode() {
        return model;
    }

    public Node getWorldBlocksNode() {
        return shootables;
    }

    public CharackterControlHelper getCharHelper() {
        return charHelper;
    }

    public void setSetupKeys(boolean setupKeys) {
        this.setupKeys = setupKeys;
    }
}
