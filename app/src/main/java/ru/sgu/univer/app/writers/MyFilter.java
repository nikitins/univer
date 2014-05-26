package ru.sgu.univer.app.writers;

import java.io.File;
import java.io.FileFilter;

public class MyFilter implements FileFilter {

    boolean findFile = true;
    String fileEnd = "";

    public MyFilter(boolean findFile, String fileEnd) {
        this.findFile = findFile;
        this.fileEnd = fileEnd;
    }

    @Override
    public boolean accept(File file) {
        if (!file.canRead() || !file.canWrite()) {
            return false;
        }

        if (findFile) {
            return file.isDirectory() || file.getName().endsWith(fileEnd);
        } else {
            return file.isDirectory();
        }
    }
}
