package mod.iceandshadow3;

//Keep this in Java for convenience. This is used by tests written in Java.

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.StatusEffect;
import mod.iceandshadow3.lib.base.LogicCommon;
import mod.iceandshadow3.lib.compat.Registrar;
import mod.iceandshadow3.lib.compat.recipe.AddedRecipesInfo;

import java.util.*;
import java.util.stream.Stream;

/** Static lists of IaS3's content objects.
 * Purged at the end of normal init, or kept indefinitely in tool mode.
 */
public class ContentLists {
	public static final List<BLogicItem> item = new ArrayList<>();
	public static final List<BLogicBlock> block = new ArrayList<>();
	public static final List<StatusEffect> status = new ArrayList<>();
	public static final List<String> namesSound = new ArrayList<>();
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static Optional<AddedRecipesInfo> recipesInfo = Optional.ofNullable(Registrar.recipeInfo());
	// Suppressing the above warning because this field is never serializied,
	// and it's only used in an accessor as a way to prevent redundant wrapping.
	static void purge() {
		item.clear();
		block.clear();
		status.clear();
		namesSound.clear();
		recipesInfo = Optional.empty();
	}
	public static Optional<AddedRecipesInfo> getRecipeInfo() {
		return recipesInfo;
	}
	//TODO: Make this instead a stream of item names (and change the relevant test).
	public static Stream<LogicCommon> logicsWithItems() {
		return Stream.concat(
			item.stream(),
			block.stream().filter(l -> l.itemLogic().isDefined())
		);
	}
}
