package mod.iceandshadow3.lib.compat.util

import net.minecraft.nbt._

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object NbtTagUtils {
	final val ID_END = 0
	final val ID_BYTE = 1
	final val ID_SHORT = 2
	final val ID_INT = 3
	final val ID_LONG = 4
	final val ID_FLOAT = 5
	final val ID_DOUBLE = 6
	final val ID_ARRAY_BYTE = 7
	final val ID_STRING = 8
	final val ID_LIST = 9
	final val ID_COMPOUND = 10
	final val ID_ARRAY_INT = 11
	final val ID_ARRAY_LONG = 12

	def canReadToByte(id: Int) = id == ID_BYTE
	def canReadToShort(id: Int) = id >= ID_BYTE && id <= ID_SHORT
	def canReadToInt(id: Int) = id >= ID_BYTE && id <= ID_INT
	def canReadToLong(id: Int) = id >= ID_BYTE && id <= ID_LONG
	def canReadToFloat(id: Int) = id == ID_FLOAT || id >= ID_BYTE && id <= ID_SHORT
	def canReadToDouble(id: Int) = id >= ID_BYTE && id <= ID_DOUBLE && id != ID_LONG
	def canReadToNumber(id: Int) = id >= ID_BYTE && id <= ID_DOUBLE

	def toNbtList(tag: INBT): Option[mutable.Seq[INBT]] =
		if(tag.getId == ID_LIST) Some(tag.asInstanceOf[ListNBT].asScala) else None

	def toNbtFn(tag: INBT): Option[String => Option[INBT]] =
		if(tag.getId == ID_COMPOUND) Some(key => Option(tag.asInstanceOf[CompoundNBT].get(key))) else None
}
