package mod.iceandshadow3.compat.mat;

import mod.iceandshadow3.compat.BMateria;
import mod.iceandshadow3.compat.HarvestMethod;
import net.minecraft.block.material.Material;

public abstract class BMateriaLeaves extends BMateria {

	protected BMateriaLeaves(Material m) {
		super(m);
	}
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
