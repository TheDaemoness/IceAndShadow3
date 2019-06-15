package mod.iceandshadow3.compat.entity.impl

//WARNING: ELDRITCH CODE.

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.{BLogicCommonEntity, BLogicEntity, BLogicMob, BLogicProjectile}
import mod.iceandshadow3.util.BinderLazy
import net.minecraft.entity.{Entity, EntityClassification, EntityType}
import net.minecraft.world.World

import scala.collection.mutable.ListBuffer
import scala.reflect.{ClassTag, classTag}

object BBinderEntity {
	private[iceandshadow3] val binders = new ListBuffer[BBinderEntity[_, _ <: Entity]]
}
sealed class BBinderEntity[K <: BLogicCommonEntity: ClassTag, V <: Entity: ClassTag](classification: EntityClassification)
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
	val etype = builder.build(logic.getName)
	etype.setRegistryName(IaS3.MODID, logic.getName)
	etype
}) {
	BBinderEntity.binders += this
}

object BinderEntity extends BBinderEntity[BLogicEntity, AEntity](EntityClassification.MISC)
object BinderProjectile extends BBinderEntity[BLogicProjectile, AProjectile](EntityClassification.MISC)
object BinderMob extends BBinderEntity[BLogicMob, AMob](EntityClassification.MONSTER)

