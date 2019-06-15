package mod.iceandshadow3.compat.misc

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.data._
import net.minecraft.nbt._

class WNbtTree(val root: CompoundNBT)
{
	def isNull: Boolean = root == null
	def isEmpty: Boolean = isNull || root.isEmpty
	def chroot(): WNbtTree = chroot(IaS3.MODID)
	def chroot(key: String): WNbtTree = try {
		if(root == null) return this
		val tags = root.getCompound(key)
		if(tags.isEmpty) root.put(key, tags)
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

	def getString(key: String): String = {
		if(root == null) "" else Option(root.get(key)).fold("")(_.getString)
	}

	def store(key: String, obj: IDataTreeRW[_ <: BDataTree[_]]): Unit =
		set(key, obj.exposeDataTree())
	def load[T <: BDataTree[_]] (key: String, obj: IDataTreeRW[T]): Boolean = {
		val tree: T = obj.exposeDataTree()
		if(get(key,tree)) obj.fromDataTree(tree) else false
	}

	protected def matchLeaf(tag: INBT): BDataTreeLeaf[_] = tag match {
		case tag: EndNBT => null
		case tag: ByteNBT => new DatumInt8(tag.getByte)
		case tag: ShortNBT => new DatumInt16(tag.getShort)
		case tag: IntNBT => new DatumInt32(tag.getInt)
		case tag: LongNBT => new DatumInt64(tag.getInt)
		case tag: FloatNBT => new DatumFloat(tag.getFloat, true)
		case tag: DoubleNBT => new DatumFloat(tag.getDouble, false)
		case tag: ByteArrayNBT => new DatumInt8Array(tag.getByteArray)
		case tag: StringNBT => new DatumString(tag.getString)
		case tag: IntArrayNBT => new DatumInt32Array(tag.getIntArray)
		case tag: LongArrayNBT => new DatumInt64Array(tag.getAsLongArray)
		case _ =>
			val id = tag.getId
			IaS3.logger().error(s"Unknown NBT tag with type number $id. Corruption, or was there a Minecraft update?")
			null
	}

	def toDataTree: DataTreeMap = {
		val retval = new DataTreeMap()
		import scala.collection.JavaConverters._
		for(key <- root.keySet().asScala) {
			val rawtag = root.get(key)
			retval.add(key, rawtag match {
				case tag: ListNBT => new DataTreeList[BDataTree[_]]
				case tag: CompoundNBT => new WNbtTree(tag).toDataTree
				case _ => matchLeaf(rawtag)
			})
		}
		retval
	}
}
