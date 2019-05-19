package mod.iceandshadow3.basics.damage;

import java.util.EnumMap;
import java.util.Map;

import mod.iceandshadow3.compat.entity.TEffectSource;
import mod.iceandshadow3.compat.entity.CRefEntity;

public class Damage {
	protected EnumMap<EDamageType, Float> damages = new EnumMap<>(EDamageType.class);
	protected final DamageForm form;
	protected final TEffectSource source;
	
	Damage(TEffectSource attacker, DamageForm attackForm) {
		form = attackForm;
		source = attacker;
	}

	public void add(EDamageType type, float amount) {
		damages.compute(type, (k, v) -> (v != null) ? v+amount : amount);
	}

	public TEffectSource getSource() {
		return source;
	}
	public DamageForm getForm() {
		return form;
	}
	
	/**
	 * Applies on-damage effects and calculates damage taken.
	 * @return The amount of damage taken, in half-hearts.
	 */
	public float onDamage(CRefEntity victim) {
		float damagetotal = 0f;
		for(Map.Entry<EDamageType, Float> dmg : damages.entrySet()) {
			damagetotal += dmg.getKey().onHarm.apply(victim, dmg.getValue());
		}
		return damagetotal;
	}
}
