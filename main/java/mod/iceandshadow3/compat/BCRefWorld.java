package mod.iceandshadow3.compat;

import net.minecraft.world.World;

/**
 * Base class for world references.
 * Written under the realization that under current design, block references can also function as world references.
 * After all, block data is often sent alongside a world.
 */
public abstract class BCRefWorld {
	protected abstract World getWorld();
	
	public boolean isServerSide() {return !getWorld().isRemote;}
	public CDimension getDimension() {return CDimension.get(getWorld().provider.getDimension());}
}
