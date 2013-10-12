package org.unigram.docvalidator.validator.sentence;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.unigram.docvalidator.store.Sentence;
import org.unigram.docvalidator.util.CharacterTable;
import org.unigram.docvalidator.util.ValidationError;
import org.unigram.docvalidator.validator.sentence.SymbolWithSpaceValidator;

class SpaceWithSymbolValidatorForTest extends SymbolWithSpaceValidator {
  void loadCharacterTable (CharacterTable characterTable) {
    this.characterTable = characterTable;
  }
}

public class SpaceWithSymbolValidatorTest {
  @Test
  public void testNotNeedSpace() {
    SpaceWithSymbolValidatorForTest validator = new SpaceWithSymbolValidatorForTest();
    String sampleCharTable = new String(
        "<?xml version=\"1.0\"?>"+
        "<character-table>" +
        "<character name=\"SLASH\" value=\"/\" />" +
        "</character-table>");
    InputStream stream = IOUtils.toInputStream(sampleCharTable);
    CharacterTable characterTable = new CharacterTable(stream);
    validator.loadCharacterTable(characterTable);
    Sentence str = new Sentence("I like apple/orange",0);
    List<ValidationError> errors = validator.check(str);
    assertEquals(0, errors.size());
  }

  @Test
  public void testNeedAfterSpace() {
    SpaceWithSymbolValidatorForTest validator = new SpaceWithSymbolValidatorForTest();
    String sampleCharTable = new String(
        "<?xml version=\"1.0\"?>"+
        "<character-table>" +
        "<character name=\"COLLON\" value=\":\" after-space=\"true\" />" +
        "</character-table>");
    InputStream stream = IOUtils.toInputStream(sampleCharTable);
    CharacterTable characterTable = new CharacterTable(stream);
    validator.loadCharacterTable(characterTable);
    Sentence str = new Sentence("I like her:yes it is.",0);
    List<ValidationError> errors = validator.check(str);
    assertEquals(1, errors.size());
  }

  @Test
  public void testNeedBeforeSpace() {
    SpaceWithSymbolValidatorForTest validator = new SpaceWithSymbolValidatorForTest();
    String sampleCharTable = new String(
        "<?xml version=\"1.0\"?>"+
        "<character-table>" +
        "<character name=\"LEFT_PARENTHEIS\" value=\"(\" invalid-chars=\"（\" before-space=\"true\" />" +
        "</character-table>");
    InputStream stream = IOUtils.toInputStream(sampleCharTable);
    CharacterTable characterTable = new CharacterTable(stream);
    validator.loadCharacterTable(characterTable);
    Sentence str = new Sentence("I like her(Nancy)very much.",0);
    List<ValidationError> errors = validator.check(str);
    assertEquals(1, errors.size());
  }

  @Test
  public void testNeedSpaceInMultiplePostion() {
    SpaceWithSymbolValidatorForTest validator = new SpaceWithSymbolValidatorForTest();
    String sampleCharTable = new String(
        "<?xml version=\"1.0\"?>"+
        "<character-table>" +
        "<character name=\"LEFT_PARENTHEIS\" value=\"(\" before-space=\"true\" />" +
        "<character name=\"RIGHT_PARENTHEIS\" value=\")\" after-space=\"true\" />" +
        "</character-table>");
    InputStream stream = IOUtils.toInputStream(sampleCharTable);
    CharacterTable characterTable = new CharacterTable(stream);
    validator.loadCharacterTable(characterTable);
    Sentence str = new Sentence("I like her(Nancy)very much.",0);
    List<ValidationError> errors = validator.check(str);
    assertEquals(2, errors.size());
  }

  @Test
  public void testReturnOnlyOneForHitBothBeforeAndAfter() {
    SpaceWithSymbolValidatorForTest validator = new SpaceWithSymbolValidatorForTest();
    String sampleCharTable = new String(
        "<?xml version=\"1.0\"?>"+
        "<character-table>" +
        "<character name=\"ASTARISK\" value=\"*\" before-space=\"true\" after-space=\"true\" />" +
        "</character-table>");
    InputStream stream = IOUtils.toInputStream(sampleCharTable);
    CharacterTable characterTable = new CharacterTable(stream);
    validator.loadCharacterTable(characterTable);
    Sentence str = new Sentence("I like 1*10",0);
    List<ValidationError> errors = validator.check(str);
    assertEquals(1, errors.size());
  }
}
