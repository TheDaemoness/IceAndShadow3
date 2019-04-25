package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.CommonLogic;

public abstract class BBlockLogic extends CommonLogic {
	final BMateria materia;
	protected BBlockLogic(BMateria mat) {
		materia = mat;
	}
	
	protected boolean isOfMateria(BMateria b) {return b == materia;}
}
