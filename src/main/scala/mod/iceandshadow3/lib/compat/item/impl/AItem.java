package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.item.BItemProperty;
import mod.iceandshadow3.lib.util.LogicPair;
import mod.iceandshadow3.lib.util.ILogicItemProvider;
import mod.iceandshadow3.lib.compat.misc.WNbtTree;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.world.WWorld;
import mod.iceandshadow3.util.E3vl;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AItem extends Item implements ILogicItemProvider {

	private ActionResultType toEActionResult(E3vl in) {
		return in.remap(ActionResultType.SUCCESS, ActionResultType.PASS, ActionResultType.FAIL);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || logic.isShiny(
			variant,
			new WNbtTree(stack.getTag()),
			new WItemStack(stack, null)
		);
	}

	private final BLogicItem logic;
	private final int variant;
	public AItem(BLogicItem itemlogic, int variant) {
		super(itemlogic.toItemProperties(variant));
		logic = itemlogic;
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
		this.variant = variant;
		for(BItemProperty bpo : logic.propertyOverrides()) {
			this.addPropertyOverride(new ResourceLocation(IaS3.MODID, bpo.name()), new IItemPropertyGetter() {
				final BItemProperty impl = bpo;

				@OnlyIn(Dist.CLIENT)
				@Override
				public float call(@Nonnull ItemStack is, @Nullable World world, @Nullable LivingEntity owner) {
					return impl.call(new WItemStack(is, owner), new WWorld(world));
				}
			});
		}
	}

	@Nonnull
	@Override
	public LogicPair<BLogicItem> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		final boolean mainhand = handIn == Hand.MAIN_HAND;
		final ItemStack is = mainhand?playerIn.getHeldItemMainhand():playerIn.getHeldItemOffhand();
		final WItemStack wri = new WItemStack(is, playerIn);
		final WEntityPlayer plai = CNVEntity.wrap(playerIn);
		final E3vl result = logic.onUse(variant, wri.exposeStateData(getLogicPair()), wri, plai, mainhand);
		return new ActionResult<>(toEActionResult(result), wri.exposeItems());
	}

	@Override
	public void addInformation(
		ItemStack stack,
		@Nullable World worldIn,
		List<ITextComponent> tooltip,
		ITooltipFlag flagIn)
	{
		final String tt = logic.addTooltip(variant, new WItemStack(stack, null));
		if(!tt.isEmpty()) tooltip.add(new TranslationTextComponent(tt));
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		final String nameOverride = logic.nameOverride(variant, new WItemStack(stack, null));
		if(nameOverride == null) return super.getTranslationKey(stack);
		else return nameOverride;
	}

	@Nullable
	@Override
	public Food func_219967_s() {
		return super.func_219967_s();
	}
}
