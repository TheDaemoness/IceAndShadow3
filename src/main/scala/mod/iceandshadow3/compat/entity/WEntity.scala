package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.dimension.WDimensionCoord
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.compat.world.TWWorldPlace
import mod.iceandshadow3.damage.Attack
import mod.iceandshadow3.forge.{TeleporterDeferred, TeleporterExact}
import mod.iceandshadow3.spatial.{IPositional, IVec3}
import mod.iceandshadow3.util.IteratorEmpty
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.{EnumLightType, Teleporter}
import net.minecraftforge.common.DimensionManager

class WEntity protected[entity](protected[compat] val entity: Entity)
	extends TWWorldPlace
		with TEffectSource
		with IPositional {
	override def getEffectSourceEntity: Entity = entity
	override def sunlight: Int = exposeWorld().getLightFor(EnumLightType.SKY, CNVVec3.toBlockPos(position).add(0,1,0))

	override def getNameTextComponent: ITextComponent = entity.getDisplayName

	override def position = CNVVec3.fromEntity(entity)

	override protected[compat] def exposeWorld(): net.minecraft.world.World = entity.world

	override def getAttack: Attack = entity match {
		case source: TEffectSource => source.getAttack
		case _ => null
	}

	def teleport(newpos: IVec3): Unit = {
		//TODO: For very long teleports, do we still need to do chunk loading shenanigans ala gatestones?
		val pitchyaw = entity.getPitchYaw
		entity.setPositionAndUpdate(newpos.xDouble, newpos.yDouble, newpos.zDouble)
	}

	def teleport(newpos: IVec3, dim: WDimensionCoord): Unit =
		entity.changeDimension(dim.dimtype, new TeleporterExact(newpos))

	def teleport(dim: BDimension): Unit = {
		if (!dim.isEnabled) {
			IaS3.bug(new NullPointerException, s"Attempted to teleport to a disabled BDimension $dim.")
		} else entity.changeDimension(dim.coord.dimtype, new TeleporterDeferred(dim))
	}

	def teleportVanilla(dim: WDimensionCoord): Unit =
		entity.changeDimension(
			dim.dimtype,
			new Teleporter(DimensionManager.getWorld(
				entity.getServer, dim.dimtype, true, true
			)
			)
		)

	def items(): Iterator[WRefItem] = new IteratorEmpty[WRefItem]

	def extinguish(): Unit = entity.extinguish()

	def damage(attacker: TEffectSource): Boolean = if(isServerSide) {
		val how = attacker.getAttack
		if(how == null) false
		else entity.attackEntityFrom(new ADamageSource(how, attacker), how.baseDamage(attacker.getAttackMultiplier))
	} else false
	def damage(how: Attack, multiplier: Float = 1f): Boolean = if(isServerSide) {
		entity.attackEntityFrom(new ADamageSource(how), how.baseDamage(multiplier)
		)
	} else false
}

