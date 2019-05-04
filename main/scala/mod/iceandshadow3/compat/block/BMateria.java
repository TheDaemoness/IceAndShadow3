package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.basics.IMateria;
import net.minecraft.block.material.Material;

public abstract class BMateria implements IMateria {
	final Material mcmat;

	protected BMateria(Material material) {
		mcmat = material;
	}
}
