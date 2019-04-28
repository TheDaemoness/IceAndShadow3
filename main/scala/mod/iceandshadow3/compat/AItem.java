package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.ItemLogic;
import net.minecraft.item.Item;

public class AItem extends Item {
	final ItemLogic il;
	AItem(ItemLogic itemlogic) {
		super(SLogicToProperties.convert(itemlogic));
		il = itemlogic;
		//TODO: Creativetab.
	}
}
