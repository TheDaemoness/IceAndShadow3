package mod.iceandshadow3.basics;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import mod.iceandshadow3.compat.ADamageSource;
import mod.iceandshadow3.compat.CRefEntity;
import mod.iceandshadow3.compat.IEffectSource;
import mod.iceandshadow3.compat.ILocalizable;

public class Damage {
	protected EnumMap<EDamageType, Float> damages = new EnumMap<>(EDamageType.class);
	protected final EDamageShape shape;
	protected final IEffectSource source;
	
	Damage(IEffectSource attacker, EDamageShape attackShape) {
		shape = attackShape;
		source = attacker;
	}

	public void add(EDamageType type, float amount) {
		damages.compute(type, (k, v) -> (v != null) ? v+amount : amount);
	}

	public IEffectSource getSource() {
		return source;
	}
	
	/**
	 * Applies on-damage effects and calculates damage taken.
	 * @return The amount of damage taken, in half-hearts.
	 */
	public float onDamage(CRefEntity victim) {
		float damagetotal = 0f;
		for(Map.Entry<EDamageType, Float> dmg : damages.entrySet()) {
			damagetotal += dmg.getKey().onHarm(victim, dmg.getValue());
		}
		return damagetotal;
	}
}
