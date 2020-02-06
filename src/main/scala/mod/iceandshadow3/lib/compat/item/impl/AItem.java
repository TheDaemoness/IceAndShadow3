package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.base.ProviderLogic;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.id.WId;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.WEntity;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.item.WItemStackOwned;
import mod.iceandshadow3.lib.compat.item.WUsageItem;
import mod.iceandshadow3.lib.compat.item.WUsageItemOnBlock;
import mod.iceandshadow3.lib.compat.world.WWorld;
import mod.iceandshadow3.lib.item.ItemModelProperty;
import mod.iceandshadow3.lib.util.E3vl;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class AItem extends Item implements ProviderLogic.Item {

	private ActionResultType toEActionResult(E3vl in) {
		return in.remap(ActionResultType.SUCCESS, ActionResultType.PASS, ActionResultType.FAIL);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if(!super.hasEffect(stack)) {
			final Predicate<WItemStack> handler = logic.handlerShine();
			if(handler != null) return handler.test(new WItemStack(stack));
			else return false;
		} else return true;
	}

	private final BLogicItem logic;
	public AItem(BLogicItem itemlogic) {
		super(LogicToProperties$.MODULE$.toProperties(itemlogic));
		logic = itemlogic;
		for(ItemModelProperty bpo : logic.propertyOverrides()) {
			this.addPropertyOverride(IaS3.rloc(bpo.name()), new IItemPropertyGetter() {
				final ItemModelProperty impl = bpo;

				@Override
				public float call(@Nonnull ItemStack is, @Nullable World world, @Nullable LivingEntity owner) {
					if(owner != null) return impl.valueOwned(new WItemStackOwned<>(is, CNVEntity.wrap(owner)));
					else {
						final WItemStack wis = new WItemStack(is);
						if(world != null) return impl.valueUnowned(wis, new WWorld(world));
						else return impl.valueUnowned(wis);
					}
				}
			});
		}
	}

	@Nonnull
	@Override
	public BLogicItem getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		final boolean mainhand = handIn == Hand.MAIN_HAND;
		final ItemStack is = mainhand?playerIn.getHeldItemMainhand():playerIn.getHeldItemOffhand();
		final WUsageItem context = new WUsageItem(getLogic(), is, playerIn, handIn, playerIn.isSneaking());
		final E3vl result = logic.onUseGeneral(context);
		return new ActionResult<>(toEActionResult(result), context.stack().expose());
	}

	@Override
	public void addInformation(
		ItemStack stack,
		@Nullable World worldIn,
		List<ITextComponent> tooltip,
		ITooltipFlag flagIn)
	{
		final Function<WItemStack, String> handler = logic.handlerTooltip();
		if(handler != null) {
			final String tt = handler.apply(new WItemStack(stack));
			if(tt != null) tooltip.add(new TranslationTextComponent(tt));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		final String nameOverride = logic.nameOverride(new WItemStack(stack));
		if(nameOverride == null) return super.getTranslationKey(stack);
		else return nameOverride;
	}

	@Override
	@Nonnull
	public ActionResultType onItemUse(ItemUseContext ctxi) {
		final WUsageItemOnBlock context = new WUsageItemOnBlock(getLogic(), ctxi);
		final E3vl result = logic.onUseBlock(context);
		return toEActionResult(result);
	}

	@Nullable
	@Override
	public Food getFood() {
		return super.getFood();
	}

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return logic.getBurnTicks(new WItemStack(itemStack));
	}

	@Override
	public void inventoryTick(
		ItemStack is, World world, net.minecraft.entity.Entity owner,
		int slot, boolean held
	) {
		final Consumer<WItemStackOwned<WEntity>> handler = logic.handlerTickOwned(held);
		//TODO: WItemStack holding owner data is overstaying its welcome.
		if(handler != null) handler.accept(new WItemStackOwned<>(is, CNVEntity.wrap(owner)));
		super.inventoryTick(is, world, owner, slot, held);
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
