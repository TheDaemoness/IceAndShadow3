package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.base.LogicPair;
import mod.iceandshadow3.lib.base.LogicProvider;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.WEntity;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.item.WItemStackOwned;
import mod.iceandshadow3.lib.compat.item.WUsageItem;
import mod.iceandshadow3.lib.compat.item.WUsageItemOnBlock;
import mod.iceandshadow3.lib.compat.world.WWorld;
import mod.iceandshadow3.lib.item.BItemModelProperty;
import mod.iceandshadow3.lib.util.E3vl;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import scala.Function1;
import scala.runtime.BoxedUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AItem extends Item implements LogicProvider.Item {

	private ActionResultType toEActionResult(E3vl in) {
		return in.remap(ActionResultType.SUCCESS, ActionResultType.PASS, ActionResultType.FAIL);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || logic.isShiny(variant, new WItemStack(stack));
	}

	private final BLogicItem logic;
	private final int variant;
	private final LogicPair<BLogicItem> lp;
	public AItem(BLogicItem itemlogic, int variant) {
		super(LogicToProperties$.MODULE$.toProperties(itemlogic, variant));
		logic = itemlogic;
		this.variant = variant;
		lp = new LogicPair<>(itemlogic, variant);
		for(BItemModelProperty bpo : logic.propertyOverrides()) {
			this.addPropertyOverride(new ResourceLocation(IaS3.MODID, bpo.name()), new IItemPropertyGetter() {
				final BItemModelProperty impl = bpo;

				@OnlyIn(Dist.CLIENT)
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
	public LogicPair<BLogicItem> getLogicPair() {
		return lp;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		final boolean mainhand = handIn == Hand.MAIN_HAND;
		final ItemStack is = mainhand?playerIn.getHeldItemMainhand():playerIn.getHeldItemOffhand();
		final WUsageItem context = new WUsageItem(getLogicPair(), is, playerIn, handIn, playerIn.isSneaking());
		final E3vl result = logic.onUseGeneral(variant, context);
		return new ActionResult<>(toEActionResult(result), context.stack().asItemStack());
	}

	@Override
	public void addInformation(
		ItemStack stack,
		@Nullable World worldIn,
		List<ITextComponent> tooltip,
		ITooltipFlag flagIn)
	{
		final String tt = logic.addTooltip(variant, new WItemStack(stack));
		if(!tt.isEmpty()) tooltip.add(new TranslationTextComponent(tt));
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		final String nameOverride = logic.nameOverride(variant, new WItemStack(stack));
		if(nameOverride == null) return super.getTranslationKey(stack);
		else return nameOverride;
	}

	@Override
	@Nonnull
	public ActionResultType onItemUse(ItemUseContext ctxi) {
		final WUsageItemOnBlock context = new WUsageItemOnBlock(getLogicPair(), ctxi);
		final E3vl result = logic.onUseBlock(variant, context);
		return toEActionResult(result);
	}

	@Nullable
	@Override
	public Food getFood() {
		return super.getFood();
	}

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return logic.getBurnTicks(variant, new WItemStack(itemStack));
	}

	@Override
	public void inventoryTick(
		ItemStack is, World world, net.minecraft.entity.Entity owner,
		int slot, boolean held
	) {
		final Function1<WItemStackOwned<WEntity>, BoxedUnit> handler = logic.handlerTickOwned(variant, held);
		//TODO: WItemStack holding owner data is overstaying its welcome.
		if(handler != null) handler.apply(new WItemStackOwned<>(is, CNVEntity.wrap(owner)));
		super.inventoryTick(is, world, owner, slot, held);
	}
}
