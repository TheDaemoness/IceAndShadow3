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

sealed abstract class ECraftingType(val name: String)
sealed abstract class ECraftingTypeCooking(name: String) extends ECraftingType(name) {
	val defaultTicks: Int
	protected val constructor: BRecipeBuilderCooking.RecipeMaker
	def apply(output: BCraftResult, input: IngredientFactory): BRecipeBuilderCooking =
		new BRecipeBuilderCooking(this, output, defaultTicks, input, constructor)
}

object ECraftingType {
	case class About(group: String, output: WItemStack)
	object About {
		def apply(output: WItemStack): About = new About(output.registryName, output)
	}
	case object UNKNOWN extends ECraftingType("")
	case object CRAFT_SPECIAL extends ECraftingType("dynamic") {
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
		def register(logic: LogicCrafting) = Registrar.addRecipeFactory(new RecipeFactory(
			RecipeMetadata.dynamic(this, s"$name.${logic.name}"),
			_ => new SpecialRecipe(IaS3.rloc(logic.name)) {
				override def matches(inv: CraftingInventory, worldIn: World) =
					logic.matches(new WInventoryCrafting(inv), new WWorld(worldIn))
				override def getCraftingResult(inv: CraftingInventory) =
					logic(new WInventoryCrafting(inv)).asItemStack()
				override def canFit(width: Int, height: Int) = logic.fitsIn(width, height)
				override def getRemainingItems(inv: CraftingInventory) =
					CNVCompat.toNonNullList(logic.leftovers(new WInventoryCrafting(inv)))
				override def getSerializer = Registrar.BuiltinRecipeProxy
			}
		).withNoUnlock())
	} 
	case object CRAFT_SHAPELESS extends ECraftingType("combine") {
		def apply(name: String, meta: About, inputs: WIngredient*) = {
			val ingrs = inputs.map(_.expose).toArray
			new ShapelessRecipe(
				IaS3.rloc(name), meta.group,
				meta.output.asItemStack(), NonNullList.from(WIngredient.empty.expose, ingrs:_*)
			)
		}
		def apply(output: BCraftResult, inputs: IngredientFactory*) =
			new BRecipeBuilder(this, output) {
				override protected def factory(nrm: RecipeMetadata) = new RecipeFactory(
					nrm,
					ingrs => new ShapelessRecipe(
						IaS3.rloc(nrm.name), nrm.group, output.toItemStack.asItemStack(),
						NonNullList.from(Ingredient.EMPTY, ingrs:_*)
					),
					inputs:_*
				)
			}
	}
	case object CRAFT_SHAPED extends ECraftingType("craft") {
		def apply(name: String, meta: About, size: ERecipeSize, inputs: WIngredient*) = {
			val ingrs = inputs.map(_.expose).toArray
			new ShapedRecipe(
				IaS3.rloc(name), meta.group,
				size.width, size.height,
				NonNullList.from(WIngredient.empty.expose,  ingrs:_*), meta.output.asItemStack()
			)
		}
		def apply(output: BCraftResult, size: ERecipeSize, inputs: IngredientFactory*) =
			new BRecipeBuilder(this, output) {
				override protected def factory(nrm: RecipeMetadata) = new RecipeFactory(
					nrm,
					ingrs => new ShapedRecipe(
						IaS3.rloc(nrm.name), nrm.group, size.width, size.height,
						NonNullList.from(Ingredient.EMPTY, ingrs:_*),
						output.toItemStack.asItemStack()
					),
					inputs:_*
				)
			}
	}
	case object COOK_SMELT extends ECraftingTypeCooking("smelt") {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 200) = new FurnaceRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack(),
			xp, cooktime
		)
		override val defaultTicks = 200
		override protected val constructor = new FurnaceRecipe(_, _, _, _, _, _)
	}
	case object COOK_SMOKE extends ECraftingTypeCooking("smoke") {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 100) = new SmokingRecipe(
				IaS3.rloc(name), meta.group,
				input.expose, meta.output.asItemStack(),
				xp, cooktime
			)
		override val defaultTicks = 100
		override protected val constructor = new SmokingRecipe(_, _, _, _, _, _)
	}
	case object COOK_BLAST extends ECraftingTypeCooking("blast") {
		def apply(name: String, meta: About, input: WIngredient, xp: Float = 0f, cooktime: Int = 100) = new BlastingRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack(),
			xp, cooktime
		)
		override val defaultTicks = 100
		override protected val constructor = new BlastingRecipe(_, _, _, _, _, _)
	}
	case object STONECUT extends ECraftingType("cut") {
		def apply(name: String, meta: About, input: WIngredient): Unit = new StonecuttingRecipe(
			IaS3.rloc(name), meta.group,
			input.expose, meta.output.asItemStack()
		)
		def apply(output: BCraftResult, input: IngredientFactory): BRecipeBuilder =
			new BRecipeBuilder(this, output) {
				override protected def factory(nrm: RecipeMetadata) = new RecipeFactory(
					nrm,
					ins => new StonecuttingRecipe(
						IaS3.rloc(nrm.name), nrm.group, ins.head, nrm.result.get.asItemStack()
					),
					input
				)
			}
	}
}
