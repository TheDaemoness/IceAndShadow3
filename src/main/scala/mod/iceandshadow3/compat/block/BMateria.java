package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.basics.block.IMateria;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public abstract class BMateria implements IMateria {
	final Material mcmat;
	final SoundType snd;

	protected BMateria(Material material, SoundType sndtype) {
		mcmat = material;
		snd = sndtype;
	}
	
	@Override
	public int getBaseOpacity() {
		return mcmat.isOpaque() ? 0xff : 0;
	}
}
