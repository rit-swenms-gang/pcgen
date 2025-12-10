package system;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.helper.CNAbilitySelection;
import pcgen.core.Language;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.WeaponProf;
import pcgen.core.character.*;
import plugin.bonustokens.Lang;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SystemTest_basic_character {
    
    @Test
    public void testBasicCharacter(){
        // Mock a basic playercharacter
        PlayerCharacter PC = mock(PlayerCharacter.class);
        Object owner = mock(Object.class); 

        //Add various elements to the character
        PCClass PCC = mock(PCClass.class);
        PC.addClass(PCC);

        Language lang = mock(Language.class);
        CDOMObject source = mock(CDOMObject.class);
        PC.addAddLanguage(lang, source);

        CNAbilitySelection cnas = mock(CNAbilitySelection.class);
        PC.addAbility(cnas, owner, source);

        WeaponProf wp = mock(WeaponProf.class);
        PC.addWeaponProf(owner, wp);

        // Verify that the elements were added
        verify(PC).addClass(PCC);
        verify(PC).addAddLanguage(lang, source);
        verify(PC).addAbility(cnas, owner, source);
        verify(PC).addWeaponProf(owner, wp);
        
       


    }

    
    
}
