package mod.iceandshadow3.compat.entity;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BStatusEffect;
import mod.iceandshadow3.util.E3vl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;

import javax.annotation.Nonnull;
import java.util.Collections;

public class AStatusEffect extends Potion {
	private final BStatusEffect fxlogic;
	public AStatusEffect(BStatusEffect fx) {
		super(fx.isBeneficial() == E3vl.FALSE, fx.color().colorcode());
		if(fx.isBeneficial() == E3vl.TRUE) this.setBeneficial();
		this.setRegistryName(IaS3.MODID, fx.name());
		fxlogic = fx;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return fxlogic.shouldTick(duration, amplifier);
	}

	@Override
	public void performEffect(EntityLivingBase elb, int amplifier) {
		fxlogic.onTick(CNVEntity.wrap(elb), amplifier);
	}

	public void applyAttributesModifiersToEntity(EntityLivingBase elb, AbstractAttributeMap map, int amp) {
		fxlogic.onStart(CNVEntity.wrap(elb), amp);
		super.applyAttributesModifiersToEntity(elb, map, amp);
	}

	public void removeAttributesModifiersFromEntity(EntityLivingBase elb, AbstractAttributeMap map, int amp) {
		fxlogic.onEnd(CNVEntity.wrap(elb), amp);
		super.removeAttributesModifiersFromEntity(elb, map, amp);
	}

	@Nonnull
	@Override
	public java.util.List<net.minecraft.item.ItemStack> getCurativeItems() {
		//Haaa, no milk cures in IaS3.
		return Collections.emptyList();
	}
}
