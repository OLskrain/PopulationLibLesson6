package com.example.olskr.populationliblesson6.mvp.model.cache;

import android.graphics.Bitmap;

import com.example.olskr.populationliblesson6.App;
import com.example.olskr.populationliblesson6.mvp.model.entity.realm.CachedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.realm.Realm;
import timber.log.Timber;

public class ImageCache { //кеш картинки на Realm
    private static final String IMAGE_FOLDER_NAME = "image";

    public static File getFile(String url) {
        //делаем запрос по url и берем первый
        CachedImage cachedImage = Realm.getDefaultInstance().where(CachedImage.class).equalTo("url", url).findFirst();

        if (cachedImage != null) {
            return new File(cachedImage.getPath()); //если нашлось, то дерем картинку по этому пути
        }
        return null;
    }

    public static boolean contains(String url) { //проверяем сть ли там вообще картинка по url
        return Realm.getDefaultInstance().where(CachedImage.class).equalTo("url", url).count() > 0;
    }

    public static void clear() { //очистить кеш
        Realm.getDefaultInstance().executeTransaction(realm -> realm.delete(CachedImage.class));
        deleteFileOrDirRecursive(getImageDir());
    }

    public static File saveImage(final String url, Bitmap bitmap) { //сохрашение картинки
        if (!getImageDir().exists() && !getImageDir().mkdirs()) { //смотрим есть ли путь к картинки, если нет создаем
            throw new RuntimeException("Failed to create directory: " + getImageDir().toString()); //если нет и не создать, но кидаем ошибку
        }

        final String fileFormat = url.contains(".jpg") ? ".jpg" : ".png"; //вычисляем вормат файлов
        final File imageFile = new File(getImageDir(), SHA1(url) + fileFormat);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imageFile);
            //в зависимости от формата записываем
            bitmap.compress(fileFormat.equals("jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.d("Failed to save image");
            return null;
        }

        //дальше записываем в реалм
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            CachedImage cachedImage = new CachedImage();
            cachedImage.setUrl(url);
            cachedImage.setPath(imageFile.toString());
            realm.copyToRealm(cachedImage);
        });

        return imageFile;
    }

    public static File getImageDir() { //куда класть картинки
        return new File(App.getInstance().getExternalFilesDir(null) + "/" + IMAGE_FOLDER_NAME);
    }

    public static String SHA1(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public static float getSizeKb() { //чтобы посчитать размер всего кеша
        return getFileOrDirSize(getImageDir()) / 1024f;
    }

    public static void deleteFileOrDirRecursive(File fileOrDirectory) { //рекурсивное удаление файла
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteFileOrDirRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static long getFileOrDirSize(File f) { //получить размер кеша
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFileOrDirSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }
}
