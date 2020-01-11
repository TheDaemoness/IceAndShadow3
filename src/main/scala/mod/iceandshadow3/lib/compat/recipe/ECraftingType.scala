package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.inventory.WInventoryCrafting
import mod.iceandshadow3.lib.compat.file.RecipeUnlockGen
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
	protected val constructor: RecipeBuilderCooking.RecipeMaker
	def apply(output: => BCraftResult, input: IngredientFactory): RecipeBuilderCooking =
		new RecipeBuilderCooking(this, output, defaultTicks, input, constructor)
}

object ECraftingType {
	case object UNKNOWN extends ECraftingType("")
	case object CRAFT_SPECIAL extends ECraftingType("dynamic") {
		def register(logic: LogicCrafting) = Registrar.addRecipeFactory(new RecipeFactory(
			NewRecipeMetadata.dynamic(this, s"$name.${logic.name}"),
			_ => new SpecialRecipe(IaS3.rloc(logic.name)) {
				override def matches(inv: CraftingInventory, worldIn: World) =
					logic.matches(new WInventoryCrafting(inv), new WWorld(worldIn))
				override def getCraftingResult(inv: CraftingInventory) =
					logic(new WInventoryCrafting(inv)).asItemStack()
				override def canFit(width: Int, height: Int) = logic.fitsIn(width, height)
				override def getRemainingItems(inv: CraftingInventory) =
					CNVCompat.toNonNullList(logic.leftovers(new WInventoryCrafting(inv)))
				override def getSerializer = Registrar.RecipeHandler
			},
			RecipeUnlockGen.none(),
			Seq.empty
		))
	} 
	case object CRAFT_SHAPELESS extends ECraftingType("combine") {
		def apply(output: => BCraftResult, inputs: IngredientFactory*) =
			new RecipeBuilder(this, output) {
				override protected def factory(nrm: NewRecipeMetadata) = new RecipeFactory(
					nrm,
					ingrs => new ShapelessRecipe(
						IaS3.rloc(nrm.name), nrm.group, nrm.result.asItemStack(),
						NonNullList.from(Ingredient.EMPTY, ingrs:_*)
					),
					unlock,
					inputs
				)
			}
	}
	case object CRAFT_SHAPED extends ECraftingType("craft") {
		def apply(output: => BCraftResult, size: ERecipeSize, inputs: IngredientFactory*): RecipeBuilder =
			new RecipeBuilder(this, output) {
				override protected def factory(nrm: NewRecipeMetadata) = new RecipeFactory(
					nrm,
					ingrs => new ShapedRecipe(
						IaS3.rloc(nrm.name), nrm.group, size.width, size.height,
						NonNullList.from(Ingredient.EMPTY, ingrs:_*),
						nrm.result.asItemStack()
					),
					unlock,
					inputs
				)
			}
		/** Create a builder for a recipe that uses n items of the same type in a certain basic shape. */
		def apply(output: BCraftResult, input: IngredientFactory, size: ERecipeSize): RecipeBuilder =
			apply(output, size, Seq.fill(size.size)(input):_*)
	}
	case object COOK_SMELT extends ECraftingTypeCooking("smelt") {
		override val defaultTicks = 200
		override protected val constructor = new FurnaceRecipe(_, _, _, _, _, _)
	}
	case object COOK_SMOKE extends ECraftingTypeCooking("smoke") {
		override val defaultTicks = 100
		override protected val constructor = new SmokingRecipe(_, _, _, _, _, _)
	}
	case object COOK_BLAST extends ECraftingTypeCooking("blast") {
		override val defaultTicks = 100
		override protected val constructor = new BlastingRecipe(_, _, _, _, _, _)
	}
	case object STONECUT extends ECraftingType("cut") {
		def apply(output: => BCraftResult, input: IngredientFactory): RecipeBuilder =
			new RecipeBuilder(this, output) {
				override protected def factory(nrm: NewRecipeMetadata) = new RecipeFactory(
					nrm,
					ins => new StonecuttingRecipe(
						IaS3.rloc(nrm.name), nrm.group, ins.head, nrm.result.asItemStack()
					),
					unlock,
					Seq(input)
				)
			}
	}

	/** Register recipes to create a larger item from several smaller items, or smaller items from that larger item. */
	def registerAB(
		smaller: IngredientFactory, larger: IngredientFactory, size: ERecipeSize = ERecipeSize.THREE_X_THREE
	) = {
		val resultS = smaller.toResult
		val resultL = larger.toResult
		; {
			val idS = resultS.id
			ECraftingType.CRAFT_SHAPED(resultL, smaller, size).suffix(
				s"ab_from.${idS.namespace}.${idS.name}"
			).unlockDeduce.register()
		}
		; {
			val idL = resultL.id
			ECraftingType.CRAFT_SHAPELESS(resultS, larger).suffix(
				s"ab_from.${idL.namespace}.${idL.name}"
			).alterResult(_.setCount(size.size)).unlockDeduce.register()
		}
	}
}
