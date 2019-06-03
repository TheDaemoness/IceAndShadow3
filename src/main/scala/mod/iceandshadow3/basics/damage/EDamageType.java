package mod.iceandshadow3.basics.damage;

import mod.iceandshadow3.compat.entity.WEntity;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public enum EDamageType {
	PHYSICAL(null),
	ICE(null),
	HEAT(null),
	SHOCK(null),
	TOXIN(null),
	EXOUSIA(null);
	
	BiFunction<WEntity, Float, Float> onHarm;
	private EDamageType(@Nullable BiFunction<WEntity, Float, Float> fn) {
		onHarm = fn;
	}
}