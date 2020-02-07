package mod.iceandshadow3.lib.compat.nbt

import net.minecraft.nbt.{CompoundNBT, INBT, ListNBT}

import scala.collection.mutable

import scala.jdk.CollectionConverters._

object NbtTagUtils {
	import net.minecraftforge.common.util.Constants.NBT
	final val ID_END = NBT.TAG_END
	final val ID_BYTE = NBT.TAG_BYTE
	final val ID_SHORT = NBT.TAG_SHORT
	final val ID_INT = NBT.TAG_INT
	final val ID_LONG = NBT.TAG_LONG
	final val ID_FLOAT = NBT.TAG_FLOAT
	final val ID_DOUBLE = NBT.TAG_DOUBLE
	final val ID_ARRAY_BYTE = NBT.TAG_BYTE_ARRAY
	final val ID_STRING = NBT.TAG_STRING
	final val ID_LIST = NBT.TAG_LIST
	final val ID_COMPOUND = NBT.TAG_COMPOUND
	final val ID_ARRAY_INT = NBT.TAG_INT_ARRAY
	final val ID_ARRAY_LONG = NBT.TAG_LONG_ARRAY

	def canReadToByte(id: Int) = id == ID_BYTE
	def canReadToShort(id: Int) = id >= ID_BYTE && id <= ID_SHORT
	def canReadToInt(id: Int) = id >= ID_BYTE && id <= ID_INT
	def canReadToLong(id: Int) = id >= ID_BYTE && id <= ID_LONG
	def canReadToFloat(id: Int) = id == ID_FLOAT || id >= ID_BYTE && id <= ID_SHORT
	def canReadToDouble(id: Int) = id >= ID_BYTE && id <= ID_DOUBLE && id != ID_LONG
	def canReadToNumber(id: Int) = id >= ID_BYTE && id <= ID_DOUBLE

	def toNbtCompound(tag: INBT): Option[CompoundNBT] =
		if(tag.getId == ID_COMPOUND) Some(tag.asInstanceOf[CompoundNBT]) else None

	def toNbtList(tag: INBT): Option[mutable.Seq[INBT]] =
		if(tag != null && tag.getId == ID_LIST) Some(tag.asInstanceOf[ListNBT].asScala) else None

	def toNbtFn(tag: INBT): Option[String => Option[INBT]] =
		if(tag != null && tag.getId == ID_COMPOUND) Some(key => Option(tag.asInstanceOf[CompoundNBT].get(key))) else None
}
