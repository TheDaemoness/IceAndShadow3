package mod.iceandshadow3.basics;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import mod.iceandshadow3.compat.ADamageSource;
import mod.iceandshadow3.compat.CRefEntity;

public enum EDamageType {
	PHYSICAL(null),
	ICE(null),
	HEAT(null),
	SHOCK(null),
	TOXIN(null),
	EXOUSIA(null);
	
	private EDamageType(@Nullable Object o) {
		
	}
	
	///Modify the damage taken by an entity and apply on-hit effects.
	public float onHarm(CRefEntity victim, float amount) {
		//TODO: Resistance calculations.
		return amount;
	}
	
	///Handle additional effects that should trigger upon a successful kill using a certain type.
	public void onKill(CRefEntity victim, float percent) {
		//TODO: Autotyped method stub.
	}
}
