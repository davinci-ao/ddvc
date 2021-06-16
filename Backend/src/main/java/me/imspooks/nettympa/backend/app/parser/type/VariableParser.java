package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.compiler.VariableCompiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class VariableParser implements Parser<Map<String, Object>> {

    @Override
    public String parse(String view, Map<String, Object> variables) throws IOException {
        Pattern pattern = Pattern.compile("\\{\\{(.*?)}}", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(view);

        // find all sections
        while (matcher.find()) {
            StringBuilder match = new StringBuilder(matcher.group(0));
            String target = matcher.group(1);

            try {
                String result = this.getFromString(target, variables).toString();
                view = view.replace(match.toString(), result);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
    
    private Object getFromString(String input, Map<String, Object> variables) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Compile string to code

        Path work = Mpa.getInstance().getWorkPath();
        Path path = work.resolve("compiled");

        VariableCompiler.compile(path, input, variables);

        List<Object> parameters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            parameters.add(entry.getValue());
        }

        String hashed = "hash_" + Long.toHexString(input.hashCode());

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {work.toUri().toURL()});
        Class<?> cls = Class.forName(String.format("compiled.%s.Compiled", hashed), true, classLoader);

        final List<Class<?>> parameterClasses = new ArrayList<>();
        for (Object parameter : parameters) {
            parameterClasses.add(parameter.getClass());
        }

        return cls.getMethod("parse", parameterClasses.toArray(new Class<?>[0])).invoke(null, parameters.toArray(new Object[0]));
    }
}