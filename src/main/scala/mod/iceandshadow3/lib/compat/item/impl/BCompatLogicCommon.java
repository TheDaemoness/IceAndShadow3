package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.lib.BDomain;
import mod.iceandshadow3.lib.util.BLogic;
import net.minecraft.item.Item;

public abstract class BCompatLogicCommon extends BLogic {
	public BCompatLogicCommon(BDomain domain, String name) {
		super(domain, name);
	}
	
	public int stackLimit(int variant) {
		return 64;
	}
	
	protected Item.Properties toItemProperties(int variant) {
		final Item.Properties retval = new Item.Properties();
		retval.maxStackSize(this.stackLimit(variant));
		retval.rarity(getDomain().tierToRarity(getTier(variant)).rarity);
		if(!isTechnical()) retval.group(CreativeTab$.MODULE$);
		return retval;
	}
}
