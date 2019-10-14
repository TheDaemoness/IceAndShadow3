package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.lib.BDomain;
import mod.iceandshadow3.lib.compat.item.WItemType;
import net.minecraft.item.Item;

public abstract class BCompatLogicItem extends BCompatLogicCommon {
	public abstract int damageLimit(int variant);
			
	public BCompatLogicItem(BDomain domain, String name) {
		super(domain, name);
	}

	@Override
	protected final Item.Properties toItemProperties(int variant) {
		Item.Properties retval = super.toItemProperties(variant);
		final int damageLimit = this.damageLimit(variant);
		if(stackLimit(variant) == 1 && damageLimit > 0) retval.defaultMaxDamage(damageLimit);
		//TODO: There's more.
		return retval;
	}
}