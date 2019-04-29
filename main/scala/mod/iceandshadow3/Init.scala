package mod.iceandshadow3

import mod.iceandshadow3.compat.CRegistryBlock
import mod.iceandshadow3.compat.CRegistryItem

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.InterModComms

import net.minecraft.block.Block
import net.minecraft.item.Item

import scala.collection.mutable.MutableList;

object Init {
	private val domains: MutableList[BDomain] = new MutableList
	private[iceandshadow3] def addDomain(dom: BDomain) = {domains += dom}
	
	def initEarly(): Unit = {
		//Domains.nyx = new DomainNyx
	}
	def initCommon(): Unit = {}
	def initClient(): Unit = {}
	def imcSend(): Unit = {}
	def initRegistries(): Unit = {} //TODO: RegistryBuilder
	def registerBlocks(reg: IForgeRegistry[Block]): Unit = 
		for(dom <- domains) dom.register(new CRegistryBlock(reg))
	
	def registerItems(reg: IForgeRegistry[Item]): Unit = 
		for(dom <- domains) dom.register(new CRegistryItem(reg))
		
	def imcRecv(arg: java.util.stream.Stream[InterModComms.IMCMessage]): Unit = {}
	def serverStarting(server: MinecraftServer): Unit = {} //TODO: Dimensions?
}
