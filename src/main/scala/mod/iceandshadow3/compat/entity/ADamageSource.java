package mod.iceandshadow3.compat.entity;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.damage.Attack;
import mod.iceandshadow3.damage.AttackForm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Damage source.
 */
public class ADamageSource extends DamageSource {

	private final TEffectSource source;
	private final Attack attack;
	
	public ADamageSource(final Attack dmg, final TEffectSource source) {
		super(dmg.name());
		this.source = source;
		this.setDamageIsAbsolute().setDamageBypassesArmor();
		attack = dmg;
		AttackForm f = attack.form();
		if(f.isProjectile()) this.setProjectile();
		if(f.isMystic()) this.setMagicDamage();
	}
	public ADamageSource(final Attack dmg) {
		this(dmg, null);
	}

	public Attack getAdsAttack() {return attack;}

	@Nonnull
	@Override
	public ITextComponent getDeathMessage(LivingEntity elb) {
     String s = "death."+IaS3.MODID+'.'+ attack.name();
		if(source != null) return new TranslationTextComponent(
			s+".attacker",
			elb.getDisplayName(),
			source.getNameTextComponent()
		);
		else return new TranslationTextComponent(s, elb.getDisplayName());
	}

	@Nullable
	@Override
	public Entity getImmediateSource() {
		return (source == null)?null:source.getEffectSourceEntity();
	}

	@Nullable
	@Override
	public Entity getTrueSource() {
		Entity truesource = null;
		for(TEffectSource it = source; it != null; it = it.getMaster()) {
			truesource = it.getEffectSourceEntity();
		}
		return truesource;
	}
}
