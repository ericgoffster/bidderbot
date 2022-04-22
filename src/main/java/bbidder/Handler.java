package bbidder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public final class Handler extends URLStreamHandler {
    /** The classloader to find resources from. */
    private final ClassLoader classLoader;

    public Handler() {
        this.classLoader = getClass().getClassLoader();
    }

    public Handler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String path = u.getPath();
        final URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null) {
            throw new FileNotFoundException(path);
        }
        return resourceUrl.openConnection();
    }
}