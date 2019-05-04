package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.Damage
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent
import mod.iceandshadow3.compat.TEffectSource
import mod.iceandshadow3.compat.TCRefWorld

//TODO: Manually generated class stub.
class CRefEntity(private[compat] val entity: Entity) extends TCRefWorld with TEffectSource {
	
	override def getNameTextComponent(): ITextComponent = entity.getDisplayName();

	override def getEffectSourceEntity(): Entity = entity
	
	override protected[compat] def getWorld(): net.minecraft.world.World = entity.world
	
	override def getAttack(): Damage =
		if(entity.isInstanceOf[TEffectSource]) entity.asInstanceOf[TEffectSource].getAttack() else null

	def damage(attack: Damage): Unit = 
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this));

}
