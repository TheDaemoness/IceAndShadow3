package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.world.BDomain;
import mod.iceandshadow3.compat.BLogic;
import mod.iceandshadow3.compat.SCreativeTab$;
import net.minecraft.item.Item;

public abstract class BCompatLogicItem extends BLogic {
	public int stackLimit() {return 64;}
	public int damageLimit() {return 0;}
			
	public BCompatLogicItem(BDomain domain, String name) {
		super(domain, name);
	}

	Item.Properties toProperties() {
		Item.Properties retval = new Item.Properties();
		retval.maxStackSize(stackLimit());
		if(stackLimit() == 1 && damageLimit() > 0) retval.defaultMaxDamage(damageLimit());
		if(!isTechnical()) retval.group(SCreativeTab$.MODULE$);
		//TODO: There's more.
		return retval;
	}
}