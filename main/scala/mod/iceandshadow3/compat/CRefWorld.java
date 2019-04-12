package mod.iceandshadow3.compat;

import net.minecraft.world.World;

public class CRefWorld extends BCRefWorld {
	//WARNING: Most functionality should NOT go here, but in BCRefWorld.
	final World world;
	CRefWorld(World w) {
		world = w;
	}
	@Override
	protected World getWorld() {
		return world;
	}

}
