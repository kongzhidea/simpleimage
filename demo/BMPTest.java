package com.kk.simpleimage;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.WriteRender;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * 类BMPTest.java的实现描述
 *
 * @author wendell 2011-8-16 下午06:37:24
 */
public class BMPTest {

    static File bmpDir = new File("D:\\data\\image\\test");

    public static File resultDir = new File("D:\\data\\image\\test");

    // 类型转换
    public void doReadWrite(File in, File out, ImageFormat format) throws Exception {
        doReadWrite(in, true, out, format);
    }

    public void doReadWrite(File in, boolean toRGB, File out, ImageFormat format) throws Exception {
        WriteRender wr = null;
        InputStream inStream = new FileInputStream(in);
        try {
            ReadRender rr = new ReadRender(inStream, toRGB);
            wr = new WriteRender(rr, out, format);

            wr.render();
        } finally {
            IOUtils.closeQuietly(inStream);

            if (wr != null) {
                wr.dispose();
            }
        }
    }

    public ImageWrapper read(File file) throws Exception {
        ImageRender rr = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            rr = new ReadRender(in);

            return rr.render();
        } finally {
            IOUtils.closeQuietly(in);
            if (rr != null) {
                rr.dispose();
            }
        }
    }

    public void testBMP2JPG() throws Exception {
        for (File bmpFile : bmpDir.listFiles()) {
            String fileName = bmpFile.getName().toLowerCase();
            if (fileName.endsWith("bmp")) {
                File out = new File(resultDir, "BMP2JPG_" + fileName.substring(0, fileName.indexOf(".")) + ".jpg");
                doReadWrite(bmpFile, out, ImageFormat.JPEG);
            }
        }
    }

    public void testBMP2BMP() throws Exception {
        for (File bmpFile : bmpDir.listFiles()) {
            String fileName = bmpFile.getName().toLowerCase();
            if (fileName.endsWith("bmp")) {
                File out = new File(resultDir, "BMP2BMP_" + fileName);
                doReadWrite(bmpFile, out, ImageFormat.BMP);
            }
        }
    }

    public void testJPG2PNG() throws Exception {
        for (File bmpFile : bmpDir.listFiles()) {
            String fileName = bmpFile.getName().toLowerCase();
            if (fileName.endsWith("jpg")) {
                File out = new File(resultDir, "JPG2PNG_" + fileName.substring(0, fileName.indexOf(".")) + ".png");
                doReadWrite(bmpFile, out, ImageFormat.PNG);
            }
        }
    }

    public void testPNG2JPG() throws Exception {
        for (File bmpFile : bmpDir.listFiles()) {
            String fileName = bmpFile.getName().toLowerCase();
            if (fileName.endsWith("png")) {
                File out = new File(resultDir, "PNG2JPG_" + fileName.substring(0, fileName.indexOf(".")) + ".jpg");
                doReadWrite(bmpFile, out, ImageFormat.PNG);
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        new BMPTest().testBMP2JPG();
        new BMPTest().testPNG2JPG();
    }
}