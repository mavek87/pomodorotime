package com.matteoveroni.pomodorotime.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalFolderPath {

    private static final Logger log = LoggerFactory.getLogger(ExternalFolderPath.class);

    private static boolean isRunningFromDevelopment;
    private static String DEV_BASE_PATH;
    private static String PROD_BASE_PATH;

    static {
        try {
            Path jarPath = Paths.get(ExternalFolderPath.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            log.info("jarPath: {}", jarPath);
            if (jarPath.toString().endsWith(".jar")) {
                isRunningFromDevelopment = false;
                PROD_BASE_PATH = jarPath.getParent().toString();
                log.info("PROD_BASE_PATH {}", PROD_BASE_PATH);
            } else {
                isRunningFromDevelopment = true;
                DEV_BASE_PATH = "";
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error", ex);
        }
    }

    public static Path getPath(String... pathPiece) throws IOException {
        Path path;
        if (isRunningFromDevelopment) {
            path = Paths.get(DEV_BASE_PATH, pathPiece);
        } else {
            path = Paths.get(PROD_BASE_PATH, pathPiece);
        }
        return path.toAbsolutePath();
    }

    // https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file

//    /**
//     * Gets the base location of the given class.
//     * <p>
//     * If the class is directly on the file system (e.g.,
//     * "/path/to/my/package/MyClass.class") then it will return the base directory
//     * (e.g., "file:/path/to").
//     * </p>
//     * <p>
//     * If the class is within a JAR file (e.g.,
//     * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
//     * path to the JAR (e.g., "file:/path/to/my-jar.jar").
//     * </p>
//     *
//     * @param clazz The class whose location is desired.
//     * @see FileUtils#urlToFile(URL) to convert the result to a {@link File}.
//     */
//    public static URL getLocation(final Class<?> clazz) {
//        if (clazz == null) return null; // could not load the class
//
//        // try the easy way first
//        try {
//            final URL codeSourceLocation = clazz.getProtectionDomain().getCodeSource().getLocation();
//            if (codeSourceLocation != null) return codeSourceLocation;
//        } catch (Exception e) {
//        }
//
//        // NB: The easy way failed, so we try the hard way. We ask for the class
//        // itself as a resource, then strip the class's path from the URL string,
//        // leaving the base path.
//
//        // get the class's raw resource path
//        final URL classResource = clazz.getResource(clazz.getSimpleName() + ".class");
//        if (classResource == null) return null; // cannot find class resource
//
//        final String url = classResource.toString();
//        final String suffix = clazz.getCanonicalName().replace('.', '/') + ".class";
//        if (!url.endsWith(suffix)) return null; // weird URL
//
//        // strip the class's path from the URL string
//        final String base = url.substring(0, url.length() - suffix.length());
//
//        String path = base;
//
//        // remove the "jar:" prefix and "!/" suffix, if present
//        if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);
//
//        try {
//            return new URL(path);
//        } catch (final Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * Converts the given {@link URL} to its corresponding {@link File}.
//     * <p>
//     * This method is similar to calling {@code new File(url.toURI())} except that
//     * it also handles "jar:file:" URLs, returning the path to the JAR file.
//     * </p>
//     *
//     * @param url The URL to convert.
//     * @return A file path suitable for use with e.g. {@link FileInputStream}
//     * @throws IllegalArgumentException if the URL does not correspond to a file.
//     */
//    public static File urlToFile(final URL url) {
//        return url == null ? null : urlToFile(url.toString());
//    }
//
//    /**
//     * Converts the given URL string to its corresponding {@link File}.
//     *
//     * @param url The URL to convert.
//     * @return A file path suitable for use with e.g. {@link FileInputStream}
//     * @throws IllegalArgumentException if the URL does not correspond to a file.
//     */
//    public static File urlToFile(final String url) {
//        String path = url;
//        if (path.startsWith("jar:")) {
//            // remove "jar:" prefix and "!/" suffix
//            final int index = path.indexOf("!/");
//            path = path.substring(4, index);
//        }
//        try {
//            if (PlatformUtils.isWindows() && path.matches("file:[A-Za-z]:.*")) {
//                path = "file:/" + path.substring(5);
//            }
//            return new File(new URL(path).toURI());
//        } catch (Exception e) {
//        }
//
//        if (path.startsWith("file:")) {
//            // pass through the URL as-is, minus "file:" prefix
//            path = path.substring(5);
//            return new File(path);
//        }
//        throw new IllegalArgumentException("Invalid URL: " + url);
//    }
}
