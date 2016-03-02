/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.sentence;

import cc.redpen.RedPenException;
import cc.redpen.model.Sentence;
import cc.redpen.tokenizer.TokenElement;
import cc.redpen.validator.ExpressionRule;
import cc.redpen.validator.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Detect double negative expressions in Japanese texts.
 */
public class DoubleNegativeValidator extends Validator {
    private static final String DEFAULT_RESOURCE_EXPRESSION_PATH =
            "default-resources/double-negative/double-negative-expression-";
    private static final String DEFAULT_RESOURCE_WORD_PATH =
            "default-resources/double-negative/double-negative-word-";
    private Set<ExpressionRule> invalidExpressions;
    private Set<String> negativeWords;

    @Override
    public void validate(Sentence sentence) {
        // validate with expressions (phrase)
        for (ExpressionRule rule : invalidExpressions) {
            if (rule.match(sentence.getTokens())) {
                addLocalizedError(sentence, rule.toString());
                return;
            }
        }

        // validate with set of negative words
        int count = 0;
        for (String negativeWord : negativeWords) {
            List<TokenElement> tokens = sentence.getTokens();
            for (TokenElement token : tokens) {
                if (token.getSurface().toLowerCase().equals(negativeWord)) {
                    count++;
                }
                if (count >= 2) {
                    addLocalizedErrorFromToken(sentence, token);
                    return;
                }
            }
        }
    }

    @Override
    protected void init() throws RedPenException {
        invalidExpressions = RULE.loadCachedFromResource(
                DEFAULT_RESOURCE_EXPRESSION_PATH + getSymbolTable().getLang() + ".dat",
                "double negative expression rules");

        negativeWords = WORD_LIST_LOWERCASED.loadCachedFromResource(
                DEFAULT_RESOURCE_WORD_PATH + getSymbolTable().getLang() +".dat",
                "double negative words");
    }

    @Override
    public List<String> getSupportedLanguages() {
        return asList(Locale.JAPANESE.getLanguage(), Locale.ENGLISH.getLanguage());
    }
}
