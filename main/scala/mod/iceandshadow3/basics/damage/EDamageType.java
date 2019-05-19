package mod.iceandshadow3.basics.damage;

import java.util.function.BiFunction;

import javax.annotation.Nullable;

import mod.iceandshadow3.compat.entity.CRefEntity;

public enum EDamageType {
	PHYSICAL(null),
	ICE(null),
	HEAT(null),
	SHOCK(null),
	TOXIN(null),
	EXOUSIA(null);
	
	BiFunction<CRefEntity, Float, Float> onHarm;
	private EDamageType(@Nullable BiFunction<CRefEntity, Float, Float> fn) {
		onHarm = fn;
	}
}