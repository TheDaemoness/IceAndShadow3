package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.basics.util.IMateria;
import net.minecraft.block.material.Material;

public abstract class BMateria implements IMateria {
	final Material mcmat;

	protected BMateria(Material material) {
		mcmat = material;
	}
	
	@Override
	public int getBaseOpacity() {
		return mcmat.isOpaque() ? 0xff : 0;
	}
}