package mod.iceandshadow3.compat.mat;

import mod.iceandshadow3.compat.BMateria;
import mod.iceandshadow3.compat.HarvestMethod;
import net.minecraft.block.material.Material;

public abstract class BMateriaStone extends BMateria {

	protected BMateriaStone(Material m) {
		super(m);
	}
	protected BMateriaStone() {
		super(Material.ROCK);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.PICKAXE;
	}
}
