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
package cc.redpen.formatter;

import cc.redpen.RedPenException;
import cc.redpen.model.Document;
import cc.redpen.validator.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Format errors as string messages.
 */
public final class PlainFormatter extends Formatter {
    private static final Logger LOG = LoggerFactory.getLogger(PlainFormatter.class);

    @Override
    public void format(PrintWriter pw, Map<Document, List<ValidationError>> docErrorsMap) throws RedPenException, IOException {
        BufferedWriter writer = new BufferedWriter(new PrintWriter(pw));
        for (Document document : docErrorsMap.keySet()) {
            List<ValidationError> errors = docErrorsMap.get(document);
            for (ValidationError error : errors) {
                writer.write(formatError(document, error));
            }
        }
        try {
            writer.flush();
        } catch (IOException e) {
            LOG.error("failed to flush", e);
        }
    }

    @Override
    public String formatError(Document document, ValidationError error) {
        StringBuilder str = new StringBuilder();
        document.getFileName().ifPresent(e -> str.append(e).append(":"));
        str.append(error.getLineNumber());
        str.append(": Validation" + error.getLevel()).append("[").append(error.getValidatorName()).append("], ");
        str.append(error.getMessage());
        str.append(" at line: ").append(error.getSentence().getContent());
        str.append("\n");
        return str.toString();
    }
}
