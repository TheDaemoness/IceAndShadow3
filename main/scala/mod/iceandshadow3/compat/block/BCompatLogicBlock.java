package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.world.BDomain;
import mod.iceandshadow3.basics.HarvestMethod;
import mod.iceandshadow3.basics.IMateria;
import mod.iceandshadow3.compat.item.BCompatLogicCommon;
import net.minecraft.block.Block;

public abstract class BCompatLogicBlock extends BCompatLogicCommon {
	final BMateria materia;
	protected BCompatLogicBlock(BDomain b, String name, BMateria mat) {
		super(b, name);
		materia = mat;
	}
	
	public boolean isToolClassEffective(HarvestMethod m) {
		return materia.isToolClassEffective(m);
	}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(IMateria b) {return b == materia;}
	
	Block.Properties toBlockProperties(int variant) {
		Block.Properties retval = Block.Properties.create(materia.mcmat);
		retval.hardnessAndResistance(materia.getBaseHardness(), materia.getBaseBlastResist());
		retval.lightValue(materia.getBaseLuma());
		if(materia.isEthereal()) retval.doesNotBlockMovement();
		//TODO: There's more.
		return retval;
	}
}
