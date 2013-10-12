package org.unigram.docvalidator.validator.sentence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unigram.docvalidator.store.Sentence;
import org.unigram.docvalidator.util.CharacterTable;
import org.unigram.docvalidator.util.ValidationError;
import org.unigram.docvalidator.util.Configuration;
import org.unigram.docvalidator.util.DocumentValidatorException;
import org.unigram.docvalidator.validator.SentenceValidator;

/**
 * Validate input sentences contain more charcters more than specified.
 */
public class SentenceLengthValidator implements SentenceValidator {

  public List<ValidationError> check(Sentence line) {
    List<ValidationError> result = new ArrayList<ValidationError>();
    if (line.content.length() > maxLength) {
      result.add(new ValidationError(line.position,
          "The length of the line exceeds the maximum "
          + String.valueOf(line.content.length())
          + " in line: " + line.content));
    }
    return result;
  }

  public SentenceLengthValidator() {
    super();
    this.maxLength = DEFAULT_MAX_LENGTH;
  }

  public boolean initialize(Configuration conf, CharacterTable characterTable)
        throws DocumentValidatorException {
    if (conf.getAttribute("max_length") == null) {
      this.maxLength = DEFAULT_MAX_LENGTH;
      LOG.info("max_length was not set.");
      LOG.info("Using the default value of max_length.");
    } else {
      this.maxLength = Integer.valueOf(conf.getAttribute("max_length"));
    }
    return true;
  }

  private static Logger LOG =
      LoggerFactory.getLogger(SentenceLengthValidator.class);

  private int maxLength;

  private static final int DEFAULT_MAX_LENGTH = 30;
}
