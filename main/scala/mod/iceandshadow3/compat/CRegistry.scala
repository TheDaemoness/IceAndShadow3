package mod.iceandshadow3.compat

import mod.iceandshadow3.basics._
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import net.minecraft.item.Item

class CRegistry[RegistryType <: IForgeRegistryEntry[RegistryType], IaSType <: BLogic](
		registry: IForgeRegistry[RegistryType]) {
	
	protected def register[AdapterType](logic: IaSType, entry: RegistryType): IaSType = {
		registry.register(entry)
		logic.secrets.asInstanceOf[SecretsLogic[IaSType,RegistryType]].add(entry)
		logic
	}
}