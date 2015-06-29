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

package cc.redpen.parser;

import cc.redpen.RedPenException;
import cc.redpen.model.Document;
import cc.redpen.tokenizer.RedPenTokenizer;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.RedPenTreeProcessor;
import org.pegdown.ParsingTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Parser for AsciiDoc fasormat, utilizing AsciiDoctorJ.<br/>
 * <p/>
 * AsciiDoc's syntax and grammar is documented at @see http://asciidoc.org/
 */
public class AsciiDocParser extends BaseDocumentParser {
    private static final Logger LOG = LoggerFactory.getLogger(AsciiDocParser.class);

    @Override
    public Document parse(InputStream io, Optional<String> fileName, SentenceExtractor sentenceExtractor, RedPenTokenizer tokenizer) throws RedPenException {
        Document.DocumentBuilder documentBuilder = new Document.DocumentBuilder(tokenizer);
        fileName.ifPresent(documentBuilder::setFileName);
        BufferedReader reader = createReader(io);
        try {
            Asciidoctor asciidoctor = Asciidoctor.Factory.create();
            InputStream rubySource = new ByteArrayInputStream(AsciiDoctorRedPenRubySource.SOURCE_TEXT.getBytes("UTF-8"));
            asciidoctor.rubyExtensionRegistry().loadClass(rubySource);
            Map<String, Object> options = new HashMap<>();
            options.put("backend", "redpen");
            options.put("sourcemap", true);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("sourcemap", true);
            options.put("attributes", attributes);

            // Register our documentbuilding TreeProcessor
            asciidoctor.javaExtensionRegistry().treeprocessor(new RedPenTreeProcessor(documentBuilder, sentenceExtractor, options));

            try {
                // trigger the tree processor, which will consequently fill the documentBuilder

                asciidoctor.readDocumentStructure(reader, options);
            } catch (Exception e) {
                LOG.error("Asciidoctor parser error: " + e.getMessage());
            }

        } catch (ParsingTimeoutException e) {
            throw new RedPenException("Failed to parse timeout: ", e);
        } catch (Exception e) {
            throw new RedPenException("Exception when configuring AsciiDoctor", e);
        }

        return documentBuilder.build();
    }
}

class AsciiDoctorRedPenRubySource {
    public static String SOURCE_TEXT = "# encoding: UTF-8\n" +
            "module Asciidoctor\n" +
            "class Converter::RedPen < Converter::BuiltIn\n" +

            "def initialize backend, opts = {}\n" +
            "end\n" +

            "def redpen_output node, opts = {}\n" +
            "  location=''\n" +
            "  if defined?(node.source_location) && (node.source_location != nil)\n" +
            "    location = \"\\001#{node.source_location.lineno}\\002\"\n" +
            "  end\n" +
            "  content = defined?(node.content) ? node.content : (defined?(node.text) ? node.text : '')\n" +
            "  \"#{location}\\003#{content}\\004\"\n" +
            "end\n" +

            "alias paragraph redpen_output\n" +
            "alias document redpen_output\n" +
            "alias embedded redpen_output\n" +
            "alias outline redpen_output\n" +
            "alias section redpen_output\n" +
            "alias admonition redpen_output\n" +
            "alias audio redpen_output\n" +
            "alias colist redpen_output\n" +
            "alias dlist redpen_output\n" +
            "alias example redpen_output\n" +
            "alias floating_title redpen_output\n" +
            "alias image redpen_output\n" +
            "alias listing redpen_output\n" +
            "alias literal redpen_output\n" +
            "alias stem redpen_output\n" +
            "alias olist redpen_output\n" +
            "alias open redpen_output\n" +
            "alias page_break redpen_output\n" +
            "alias preamble redpen_output\n" +
            "alias quote redpen_output\n" +
            "alias thematic_break redpen_output\n" +
            "alias sidebar redpen_output\n" +
            "alias table redpen_output\n" +
            "alias toc redpen_output\n" +
            "alias ulist redpen_output\n" +
            "alias verse redpen_output\n" +
            "alias video redpen_output\n" +
            "alias inline_anchor redpen_output\n" +
            "alias inline_break redpen_output\n" +
            "alias inline_button redpen_output\n" +
            "alias inline_callout redpen_output\n" +
            "alias inline_footnote redpen_output\n" +
            "alias inline_image redpen_output\n" +
            "alias inline_indexterm redpen_output\n" +
            "alias inline_kbd redpen_output\n" +
            "alias inline_menu redpen_output\n" +
            "alias inline_quoted redpen_output\n" +
            "end\n" +

            "Asciidoctor::Converter::Factory.register Asciidoctor::Converter::RedPen, [\"redpen\"]\n" +

            // need to prevent AsciiDoctor's substitutors from replacing quotes with fancy-quotes, and (c) with the &copy; etc
            "Substitutors::SUBS[:basic]=[:specialcharacters]\n" +
            "Substitutors::SUBS[:normal]=[:specialcharacters, :quotes, :attributes,  :macros, :post_replacements]\n" +
            "Substitutors::SUBS[:verbatim]=[:specialcharacters, :callouts]\n" +
            "Substitutors::SUBS[:title]=[:specialcharacters, :quotes, :macros, :attributes, :post_replacements]\n" +
            "Substitutors::SUBS[:header]=[:specialcharacters, :attributes]\n" +
            "end\n";
}
