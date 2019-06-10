package mod.iceandshadow3.compat

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.data.{BDataTree, IDataTreeRW, INbtRW}
import net.minecraft.nbt.{NumberNBT, CompoundNBT}

class WNbtTree(val root: CompoundNBT) {
	def isNull: Boolean = root == null
	def isEmpty: Boolean = isNull || root.isEmpty
	def chroot(): WNbtTree = chroot(IaS3.MODID)
	def chroot(key: String): WNbtTree = try {
		if(root == null) return this
		val tags = root.getCompound(key)
		root.put(key, tags)
		new WNbtTree(tags)
	} catch {
		case e: ClassCastException =>
			IaS3.logger().warn("Overriding NBT tag \""+key+"\"")
			e.printStackTrace()
			val newCompound = new CompoundNBT()
			root.put(key, newCompound)
			new WNbtTree(newCompound)
	}
	def set(key: String, obj: INbtRW): Unit =
		if(root != null) root.put(key, obj.toNBT)
	def get(key: String, obj: INbtRW): Boolean = {
		try {
			if(root == null) return false
			//TODO: Check for nulls and set up a default tag value.
			obj.fromNBT(root.get(key))
		} catch {
			case e: Exception =>
				IaS3.logger().error("Root NBT tag mismatch when loading $obj: "+e.getMessage)
				e.printStackTrace()
				false
		}
	}

	/** Accessor that makes the same mistake as atoi and returns 0 on invalid input.
		* This mistake is intended to be exploited.
		*/
	def getLong(key: String): Long = {
		if(root == null) return 0
		val tag = root.get(key)
		if(!tag.isInstanceOf[NumberNBT]) return 0
		tag.asInstanceOf[NumberNBT] getLong()
	}

	def store(key: String, obj: IDataTreeRW[_ <: BDataTree[_]]): Unit =
		set(key, obj.exposeDataTree())
	def load[T <: BDataTree[_]] (key: String, obj: IDataTreeRW[T]): Boolean = {
		val tree: T = obj.exposeDataTree()
		if(get(key,tree)) obj.fromDataTree(tree) else false
	}
}
