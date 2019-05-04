package mod.iceandshadow3.compat.materia;

import mod.iceandshadow3.basics.HarvestMethod;
import mod.iceandshadow3.compat.wrapper.BMateria;
import net.minecraft.block.material.Material;

public abstract class BMateriaLeaves extends BMateria {

	protected BMateriaLeaves() {
		super(Material.LEAVES);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.SHEAR || m == HarvestMethod.BLADE;
	}
	@Override
	public int getBaseHarvestResist() {
		return 0;
	}
	@Override
	public float getBaseBlastResist() {
		return 1f;
	}
}
