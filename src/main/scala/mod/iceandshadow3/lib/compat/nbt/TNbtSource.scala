package mod.iceandshadow3.lib.compat.nbt

import javax.annotation.Nonnull
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.data.BVar
import net.minecraft.nbt.CompoundNBT

trait TNbtSource {
	@Nonnull protected[compat] def exposeNbt(): CompoundNBT
	protected[compat] def setNbt(what: CompoundNBT): Unit

	private def resolveNbt(tags: CompoundNBT, what: BVar[_] with TVarNbt[_]): CompoundNBT = {
		var nbt = tags
		for(objname <- what.path) {
			val tag = nbt.get(objname)
			val isNull = tag == null
			val isAbnormal = isNull || tag.getId != NbtTagUtils.ID_COMPOUND //WARNING: Short circuit!
			if(isAbnormal) {
				if(!isNull) {
					IaS3.logger().warn(
						s"Expected $objname in $nbt (in $this, full path ${what.path}) to be a compound, got $tag. Overwriting!"
					)
				}
				val newcomp = new CompoundNBT
				nbt.put(objname, newcomp)
				nbt = newcomp
			} else nbt = tag.asInstanceOf[CompoundNBT]
		}
		nbt
	}
	def get[T](what: BVar[T] with TVarNbt[T]): Option[T] =
		what.readNbt(resolveNbt(exposeNbt(), what))
	def apply[T](what: BVar[T] with TVarNbt[T]): T = get(what).getOrElse(what.defaultVal)
	def update[T](what: BVar[T] with TVarNbt[T], value: T): Unit = {
		val root = exposeNbt()
		what.writeNbt(resolveNbt(root, what), value)
		setNbt(root)
	}
	def transform[T](what: BVar[T] with TVarNbt[T], fn: T => T): Unit = {
		val root = exposeNbt()
		val child = resolveNbt(root, what)
		what.writeNbt(child, fn(what.readNbt(child).getOrElse(what.defaultVal)))
		setNbt(root)
	}
}
