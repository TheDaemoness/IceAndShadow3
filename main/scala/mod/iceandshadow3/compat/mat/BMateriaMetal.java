package mod.iceandshadow3.compat.mat;

import mod.iceandshadow3.compat.BMateria;
import mod.iceandshadow3.compat.HarvestMethod;
import net.minecraft.block.material.Material;

public abstract class BMateriaMetal extends BMateria {

	protected BMateriaMetal(Material m) {
		super(m);
	}
	protected BMateriaMetal() {
		super(Material.IRON);
	}

	@Override
	public boolean isToolClassEffective(HarvestMethod m) {
		return m == HarvestMethod.PICKAXE;
	}
}
