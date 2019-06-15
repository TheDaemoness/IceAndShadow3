package mod.iceandshadow3

import java.util.stream.Stream

import net.minecraftforge.fml.InterModComms

private[iceandshadow3] object InitModSynergy {
	private[iceandshadow3] def makeRegistries(): Unit = {}
	private[iceandshadow3] def imcSend(): Unit = {}
	private[iceandshadow3] def imcRecv(arg: Stream[InterModComms.IMCMessage]): Unit = {}
}
