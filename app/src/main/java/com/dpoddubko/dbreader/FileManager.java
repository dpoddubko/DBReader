package com.dpoddubko.dbreader;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {

    public void delete(String path){
        File file = new File(path);
        file.delete();
    }

    public boolean isDirExists(String path) {
        File file = new File(path);
        return file.exists();
    }
    public void copyDB(Context context, String inPath, String outPath) {
        InputStream myInput = null;
        FileOutputStream myOutput = null;
        try {
            myInput = context.getAssets().open(inPath);
            File out = new File(outPath);
            myOutput = new FileOutputStream(out);
            IOUtils.copy(myInput, myOutput);
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(myOutput);
            IOUtils.closeQuietly(myInput);
        }
    }
}
