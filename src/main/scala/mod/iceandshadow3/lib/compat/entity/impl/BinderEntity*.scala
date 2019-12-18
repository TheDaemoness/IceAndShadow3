package mod.iceandshadow3.lib.compat.entity.impl

//WARNING: ELDRITCH CODE.

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.util.collect.BinderLazy
import mod.iceandshadow3.lib.{BLogicEntity, BLogicEntitySpecial, BLogicEntityMob, BLogicEntityProjectile}
import net.minecraft.entity.{Entity, EntityClassification, EntityType}
import net.minecraft.world.World

import scala.collection.mutable.ListBuffer
import scala.reflect.{ClassTag, classTag}

object BinderEntity {
	private[iceandshadow3] val binders = new ListBuffer[BinderEntity[_, _ <: Entity]]
}
sealed class BinderEntity[K <: BLogicEntity: ClassTag, V <: Entity: ClassTag](classification: EntityClassification)
extends BinderLazy[K, K, EntityType[_ <: V]](logic => {
	val builder = EntityType.Builder.create[V](
		(mctype: EntityType[V], world: World) => {
			classTag[V].runtimeClass.getDeclaredConstructor(
				classTag[K].runtimeClass, mctype.getClass, classOf[World]
			).newInstance(logic, mctype, world).asInstanceOf[V]
		},
		classification
	)
	if(logic.isTechnical) builder.disableSummoning()
	if(logic.canBurn) builder.immuneToFire()
	val etype = builder.build(logic.name)
	etype.setRegistryName(IaS3.MODID, logic.name)
	etype
}) {
	BinderEntity.binders += this
}

private[lib] object BinderEntitySpecial extends BinderEntity[BLogicEntitySpecial, AEntitySpecial](EntityClassification.MISC)
private[lib] object BinderEntityProjectile extends BinderEntity[BLogicEntityProjectile, AProjectile](EntityClassification.MISC)
private[lib] object BinderEntityMob extends BinderEntity[BLogicEntityMob, AMob](EntityClassification.MONSTER)

