package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.item.{WInventoryCrafting, WItemStack}
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.item.LogicCrafting
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.crafting._
import net.minecraft.util.NonNullList
import net.minecraft.world.World

sealed class ECraftingType

object ECraftingType {
	case class About(group: String, output: WItemStack)
	object About {
		def apply(output: WItemStack): About = new About(output.registryName, output)
	}
	case object UNKNOWN extends ECraftingType
	case object CRAFT_SPECIAL extends ECraftingType {
		def apply(logic: LogicCrafting): IRecipe[_] = new SpecialRecipe(IaS3.rloc(logic.name)) {
			override def matches(inv: CraftingInventory, worldIn: World) =
				logic.matches(new WInventoryCrafting(inv), new WWorld(worldIn))
			override def getCraftingResult(inv: CraftingInventory) =
				logic(new WInventoryCrafting(inv)).asItemStack()
			override def canFit(width: Int, height: Int) = logic.fitsIn(width, height)
			override def getRemainingItems(inv: CraftingInventory) =
				CNVCompat.toNonNullList(logic.leftovers(new WInventoryCrafting(inv)))
			override def getSerializer = Registrar.BuiltinRecipeProxy
		}
	} 
	case object CRAFT_SHAPELESS extends ECraftingType {
		def apply(name: String, meta: About, inputs: WIngredient*) = {
			val ingrs = inputs.map(_.expose).toArray
			new ShapelessRecipe(
				IaS3.rloc(name), meta.group,
				meta.output.asItemStack(), NonNullList.from(WIngredient.empty.expose, ingrs:_*)
			)
		}
	}
	case object CRAFT_SHAPED extends ECraftingType {
		def apply(name: String, meta: About, size: ERecipeSize, inputs: WIngredient*) = {
			val ingrs = inputs.map(_.expose).toArray
			new ShapedRecipe(
				IaS3.rloc(name), meta.group,
				size.width, size.height,
				NonNullList.from(WIngredient.empty.expose,  ingrs:_*), meta.output.asItemStack()
			)
		}
	}
	case object COOK_SMELT extends ECraftingType {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 200) = new FurnaceRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack(),
			xp, cooktime
		)
	}
	case object COOK_SMOKE extends ECraftingType {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 100) = new SmokingRecipe(
				IaS3.rloc(name), meta.group,
				input.expose, meta.output.asItemStack(),
				xp, cooktime
			)
	}
	case object COOK_BLAST extends ECraftingType {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 100) = new BlastingRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack(),
			xp, cooktime
		)
	}
	case object STONECUT extends ECraftingType {
		def apply(name: String, meta: About, input: WIngredient): Unit = new StonecuttingRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack()
		)
	}
}
