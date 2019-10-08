package mod.iceandshadow3.lib.compat.entity.state.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BStatusEffect;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;
import java.util.Collections;

public class AStatusEffect extends Effect {
	private final BStatusEffect fxlogic;
	private final String mcname;
	public AStatusEffect(BStatusEffect fx) {
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

	//@OnlyIn(Dist.CLIENT)
	//private void draw(AbstractGui gui, int x, int y) {
	//	Minecraft.getInstance().getTextureManager().bindTexture(texloc);
	//	final int xSpriteSheet = (textureIndex % 14)*18;
	//	final int ySpriteSheet = (textureIndex / 14)*18;
	//	gui.blit(x, y, xSpriteSheet, ySpriteSheet, 18, 18);
	//}

	//@OnlyIn(Dist.CLIENT)
	//@Override
	//public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z) {
		//draw(gui, x+6, y+7);
	//}

	//@OnlyIn(Dist.CLIENT)
	//@Override
	//public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
	//	draw(gui, x+3, y+3);
	//}
}
