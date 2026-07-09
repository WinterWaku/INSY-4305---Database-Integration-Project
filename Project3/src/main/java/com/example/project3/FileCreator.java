// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileCreator {

    // Appends JSON data to the specified file.
    public void CreateFile(String fileName, String data) throws IOException {
        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(data + ",");
        }
    }
}
