package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;

public abstract class BMateria {
	final Material mcmat;
	
	protected BMateria(Material m) {
		mcmat = m;
	}

	public abstract boolean isToolClassEffective(HarvestMethod m);
	public abstract int getBaseHarvestResist();
	public abstract float getBaseBlastResist();
	public abstract float getBaseHardness();
	public boolean isOpaque() {return mcmat.blocksLight();}
	public int getBaseLuma() {return 0;}
	public int getBaseOpacity() {return isOpaque() ? 0xff : 0;}
}
