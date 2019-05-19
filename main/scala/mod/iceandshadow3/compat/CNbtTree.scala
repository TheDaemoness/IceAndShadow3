package mod.iceandshadow3.compat

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.data.{BDataTree, IDataTreeRW, INbtRW}
import net.minecraft.nbt.{NBTPrimitive, NBTTagCompound}

class CNbtTree(val root: NBTTagCompound) {
	def chroot(key: String): CNbtTree = try {
		if(root == null) return this
		val tags = root.getCompound(key)
		root.setTag(key, tags)
		new CNbtTree(tags)
	} catch {
		case e: ClassCastException => {
			IaS3.logger().warn("Overriding NBT tag \""+key+"\"")
			e.printStackTrace()
			val newCompound = new NBTTagCompound()
			root.setTag(key, newCompound)
			new CNbtTree(newCompound)
		}
	}
	def set(key: String, obj: INbtRW) =
		if(root != null) root.setTag(key, obj.toNBT)
	def get(key: String, obj: INbtRW): Boolean = {
		try {
			if(root == null) return false
			//TODO: Check for nulls and set up a default tag value.
			obj.fromNBT(root.getTag(key))
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
		val tag = root.getTag(key)
		if(!tag.isInstanceOf[NBTPrimitive]) return 0
		tag.asInstanceOf[NBTPrimitive] getLong()
	}

	def store(key: String, obj: IDataTreeRW[_ <: BDataTree[_]]) =
		set(key, obj.newDataTree)
	def load[T <: BDataTree[_]] (key: String, obj: IDataTreeRW[T]): Boolean = {
		val tree: T = obj.newDataTree
		if(get(key,tree)) obj.fromDataTree(tree) else false
	}
}
