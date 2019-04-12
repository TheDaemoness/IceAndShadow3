package mod.iceandshadow3.compat

import mod.iceandshadow3.basics.Damage;
import mod.iceandshadow3.basics.EDamageShape;
import mod.iceandshadow3.basics.EDamageType;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

//TODO: Manually generated class stub.
class CRefEntity(private[compat] val entity: Entity) extends IEffectSource {
	
	override def getNameTextComponent(): ITextComponent = entity.getDisplayName();

	override def getEffectSourceEntity(): Entity = entity
	
	override def getAttack(): Damage =
		if(entity.isInstanceOf[IEffectSource]) entity.asInstanceOf[IEffectSource].getAttack() else null

	def damage(attack: Damage): Unit = 
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this));

}
