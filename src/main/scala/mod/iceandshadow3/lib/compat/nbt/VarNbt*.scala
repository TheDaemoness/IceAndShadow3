package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.data.Var
import net.minecraft.nbt._

sealed class VarNbtNumeric[T](
	name: String,
	override val defaultVal: T,
	validId: Int => Boolean,
	fromNumberTag: NumberNBT => T,
	makeTag: T => INBT
) extends Var[T](name) with TVarNbt[T] {
	override protected[compat] def fromTag(what: INBT): Option[T] = if(validId(what.getId)) {
		val cnvd = try {
			what.asInstanceOf[NumberNBT]
		} catch {
			case classCastException: ClassCastException =>
				IaS3.bug(classCastException, "NBT tag read failure; did NBT tag type ids change?"); return None
		}
		Some(fromNumberTag(cnvd))
	} else None
	override def isDefaultValue(value: T) = value == defaultVal
	override protected def toTag(value: T) = makeTag(value)
}

class VarNbtBool(name: String, default: Boolean)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToNumber, _.getByte != 0, ByteNBT.func_229672_a_)

class VarNbtByte(name: String, default: Byte)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToByte, _.getByte, ByteNBT.func_229671_a_)

class VarNbtShort(name: String, default: Short)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToShort, _.getShort, ShortNBT.func_229701_a_)

class VarNbtInt(name: String, default: Int)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToInt, _.getInt, IntNBT.func_229692_a_)

class VarNbtLong(name: String, default: Long)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToLong, _.getLong, LongNBT.func_229698_a_)

class VarNbtFloat(name: String, default: Float)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToFloat, _.getFloat, FloatNBT.func_229689_a_)

class VarNbtDouble(name: String, default: Double)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToDouble, _.getDouble, DoubleNBT.func_229684_a_)

class VarNbtString(name: String, override val defaultVal: String)
extends Var[String](name) with TVarNbt[String] {
	override def isDefaultValue(value: String) = defaultVal.contentEquals(value)
	override protected def fromTag(what: INBT) = Some(what.getString)
	override protected def toTag(value: String) = StringNBT.func_229705_a_(value)
}