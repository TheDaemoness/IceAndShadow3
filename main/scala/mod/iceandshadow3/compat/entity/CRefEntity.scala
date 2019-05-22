package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.damage.Damage
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.TCWorldPlace
import mod.iceandshadow3.util.{EmptyIterator, IPositional, Vec3}
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent

class CRefEntity(protected[compat] val entity: Entity) extends TCWorldPlace with TEffectSource with IPositional {
	override def getEffectSourceEntity: Entity = entity
	override def getNameTextComponent: ITextComponent = entity.getDisplayName
	override def position = new Vec3(entity.posX, entity.posY, entity.posZ)
	
	override protected[compat] def exposeWorld(): net.minecraft.world.World = entity.world

	override def getAttack: Damage = entity match {
		case source: TEffectSource => source.getAttack
		case _ => null
	}

	def damage(attack: Damage): Unit = 
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this))

	def teleport(newpos: Vec3): Unit = {
		//TODO: For very long teleports, do we still need to do chunk loading shenanigans ala gatestones?
		val pitchyaw = entity.getPitchYaw
		entity.setPositionAndUpdate(newpos.xDouble, newpos.yDouble, newpos.zDouble)
	}

	def items(): Iterator[CRefItem] = new EmptyIterator[CRefItem]
}
