package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.world.BDomain;
import net.minecraft.item.Item;

public abstract class BCompatLogicItem extends BCompatLogicCommon {
	public int damageLimit(int variant) {return 0;}
			
	public BCompatLogicItem(BDomain domain, String name) {
		super(domain, name);
	}

	@Override
	protected Item.Properties toItemProperties(int variant) {
		Item.Properties retval = super.toItemProperties(variant);
		final int damageLimit = this.damageLimit(variant);
		if(stackLimit(variant) == 1 && damageLimit > 0) retval.defaultMaxDamage(damageLimit);
		//TODO: There's more.
		return retval;
	}
}