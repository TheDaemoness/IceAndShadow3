package mod.iceandshadow3.damage.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.damage.Attack;
import mod.iceandshadow3.damage.AttackForm;
import mod.iceandshadow3.lib.compat.entity.WEntity;
import mod.iceandshadow3.lib.compat.entity.state.WDamage;
import mod.iceandshadow3.lib.compat.entity.state.WDamage$;
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
	private final Attack attack;
	private final float[] damages;
	private final float total;

	//TODO: Indirect damage.

	public static WDamage buildAndWrap(Attack dmg, WEntity whom, float multiplier) {
		final float[] damages = new float[dmg.instances().size()];
		float total = 0f;
		for(int i = 0; i < damages.length; ++i) {
			final float damage = dmg.instances().apply(i).amount(whom) * multiplier;
			damages[i] = damage;
			total += damage;
		}
		return WDamage$.MODULE$.apply(new ADamageSource(
			dmg,
			damages,
			total
		), total);
	}

	public final float getTotal() {return total;}
	public final float getAmount(int index) {return damages[index];}

	private ADamageSource(final Attack dmg, final float[] damages, float total) {
		super(dmg.name());
		this.setDamageIsAbsolute().setDamageBypassesArmor();
		attack = dmg;
		AttackForm f = attack.form();
		if(f.projectile()) this.setProjectile();
		if(f.mystic()) this.setMagicDamage();
		this.damages = damages;
		this.total = total;
	}

	public Attack getAdsAttack() {return attack;}

	@Nonnull
	@Override
	public ITextComponent getDeathMessage(LivingEntity elb) {
     String s = "death."+IaS3.MODID+'.'+ attack.name();
     	Entity truesource = getTrueSource();
		if(truesource != null) return new TranslationTextComponent(
			s+".attacker",
			elb.getDisplayName(),
			truesource.getDisplayName()
		);
		else return new TranslationTextComponent(s, elb.getDisplayName());
	}

	@Nullable
	@Override
	public Entity getImmediateSource() {
		if(attack.sourceImmediate().isEmpty()) return null;
		else return attack.sourceImmediate().get().expose();
	}

	@Nullable
	@Override
	public Entity getTrueSource() {
		if(attack.sourceTrue().isEmpty()) return null;
		else return attack.sourceTrue().get().expose();
	}
}
