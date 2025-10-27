package pcgen.core.character;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pcgen.base.formula.Formula;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.Equipment;

import static org.junit.jupiter.api.Assertions.*;

public class SpellBookTest {
	@Test
	void testSetEquip() {
		Equipment equip = Mockito.mock(Equipment.class);
		Formula formula = Mockito.mock(Formula.class);
		int mockNumPages = 7;
		Mockito.when(equip.getSafe(IntegerKey.NUM_PAGES)).thenReturn(mockNumPages);
		Mockito.when(equip.getSafe(FormulaKey.PAGE_USAGE)).thenReturn(formula);

		SpellBook book = new SpellBook("Sum Book", SpellBook.TYPE_SPELL_BOOK);
		book.setEquip(equip);

		assertEquals(mockNumPages, book.getNumPages(), "Setting equip should modify numPages.");
		assertSame(formula, book.getPageFormula(), "Setting equip should modify pageFormula.");
	}

	@Test
	void testToString() {
		SpellBook book = new SpellBook("Sum Book", SpellBook.TYPE_SPELL_BOOK);
		int expectedPages = 10;
		int expectedPagesUsed = 3;
		book.setNumPages(expectedPages);
		book.setNumPagesUsed(expectedPagesUsed);

		String expected = "Sum Book [" + expectedPagesUsed + "/" + expectedPages + "]";
		assertEquals(expected, book.toString(), "toString() should show pages used.");
	}

	@Test
	void testIdenticalBooksEqual() {
		Formula formula = Mockito.mock(Formula.class);
		Equipment equip = Mockito.mock(Equipment.class);

		SpellBook book1 = new SpellBook("Sum Book", SpellBook.TYPE_KNOWN_SPELLS);
		SpellBook book2 = new SpellBook("Sum Book", SpellBook.TYPE_KNOWN_SPELLS);

		SpellBook [] books = {book1, book2};

		for(SpellBook book: books) {
			book.setNumPages(100);
			book.setNumPagesUsed(50);
			book.setNumSpells(50);
			book.setPageFormula(formula);
			book.setDescription("i cast fireball bozo");
			book.setEquip(equip);
		}

		assertEquals(book1, book2, "SpellBooks with identical state should be equal.");
		assertEquals(book1.hashCode(), book2.hashCode(), "HashCodes should match for equal objects.");
		assertEquals(book1, book1, "Reflexive equals. If this doesn't work then please contact your reality admin.");
	}

	@Test
	void testClone() {
		Equipment equip = Mockito.mock(Equipment.class);
		Mockito.when(equip.clone()).thenReturn(equip);

		Formula formula = Mockito.mock(Formula.class);

		SpellBook original = new SpellBook("Sum Book", SpellBook.TYPE_SPELL_BOOK);
		original.setNumPages(4);
		original.setNumPagesUsed(2);
		original.setNumSpells(3);
		original.setDescription("d");
		original.setPageFormula(formula);
		original.setEquip(equip);

		SpellBook copy = original.clone();

		assertNotSame(original, copy, "Clone should create a separate instance.");
		assertEquals(original, copy, "Clone should be equal to original.");

		copy.setName("Sum New Book");
		assertNotEquals(original, copy, "Clone should no longer be equal after changing name.");
	}
}
