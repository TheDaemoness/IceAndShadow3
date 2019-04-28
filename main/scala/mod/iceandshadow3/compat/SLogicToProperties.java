package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.BlockLogic;
import mod.iceandshadow3.basics.ItemLogic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class SLogicToProperties {
	static Block.Properties convert(BlockLogic log) {
		final BMateria mat = ((BBlockLogic)log).materia;
		Block.Properties retval = Block.Properties.create(mat.mcmat);
		retval.hardnessAndResistance(mat.getBaseHardness(), mat.getBaseBlastResist());
		retval.lightValue(mat.getBaseLuma());
		return retval;
	}
	static Item.Properties convert(ItemLogic log) {
		Item.Properties retval = new Item.Properties();
		retval.maxStackSize(log.stackLimit());
		return retval;
	}
}
