package mod.iceandshadow3.compat

import net.minecraftforge.registries.{IForgeRegistry, IForgeRegistryEntry}

class CRegistry[RegistryType <: IForgeRegistryEntry[RegistryType], IaSType <: BLogic](
		registry: IForgeRegistry[RegistryType]) {
	
	protected def register(logic: BLogic, entry: RegistryType): Unit = {
		registry.register(entry)
		logic.secrets.asInstanceOf[SecretsLogic[IaSType,RegistryType]].add(entry)
	}
}