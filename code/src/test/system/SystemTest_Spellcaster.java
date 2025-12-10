package system;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.character.*;
import pcgen.core.spell.Spell;

/**
 * System Test: Simulate spellcasting management.
 * 
 */
public class SystemTest_Spellcaster {
	private SpellBook spellBook;
	private SpellBook powerList;

	private Spell fireball;
	private Spell magicMissile;
	private Spell powerWordScrunch;

	private CDOMObject mockOwner;

	/**
	 * Mock method to simulate adding spells to a caster's spell book.
	 */
	private void addSpellToSpellBook(Spell spell, SpellBook book) {
		CharacterSpell characterSpell = new CharacterSpell(mockOwner, spell);
		characterSpell.addInfo(3, 3, book.getName());
		book.setNumSpells(spellBook.getNumSpells() + 1);
		book.setNumPagesUsed(spellBook.getNumPagesUsed() + 1);
	}

	@BeforeEach
	void setUp() {
		spellBook = new SpellBook("Sum Book", SpellBook.TYPE_SPELL_BOOK);
		powerList = new SpellBook("Powers", SpellBook.TYPE_INNATE_SPELLS);
		spellBook.setNumPages(100);
		spellBook.setNumPagesUsed(0);
		spellBook.setNumSpells(0);
		powerList.setNumSpells(0);

		fireball = Mockito.mock(Spell.class);
		Mockito.when(fireball.getDisplayName()).thenReturn("FIREBALL");

		magicMissile = Mockito.mock(Spell.class);
		Mockito.when(magicMissile.getDisplayName()).thenReturn("MAGIC MISSILE");

		powerWordScrunch = Mockito.mock(Spell.class);
		Mockito.when(powerWordScrunch.getDisplayName()).thenReturn("POWER WORD SCRUNCH");

		mockOwner = Mockito.mock(CDOMObject.class);
		Mockito.when(mockOwner.getDisplayName()).thenReturn("A Freaking Wizard");
	}

	@Test
	void testAddSpell() {
		addSpellToSpellBook(fireball, spellBook);

		assertEquals(1, spellBook.getNumSpells(), "Spell Book should contain 1 spell, the only spell you need (fireball)");
		assertEquals(1, spellBook.getNumPagesUsed(), "Spell Book should have 1 page used.");
		assertEquals(100, spellBook.getNumPages(), "Total pages in spellbook should remain unchanged.");
	}

	@Test
	void testMultipleBooks() {
		addSpellToSpellBook(magicMissile, spellBook);
		addSpellToSpellBook(powerWordScrunch, spellBook);

		addSpellToSpellBook(fireball, powerList);

		assertNotEquals(spellBook.getNumSpells(), powerList.getNumSpells(), "Spell lists should have different number of spells.");
	}
}
