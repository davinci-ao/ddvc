package me.imspooks.nettympa.backend.app.view.type;

import lombok.Getter;
import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.app.layout.Layout;
import me.imspooks.nettympa.backend.app.parser.ParserType;
import me.imspooks.nettympa.backend.app.section.Section;
import me.imspooks.nettympa.backend.app.section.type.FileSection;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.View;
import me.imspooks.nettympa.backend.util.ObjectBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class SectionedView implements View {

    private final Set<Section> sections = new HashSet<>();
    private final Layout layout;

    public SectionedView(Layout layout) {
        this.layout = layout;
    }

    /**
     * Returns the current layout used as a template
     *
     * @return Layout instance
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * @param section Register a section
     */
    public void registerSection(Section section) {
        sections.add(section);
    }

    /**
     * @return All registered sections
     */
    public Set<Section> getSections() {
        return sections;
    }

    /**
     * Generate sub sections, overwritten methods must always call its super method
     *
     * @param session Session instance
     */
    @Override
    public void generate(Session session) {
        for (Section section : this.getSections()) {
            if (section instanceof FileSection) {
                ((FileSection) section).generate(session);
            }
        }
    }

    public byte[] parse() throws IOException {
        StringBuilder builder = new StringBuilder();

        if (this.layout != null) {
            if (!Files.exists(this.layout.getLayoutFile())) {
                return new byte[0];
            }

            String layout = new String(Files.readAllBytes(this.layout.getLayoutFile()));

            layout = ParserType.SECTION.getParser().parseInput(layout, this.getSections());
            layout = ParserType.ENVIRONMENT.getParser().parseInput(layout, Mpa.getInstance().getEnvironment());
            // TODO
            return layout.getBytes();
        } else {
            builder.append("Layout doesnt exist");
        }

        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static class Builder implements ObjectBuilder<SectionedView> {

        public static Builder builder() {
            return new SectionedView.Builder();
        }

        @Getter private Session session;
        @Getter private Layout layout;

        @Getter private Consumer<Session> generate = session -> {};

        @Getter private final Set<Section> sections = new HashSet<>();

        public Builder setSession(Session session) {
            this.session = session;
            return this;
        }

        public Builder setLayout(Layout layout) {
            this.layout = layout;
            return this;
        }

        public Builder setGenerate(Consumer<Session> generate) {
            this.generate = generate;
            return this;
        }

        public Builder addSection(Section section) {
            this.sections.add(section);
            return this;
        }

        @Override
        public SectionedView build() {
            SectionedView view = new SectionedView(layout) {
                @Override
                public void generate(Session session) {
                    Builder.this.generate.accept(session);
                }
            };

            for (Section section : this.sections) {
                view.registerSection(section);
            }

            return view;
        }
    }
}