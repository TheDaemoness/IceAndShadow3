package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.util.NbtTagUtils
import mod.iceandshadow3.lib.data.BVar
import net.minecraft.nbt._

sealed class VarNbtNumeric[T](
	name: String,
	default: T,
	validId: Int => Boolean,
	fromNumberTag: NumberNBT => T,
	makeTag: T => INBT
) extends BVar[T](name, default) with TVarNbt[T] {
	override protected[compat] def fromTag(what: INBT): Option[T] = if(validId(what.getId)) {
		val cnvd = try {
			what.asInstanceOf[NumberNBT]
		} catch {
			case classCastException: ClassCastException =>
				IaS3.bug(classCastException, "NBT tag read failure; did NBT tag type ids change?"); return None
		}
		Some(fromNumberTag(cnvd))
	} else None

	override protected def toTag(value: T) = makeTag(value)
}

class VarNbtByte(name: String, default: Byte)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToByte, _.getByte, new ByteNBT(_))

class VarNbtShort(name: String, default: Short)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToShort, _.getShort, new ShortNBT(_))

class VarNbtInt(name: String, default: Int)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToInt, _.getInt, new IntNBT(_))

class VarNbtLong(name: String, default: Long)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToLong, _.getLong, new LongNBT(_))

class VarNbtFloat(name: String, default: Float)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToFloat, _.getFloat, new FloatNBT(_))

class VarNbtDouble(name: String, default: Double)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToDouble, _.getDouble, new DoubleNBT(_))