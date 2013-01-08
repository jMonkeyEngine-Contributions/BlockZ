/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.world;

import BlockZ.Vector3i;
import com.jme3.terrain.heightmap.AbstractHeightMap;

/**
 *
 * @author Christopher
 */
public class EndlessHeightmapWorldProvider implements WorldProvider {

    private AbstractHeightMap heightmap;
    private int tileSize;
    private int tileBufferSize;

    public EndlessHeightmapWorldProvider(AbstractHeightMap heightmap, int tileSize, int tileBufferSize) {
        this.heightmap = heightmap;
        this.tileSize = tileSize;
        this.tileBufferSize = tileBufferSize;
    }

    public VoxelType[][] getVoxelTypeForTile(Vector3i v) {

        float maxHeightInMap = heightmap.findMinMaxHeights()[1];
        float scaleFactor = tileBufferSize / maxHeightInMap;
        int y = v.y;
        int maxY = (int) (maxHeightInMap * scaleFactor);

        VoxelType[][] vt = new VoxelType[tileSize][tileSize];
//        System.out.println("v: " + v);
        for (int z = 0; z < tileSize; z++) {
            for (int x = 0; x < tileSize; x++) {
                
                float height = (heightmap.getTrueHeightAtPoint(x + ((Math.abs(v.x) * tileSize) % (tileSize * tileBufferSize)), z + ((Math.abs(v.z) * tileSize) % (tileSize * tileBufferSize))) * scaleFactor);
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
        return vt;


    }

    public VoxelType getVoxelType(Vector3i v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void loadMapData() {
//        TileManager tm = TileManager.getInstance();
    }
}
