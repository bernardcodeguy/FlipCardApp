package com.app.flipcardapp;

import androidx.core.content.FileProvider;

public class MyFileProvider extends FileProvider {
    public MyFileProvider() {
        super(R.xml.file_paths);
    }
}
