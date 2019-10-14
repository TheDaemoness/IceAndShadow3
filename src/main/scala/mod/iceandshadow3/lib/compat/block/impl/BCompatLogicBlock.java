package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BDomain;
import mod.iceandshadow3.lib.block.HarvestMethod;
import mod.iceandshadow3.lib.block.IMateria;
import mod.iceandshadow3.lib.compat.block.BMateria;
import mod.iceandshadow3.lib.compat.block.WBlockState;
import mod.iceandshadow3.lib.compat.item.impl.BCompatLogicCommon;
import net.minecraft.block.Block;

import javax.annotation.Nullable;

public abstract class BCompatLogicBlock extends BCompatLogicCommon {
	final BMateria materia;
	protected BCompatLogicBlock(BDomain b, String name, BMateria mat) {
		super(b, name);
		materia = mat;
	}
	
	public boolean isToolClassEffective(int variant, HarvestMethod m) {
		return materia.isToolClassEffective(m);
	}
	protected boolean randomlyUpdates(@Nullable WBlockState what) {return false;}
	protected boolean multipleOpacities() {return false;}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(Class<?> b) {return b.isInstance(materia);}
	
	final Block.Properties toBlockProperties(int variant) {
		Block.Properties retval = Block.Properties.create(materia.mcmat());
		retval.hardnessAndResistance(materia.getBaseHardness(), materia.getBaseBlastResist());
		retval.lightValue(materia.getBaseLuma());
		retval.slipperiness(materia.getSlipperiness());
		if(multipleOpacities()) retval.variableOpacity();
		if(randomlyUpdates(null)) retval.tickRandomly();
		if(materia.isNonSolid()) retval.doesNotBlockMovement();
		retval.sound(materia.sound());
		//TODO: There's more.
		return retval;
	}
}
