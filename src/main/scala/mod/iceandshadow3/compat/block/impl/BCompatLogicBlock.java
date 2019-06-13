package mod.iceandshadow3.compat.block.impl;

import mod.iceandshadow3.basics.BDomain;
import mod.iceandshadow3.basics.block.HarvestMethod;
import mod.iceandshadow3.basics.block.IMateria;
import mod.iceandshadow3.compat.item.impl.BCompatLogicCommon;
import net.minecraft.block.Block;

public abstract class BCompatLogicBlock extends BCompatLogicCommon {
	final BMateria materia;
	protected BCompatLogicBlock(BDomain b, String name, BMateria mat) {
		super(b, name);
		materia = mat;
	}
	
	public boolean isToolClassEffective(int variant, HarvestMethod m) {
		return materia.isToolClassEffective(m);
	}
	protected boolean randomlyUpdates() {return false;}
	protected boolean multipleOpacities() {return false;}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(IMateria b) {return b == materia;}
	
	final Block.Properties toBlockProperties(int variant) {
		Block.Properties retval = Block.Properties.create(materia.mcmat);
		retval.hardnessAndResistance(materia.getBaseHardness(), materia.getBaseBlastResist());
		retval.lightValue(materia.getBaseLuma());
		retval.slipperiness(materia.getSlipperiness());
		if(multipleOpacities()) retval.variableOpacity();
		if(randomlyUpdates()) retval.tickRandomly();
		if(materia.isNonSolid()) retval.doesNotBlockMovement();
		retval.sound(materia.snd);
		//TODO: There's more.
		return retval;
	}
}
