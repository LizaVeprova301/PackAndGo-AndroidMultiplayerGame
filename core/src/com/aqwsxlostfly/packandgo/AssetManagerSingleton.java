//package com.aqwsxlostfly.packandgo;
//
//import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.graphics.Texture;
//
//public class AssetManagerSingleton {
//    private static AssetManagerSingleton assetManagerSingleton;
//
//    private AssetManager assetManager;
//
//    private AssetManagerSingleton() {
//        assetManager = new AssetManager();
//    }
//
//    public static AssetManager getAssetManager() {
//        if (assetManagerSingleton == null) {
//            assetManagerSingleton = new AssetManagerSingleton();
//        }
//        Texture.setAssetManager(assetManagerSingleton.assetManager);
//        return assetManagerSingleton.assetManager;
//    }
//}
