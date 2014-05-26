package ru.sgu.univer.app.objects;

import java.io.File;
import java.io.Serializable;

public class MyFile implements Serializable {
    public File file;
    public boolean back = false;

    public MyFile(File file) {
        this.file = file;
    }

    public MyFile(File file, boolean back) {
        this.file = file;
        this.back = back;
    }

    @Override
    public String toString() {
        if (back) {
            return "..";
        }
        return file.getName();
    }
}
