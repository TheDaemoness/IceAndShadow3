package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.BLogicItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class SConverter {
	static Block.Properties toProperties(BLogicBlock log) {
		final BMateria mat = ((BMateriaLogic)log).materia;
		Block.Properties retval = Block.Properties.create(mat.mcmat);
		retval.hardnessAndResistance(mat.getBaseHardness(), mat.getBaseBlastResist());
		retval.lightValue(mat.getBaseLuma());
		//TODO: There's more.
		return retval;
	}
	static Item.Properties toProperties(BLogicItem log) {
		Item.Properties retval = new Item.Properties();
		retval.maxStackSize(log.stackLimit());
		if(!log.isTechnical()) retval.group(SCreativeTab$.MODULE$);
		//TODO: There's more.
		return retval;
	}
}
