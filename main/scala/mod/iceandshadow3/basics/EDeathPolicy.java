package mod.iceandshadow3.basics;

import mod.iceandshadow3.util.SMath$;

public enum EDeathPolicy {
	ERASE_LOW(Effect.ERASE), 
	ERASE(Effect.ERASE),
	ERASE_HIGH(Effect.ERASE),
	LOSE_LOW(Effect.LOSE),
	LOSE(Effect.LOSE), 
	LOSE_HIGH(Effect.LOSE),
	KEEP(Effect.KEEP), 
	KEEP_HIGH(Effect.KEEP), 
	SAVE(Effect.SAVE), 
	SAVE_HIGH(Effect.SAVE);

	public enum Effect {
		ERASE, //Trigger any decay effects and then destroy.
		LOSE, //Lose and trigger decay effects.
		KEEP, //Keep, but trigger decay effects.
		SAVE; //Keep.
	}
	
	public final Effect effect;
	public static final EDeathPolicy DEFAULT = LOSE_HIGH;
	
	EDeathPolicy(Effect e) {
		effect = e;
	}
	
	EDeathPolicy modify(int mod) {
		final EDeathPolicy[] values = EDeathPolicy.values();
		int newval = SMath$.MODULE$.bound(0, this.ordinal()+mod, values.length-1);
		return values[newval];
	}
	public EDeathPolicy weaken() {
		return this.modify(-1);
	}
	public EDeathPolicy applySavePassive() {
		return this.modify(1);
	}
	public EDeathPolicy applySaveActive() {
		return this.modify(2);
	}
}
