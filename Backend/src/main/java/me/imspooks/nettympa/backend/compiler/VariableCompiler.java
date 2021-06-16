package me.imspooks.nettympa.backend.compiler;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Nick on 08 jul. 2020.
 * Copyright © ImSpooks
 */
public class VariableCompiler {

    private static Set<Package> packages = new HashSet<>();
    public static boolean DELETE_UNCOMPILED = false;

    public static void addPackages(Package... packages) {
        VariableCompiler.packages.addAll(Arrays.asList(packages));
    }

    public static void compile(Path path, String input, Map<String, Object> variables) throws IOException {
        List<Object> parameters = new ArrayList<>();
        List<String> parameterWithClasses = new ArrayList<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            parameterWithClasses.add(entry.getValue().getClass().getSimpleName() + " " + entry.getKey());
            parameters.add(entry.getValue());
        }

        String hashed = "hash_" + Long.toHexString(input.hashCode());

        path = path.resolve(hashed);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        if (!Files.exists(path.resolve("Compiled.class"))) {
            path = path.resolve("Compiled.java");
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            synchronized (ToolProvider.getSystemJavaCompiler()) {
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

                if (compiler == null) {
                    throw new AssertionError("No JDK found (Are you running on a JRE instead of a JDK?)");
                }

                String source = String.format(
                        "package compiled.%s;\\n" +
                                "%s" +
                                "public class Compiled {\\n" +
                                "   public static String parse(%s) {\\n" +
                                "       return ((Object) %s).toString();\\n" +
                                "   }\\n" +
                                "}",
                        hashed,
                        StringUtils.join(ArrayUtils.addAll(parameters.stream().map(object -> "import " + object.getClass().getName() + ";").toArray(), packages.stream().map(aPackage -> "import " + aPackage.getName() + ".*;").toArray()), System.lineSeparator()) + System.lineSeparator(),
                        StringUtils.join(parameterWithClasses.iterator(), ", "),
                        input
                ).replace("\\n", System.lineSeparator());

                Files.write(path, source.getBytes(StandardCharsets.UTF_8));

                System.out.println("source = " + source);

                int result = compiler.run(null, System.out, System.err, path.toString());
                if (result != 0) {
                    if (DELETE_UNCOMPILED)
                        Files.delete(path);
                    throw new AssertionError("Compilation failed");
                }
                if (DELETE_UNCOMPILED)
                    Files.delete(path);
            }
        }
    }

    /**
     * @return Registered packages for the compiler
     */
    public static Set<Package> getPackages() {
        return packages;
    }
}