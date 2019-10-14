package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.item.{WInventoryCrafting, WItemStack}
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.item.LogicCrafting
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.crafting._
import net.minecraft.util.{NonNullList, ResourceLocation}
import net.minecraft.world.World

//TODO: Do we need to override all the serializers in here?

sealed class ECraftingType

object ECraftingType {
	case class BasicMeta(disambiguation: String, group: String, output: WItemStack) {
		private[recipe] def name(method: String): ResourceLocation = {
			val basename = method + '.' + output.namespace + '.' + output.modName
			new ResourceLocation(IaS3.MODID, if(disambiguation != null) basename+'.'+disambiguation else basename)
		}
	}
	object BasicMeta {
		def apply(name: String, output: WItemStack, group: String): BasicMeta = new BasicMeta(name, group, output)
		def apply(name: String, output: WItemStack): BasicMeta = new BasicMeta(name, output.registryName, output)
		def apply(output: WItemStack): BasicMeta = new BasicMeta(null, output.registryName, output)
		def apply(output: WItemStack, group: String): BasicMeta = new BasicMeta(null, group, output)
	}
	case object UNKNOWN extends ECraftingType
	case object CRAFT_SPECIAL extends ECraftingType {
		def apply(logic: LogicCrafting): Unit = {
			Registrar.addRecipe(new SpecialRecipe(new ResourceLocation(IaS3.MODID, s"dynamic.${logic.name}")) {
				override def matches(inv: CraftingInventory, worldIn: World) =
					logic.matches(new WInventoryCrafting(inv), new WWorld(worldIn))
				override def getCraftingResult(inv: CraftingInventory) =
					logic(new WInventoryCrafting(inv)).asItemStack()
				override def canFit(width: Int, height: Int) = logic.fitsIn(width, height)
				override def getRemainingItems(inv: CraftingInventory) =
					CNVCompat.toNonNullList(logic.leftovers(new WInventoryCrafting(inv)))
				override def getSerializer = Registrar.BuiltinRecipeProxy
			})
		}
	} //TODO: Implement.
	case object CRAFT_SHAPELESS extends ECraftingType {
		def apply(meta: BasicMeta, inputs: WIngredient*): Unit = {
			val ingrs = inputs.map(_.expose).toArray
			Registrar.addRecipe(new ShapelessRecipe(
				meta.name("shapeless"), meta.group,
				meta.output.asItemStack(), NonNullList.from(WIngredient.empty.expose, ingrs:_*)
			))
		}
	}
	case object CRAFT_SHAPED extends ECraftingType {
		def apply(meta: BasicMeta, size: ERecipeSize, inputs: WIngredient*): Unit = {
			val ingrs = inputs.map(_.expose).toArray
			Registrar.addRecipe(new ShapedRecipe(
				meta.name("shaped"), meta.group,
				size.width, size.height,
				NonNullList.from(WIngredient.empty.expose,  ingrs:_*), meta.output.asItemStack()
			))
		}
	}
	case object COOK_SMELT extends ECraftingType {
		def apply(meta: BasicMeta, input: WIngredient, xp: Float = 0f, cooktime: Int = 200): Unit = {
			Registrar.addRecipe(new FurnaceRecipe(
				meta.name("smelting"), meta.group,
				input.expose, meta.output.asItemStack(),
				xp, cooktime
			))
		}
	}
	case object COOK_SMOKE extends ECraftingType {
		def apply(meta: BasicMeta, input: WIngredient, xp: Float = 0f, cooktime: Int = 100): Unit = {
			Registrar.addRecipe(new SmokingRecipe(
				meta.name("smoking"), meta.group,
				input.expose, meta.output.asItemStack(),
				xp, cooktime
			))
		}
	}
	case object COOK_BLAST extends ECraftingType {
		def apply(meta: BasicMeta, input: WIngredient, xp: Float = 0f, cooktime: Int = 100): Unit = {
			Registrar.addRecipe(new BlastingRecipe(
				meta.name("blasting"), meta.group,
				input.expose, meta.output.asItemStack(),
				xp, cooktime
			))
		}
	}
	case object STONECUT extends ECraftingType {
		def apply(meta: BasicMeta, input: WIngredient): Unit = {
			Registrar.addRecipe(new StonecuttingRecipe(
				meta.name("stonecutting"), meta.group,
				input.expose, meta.output.asItemStack()
			))
		}
	}
}
