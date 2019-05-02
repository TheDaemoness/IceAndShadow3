package mod.iceandshadow3.compat;

import mod.iceandshadow3.IceAndShadow3;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.ILogicProvider;
import net.minecraft.item.Item;

public class AItem extends Item implements ILogicProvider<BLogicItem> {
	final BLogicItem il;
	AItem(BLogicItem itemlogic, int variant) {
		super(SConverter.toProperties(itemlogic));
		il = itemlogic;
		this.setRegistryName(IceAndShadow3.MODID, il.getName(variant));
	}
	
	@Override
	public BLogicItem getLogic() {
		return il;
	}
}
