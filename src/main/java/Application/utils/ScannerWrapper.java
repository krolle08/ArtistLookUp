package Application.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public class ScannerWrapper {
    private Scanner scanner;
    // Boolean indicating if this scanner has been closed
    private boolean closed = false;
    // The input source
    private Readable source;
    // Boolean is true if source is done
    private boolean sourceClosed = false;
    // A holder of the last IOException encountered
    private IOException lastException;

    public ScannerWrapper() {
        this.scanner = new Scanner(System.in);
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