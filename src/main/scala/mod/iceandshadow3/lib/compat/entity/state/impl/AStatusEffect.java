package mod.iceandshadow3.lib.compat.entity.state.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.StatusEffect;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.WEntityLiving;
import mod.iceandshadow3.lib.compat.entity.state.WDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;
import java.util.Collections;

public class AStatusEffect extends Effect {
	private final StatusEffect fxlogic;
	private final String mcname;
	public AStatusEffect(StatusEffect fx) {
		super(
			fx.isBeneficial().isTrue() ? EffectType.BENEFICIAL : (
				fx.isBeneficial().isFalse() ? EffectType.HARMFUL : EffectType.NEUTRAL
			),
			fx.color().colorcode()
		);
		final ResourceLocation namespaced = new ResourceLocation(IaS3.MODID, fx.name());
		this.setRegistryName(namespaced);
		mcname = Util.makeTranslationKey("effect", namespaced);
		fxlogic = fx;
	}

	@Override
	@Nonnull
	protected String getOrCreateDescriptionId() {
		return this.mcname;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return fxlogic.shouldTick(duration, amplifier+1);
	}

	@Override
	public void performEffect(@Nonnull LivingEntity elb, int amplifier) {
		fxlogic.onTick(CNVEntity.wrap(elb), amplifier+1);
	}

	public void applyAttributesModifiersToEntity(LivingEntity elb, @Nonnull AbstractAttributeMap map, int amp) {
		fxlogic.onStart(CNVEntity.wrap(elb), amp+1);
		super.applyAttributesModifiersToEntity(elb, map, amp);
	}

	public void removeAttributesModifiersFromEntity(LivingEntity elb, @Nonnull AbstractAttributeMap map, int amp) {
		fxlogic.onEnd(CNVEntity.wrap(elb), amp+1);
		super.removeAttributesModifiersFromEntity(elb, map, amp);
	}

	@Nonnull
	@Override
	public java.util.List<net.minecraft.item.ItemStack> getCurativeItems() {
		//Haaa, no milk cures in IaS3.
		return Collections.emptyList();
	}

	public float onHarm(WEntityLiving who, WDamage how, int ampVanilla)  {
		return fxlogic.onHarm(who, how, ampVanilla+1);
	}
}
