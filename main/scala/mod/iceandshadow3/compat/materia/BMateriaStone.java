package mod.iceandshadow3.compat.materia;

import mod.iceandshadow3.basics.HarvestMethod;
import mod.iceandshadow3.compat.wrapper.BMateria;
import net.minecraft.block.material.Material;

public abstract class BMateriaStone extends BMateria {

	protected BMateriaStone() {
		super(Material.ROCK);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.PICKAXE;
	}
}
