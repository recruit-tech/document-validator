/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.section;

import cc.redpen.config.Configuration;
import cc.redpen.config.ValidatorConfiguration;
import cc.redpen.model.Paragraph;
import cc.redpen.model.Section;
import cc.redpen.validator.ValidationError;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

public class ParagraphStartWithValidatorTest {
    ParagraphStartWithValidator validator = new ParagraphStartWithValidator();

    @Before
    public void setUp() throws Exception {
        validator.preInit(new ValidatorConfiguration("ParagraphStartWith", singletonMap("start_from", " ")), Configuration.builder().build());
    }

    @Test
    public void startWithoutSpace() {
        assertEquals(1, validateParagraph(new Paragraph().appendSentence("it like a piece of a cake.", 1)).size());
    }

    @Test
    public void startWithSpace() {
        assertEquals(0, validateParagraph(new Paragraph().appendSentence(" it like a piece of a cake.", 1)).size());
    }

    @Test
    public void voidParagraph() {
        assertEquals(0, validateParagraph(new Paragraph()).size());
    }

    private List<ValidationError> validateParagraph(Paragraph paragraph) {
        Section section = new Section(0);
        section.appendParagraph(paragraph);
        List<ValidationError> errors = new ArrayList<>();
        validator.setErrorList(errors);
        validator.validate(section);
        return errors;
    }
}
