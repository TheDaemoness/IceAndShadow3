package mod.iceandshadow3.compat.mat;

import mod.iceandshadow3.compat.BMateria;
import mod.iceandshadow3.compat.HarvestMethod;
import net.minecraft.block.material.Material;

public abstract class BMateriaWood extends BMateria {

	protected BMateriaWood(Material m) {
		super(m);
	}
	protected BMateriaWood() {
		super(Material.WOOD);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.AXE;
	}
}
