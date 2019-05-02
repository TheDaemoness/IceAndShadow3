package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;

public interface IEffectSource extends ILocalizable {
	public default @Nullable Entity getEffectSourceEntity() {return null;}
	public @Nullable mod.iceandshadow3.basics.Damage getAttack();
}
