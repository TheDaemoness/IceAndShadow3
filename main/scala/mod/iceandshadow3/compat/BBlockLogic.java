package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.CommonLogic;
import mod.iceandshadow3.basics.IMateria;

public abstract class BBlockLogic extends CommonLogic {
	final BMateria materia;
	protected BBlockLogic(BMateria mat) {
		materia = mat;
	}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(IMateria b) {return b == materia;}
}
