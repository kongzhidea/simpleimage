package com.kk.simpleimage;

import com.alibaba.simpleimage.util.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageTest {
    static String path = "D:/data/image/test";

    public static void main(String[] args) throws Exception {
//        File file = new File(path, "hydrangeas.jpg");
        File file = new File(path, "54866b9cb21bd.bmp");

        InputStream in = new FileInputStream(file);
        in = ImageUtils.createMemoryStream(in);

        System.out.println(ImageUtils.isJPEG(in));
        System.out.println(ImageUtils.isPNG(in));
        System.out.println(ImageUtils.isGIF(in));
        System.out.println(ImageUtils.isBMP(in));



    }
}
