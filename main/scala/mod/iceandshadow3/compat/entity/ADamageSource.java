package mod.iceandshadow3.compat.entity;

import mod.iceandshadow3.basics.Damage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Damage source.
 * TODO: Split away the IaS additions into a new class that constructs one of these.
 */
public class ADamageSource extends DamageSource {
	
	public static float resistPhysical(float damage, float armor, float toughness) {
		return Math.max(0, damage - armor)/(1+toughness+armor/2);
	}
	
	final Damage damage;
	
	public ADamageSource(final Damage dmg) {
		super("ias3damage");
		this.setDamageIsAbsolute().setDamageBypassesArmor();
		damage = dmg;
		mod.iceandshadow3.basics.DamageForm f = damage.getForm();
		if(f.isProjectile()) this.setProjectile();
		if(f.isBlast()) this.setExplosion();
		if(f.isMystic()) this.setMagicDamage();
	}

	@Override
	public ITextComponent getDeathMessage(EntityLivingBase elb) {
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        //if(I18n.canTranslate(s1)) {
        	return new TextComponentTranslation(s1, elb.getDisplayName(), damage.getSource().getNameTextComponent());
        //} else return new TextComponentTranslation(s, elb.getDisplayName());
	}
}
