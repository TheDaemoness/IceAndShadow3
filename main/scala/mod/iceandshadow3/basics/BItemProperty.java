package mod.iceandshadow3.basics;

import mod.iceandshadow3.compat.item.CRefItem;
import mod.iceandshadow3.compat.CRefWorld;

public abstract class BItemProperty {
	public abstract String name();
	public abstract float call(CRefItem item, CRefWorld world);
}
