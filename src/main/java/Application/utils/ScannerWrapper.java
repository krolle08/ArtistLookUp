package Application.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public class ScannerWrapper extends com.sun.tools.javac.parser.Scanner {
    private Scanner scanner;

    public ScannerWrapper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public void close() {
        if (closed)
            return;
        if (source instanceof Closeable) {
            try {
                ((Closeable)source).close();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        sourceClosed = true;
        source = null;
        closed = true;
    }
    // Other methods from Scanner that you might use
}