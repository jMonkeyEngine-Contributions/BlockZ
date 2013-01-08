package BlockZ;

import BlockZ.world.TileManager;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import BlockZ.quicksetup.Setup;
import BlockZ.world.EndlessHeightmapWorldProvider;
import BlockZ.world.VoxelType;
import BlockZ.world.WorldProvider;
import com.jme3.math.Vector3f;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    /*test*/
    /*test ende */
//    Mesh worldmesh = new Mesh();
    Setup s;
    CharacterControl chara;
    WorldProvider worldprovider;

    @Override
    public void simpleInitApp() {

        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        s = new Setup();
        chara = s.charackterSetup(inputManager, rootNode, bulletAppState.getPhysicsSpace(), assetManager, cam, this, flyCam);
//        TileManager.getInstance().addVoxel(new VoxelType(1), new Vector3f(127.19216f, 5.9999995f, 128.5386f));
//        s.flyCamSetup(inputManager, rootNode, bulletAppState.getPhysicsSpace(), assetManager, cam, this, flyCam);
//        chara= s.charackterSimpleFloorSetup(inputManager, rootNode, bulletAppState.getPhysicsSpace(), assetManager, cam, this, flyCam);
        Texture heightMapImage = assetManager.loadTexture("Textures/heightmap.jpg");
        AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        worldprovider = new EndlessHeightmapWorldProvider(heightmap, 16, 16);
        setupFilters();
        initHUD();
        Logger.getLogger("").setLevel(Level.SEVERE);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void simpleUpdate(float tpf) {
        s.updateMe(tpf);
        if (chara != null) {
            hudText.setText("CharacterPosition: " + chara.getPhysicsLocation());
        }

        TileManager.getInstance().updateMe(chara, worldprovider);
    }
    private BitmapText hudText;

    private void initHUD() {
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("You can write any string here");             // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
    }
    private FilterPostProcessor fpp;
    private DepthOfFieldFilter dofFilter;
    private float focusd = 2f;
    private float focusr = 50f;

    public void setupFilters() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            fpp = new FilterPostProcessor(assetManager);
            fpp.addFilter(new CartoonEdgeFilter());
            viewPort.addProcessor(fpp);
        }
        dofFilter = new DepthOfFieldFilter();
        dofFilter.setFocusDistance(focusd);
        dofFilter.setFocusRange(focusr);
        dofFilter.setBlurScale(3f);
        fpp.addFilter(dofFilter);
    }

    @Override
    public void stop() {
        super.stop();
        TileManager.collisionShapeProcessor.shutdown();
    }
}
