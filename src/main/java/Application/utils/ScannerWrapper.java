package Application.utils;

import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles Userinput
 */
@Component
public class ScannerWrapper implements UserInputReader {
    private final Scanner scanner;
    public ScannerWrapper(Readable source) {
        this.scanner = new Scanner(source);
    }
    public ScannerWrapper() {
        this.scanner = new Scanner(System.in);
    }

    public void close() {
        scanner.close();
    }

    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }
}