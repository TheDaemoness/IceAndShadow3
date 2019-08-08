package mod.iceandshadow3.lib.data;

public interface ITextLineRW {
	void fromLine(String line) throws IllegalArgumentException;
	String toLine();
}
