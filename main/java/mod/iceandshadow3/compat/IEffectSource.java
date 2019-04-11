package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

import mod.iceandshadow3.basics.Damage;
import mod.iceandshadow3.basics.EDamageShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

public interface IEffectSource extends ILocalizable {
	public default @Nullable Entity getEffectSourceEntity() {return null;}
	public @Nullable Damage getAttack();
}
