package mod.iceandshadow3.compat.materia;

import mod.iceandshadow3.basics.HarvestMethod;
import mod.iceandshadow3.compat.wrapper.BMateria;
import net.minecraft.block.material.Material;

public abstract class BMateriaWood extends BMateria {

	protected BMateriaWood() {
		super(Material.WOOD);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.AXE;
	}
}
