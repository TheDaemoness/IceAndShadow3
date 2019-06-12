package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.{BStatusEffect, StatusEffect}
import mod.iceandshadow3.util.BinderLazy
import net.minecraft.potion.Effect
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

object BinderStatusEffect extends BinderLazy[StatusEffect, BStatusEffect, Effect]({new AStatusEffect(_)}) {

	private[iceandshadow3] def populate(): Unit = {
		import mod.iceandshadow3.world.misc.Statuses._
		//import net.minecraft.potion.Effects._
		//Deobfuscation on Effects seems a bit shaky, so until that gets sorted, we'll get these by ID.
		add(poison, Effect.getPotionById(19))
		add(slow, Effect.getPotionById(2))
		add(blind, Effect.getPotionById(15))
		add(wither, Effect.getPotionById(20))
		add(regen, Effect.getPotionById(10))
	}
}
