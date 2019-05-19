package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.damage.Damage
import mod.iceandshadow3.compat.world.TCRefWorldPlace
import mod.iceandshadow3.util.{IPositional, Vec3}
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent

//TODO: Manually generated class stub.
class CRefEntity(protected[compat] val entity: Entity) extends TCRefWorldPlace with TEffectSource with IPositional {
	
	override def getNameTextComponent: ITextComponent = entity.getDisplayName

	override def getEffectSourceEntity: Entity = entity
	
	override protected[compat] def getWorld(): net.minecraft.world.World = entity.world
	
	override def getAttack: Damage = entity match {
		case source: TEffectSource => source.getAttack
		case _ => null
	}

	def damage(attack: Damage): Unit = 
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this))

	override def position = new Vec3(entity.posX, entity.posY, entity.posZ)
}
