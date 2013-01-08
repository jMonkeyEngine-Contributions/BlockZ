/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BlockZ.world;

import BlockZ.Vector3i;

/**
 *
 * @author Christopher
 */
public interface WorldProvider {
 
    
    public VoxelType[][] getVoxelTypeForTile(Vector3i v);
    public VoxelType getVoxelType(Vector3i v);
}
