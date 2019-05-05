package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.world.BDomain;
import mod.iceandshadow3.basics.IMateria;
import mod.iceandshadow3.compat.BLogic;
import net.minecraft.block.Block;

public abstract class BCompatLogicBlock extends BLogic {
	final BMateria materia;
	protected BCompatLogicBlock(BDomain b, String name, BMateria mat) {
		super(b, name);
		materia = mat;
	}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(IMateria b) {return b == materia;}
	
	Block.Properties toProperties() {
		Block.Properties retval = Block.Properties.create(materia.mcmat);
		retval.hardnessAndResistance(materia.getBaseHardness(), materia.getBaseBlastResist());
		retval.lightValue(materia.getBaseLuma());
		//TODO: There's more.
		return retval;
	}
}
