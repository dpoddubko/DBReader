package com.dpoddubko.dbreader;

public class Database {

    private String mAssetPath;
    private String mBaseName;
    private String mButtonName;


    public Database(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        mBaseName = components[components.length - 1];
        String[] namePart = mBaseName.split("\\.");
        mButtonName = namePart[0];
    }

    public String getBaseName() {
        return mBaseName;
    }

    public String getButtonName() {
        return mButtonName;
    }

    public String getAssetPath() {
        return mAssetPath;
    }
}
