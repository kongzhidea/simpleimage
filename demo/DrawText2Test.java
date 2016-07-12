package com.kk.simpleimage;

import com.alibaba.simpleimage.CompositeImageProcessor;
import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.io.ByteArrayOutputStream;
import com.alibaba.simpleimage.render.*;
import com.alibaba.simpleimage.util.ImageUtils;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DrawText2Test {

    static String path = "D:/data/image/test";

    static Font DEFAULT_MAIN_TEXT_FONT = new Font("Monospace", 0, 1);
    static Color DEFAULT_MAIN_TEXT_COLOR = new Color(0.7F, 0.7F, 0.7F, 0.50F);
    static Font DEFAULT_FOOT_TEXT_FONT = new Font("arial", 0, 1);
    static ScaleParameter DEFAULT_SCALE_PARAM = new ScaleParameter(1024, 1024);

    public static void main(String[] args) throws Exception {


        CompositeImageProcessor processor = new CompositeImageProcessor();

        File img = new File(path, "1463107297591.png");

        FileInputStream inputStream = null;
        File outputFile = null;
        FileOutputStream outputStream = null;
        InputStream inputToStore = null;

        boolean check = true;
        InputStream memoryStream = null;

        try {
            check = true;
            inputStream = new FileInputStream(img);
            memoryStream = ImageUtils.createMemoryStream(inputStream);

            String suffix = ".jpg";

            if (ImageUtils.isGIF(memoryStream)) {
                suffix = ".gif";
            }
            if (ImageUtils.isPNG(memoryStream)) {
                suffix = ".png";
            }
            if (ImageUtils.isJPEG(memoryStream)) {
                suffix = ".jpg";
            }
            DrawTextParameter dtp = createDrawTextParameter("水印测试", true, true);

            inputToStore = ((ByteArrayOutputStream) processor.process(memoryStream, dtp, DEFAULT_SCALE_PARAM.getMaxWidth(), DEFAULT_SCALE_PARAM.getMaxHeight())).toInputStream();

            String outputName = img.getName().substring(0, img.getName().indexOf("."));

            outputFile = new File(path + "/result", "COMPOSITETEST_" + outputName + suffix);

            outputStream = new FileOutputStream(outputFile);

            IOUtils.copy(inputToStore, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            check = false;
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputToStore);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(memoryStream);
        }
        if (check) {
            check(img, outputFile);
        }
    }

    protected static void check(File src, File dest) throws Exception {
        FileInputStream in = null;
        FileInputStream in2 = null;
        try {
            in = new FileInputStream(dest);
            in2 = new FileInputStream(src);
            ImageRender rr = new ReadRender(in, false);
            ImageRender rr2 = new ReadRender(in2, false);
            ImageWrapper srcImg = rr2.render();
            ImageWrapper dstImg = rr.render();
            BufferedImage dstBi = dstImg.getAsBufferedImage();

            assertTrue(dstBi.getColorModel().getColorSpace().isCS_sRGB());
            assertTrue(dstBi.getWidth() <= 1024);
            assertTrue(dstBi.getHeight() <= 1024);
            if (srcImg.getImageFormat() != ImageFormat.GIF) {
                if (srcImg.getQuality() >= 50) {
                    assertTrue(dstImg.getQuality() == srcImg.getQuality());
                } else {
                    assertTrue(dstImg.getQuality() == 50);
                }

                assertTrue(srcImg.getHorizontalSamplingFactor(0) == dstImg.getHorizontalSamplingFactor(0));
                assertTrue(srcImg.getVerticalSamplingFactor(0) == dstImg.getVerticalSamplingFactor(0));
                assertTrue(srcImg.getHorizontalSamplingFactor(1) == dstImg.getHorizontalSamplingFactor(1));
                assertTrue(srcImg.getVerticalSamplingFactor(1) == dstImg.getVerticalSamplingFactor(1));
                assertTrue(srcImg.getHorizontalSamplingFactor(2) == dstImg.getHorizontalSamplingFactor(2));
                assertTrue(srcImg.getVerticalSamplingFactor(2) == dstImg.getVerticalSamplingFactor(2));
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(in2);
        }
    }

    public static void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    public static DrawTextParameter createDrawTextParameter(String mainTxt, boolean drawMainTxt, boolean drawFootTxt) {
        List<DrawTextItem> textItems = new ArrayList<DrawTextItem>(4);
        if (drawMainTxt) {
            DrawTextItem mainTextItem = new FixDrawTextItem(mainTxt); // fix
            mainTextItem.setFont(new Font("微软雅黑", Font.ITALIC, 12));
            textItems.add(mainTextItem);
        }

        if (drawFootTxt) {
            DrawTextItem footTextItem = new FootnoteDrawTextItem(mainTxt, "www.alibaba.com.cn"); // footnote
            footTextItem.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            textItems.add(footTextItem);
        }

        // relation  xFactor 从左到右  位置百分比,  yFactor从上到下 位置百分比
        // textWidthPercent 文本占宽度比例
        DrawTextItem leftup = new ReleatePositionDrawTextItem("孔智慧",
                Color.RED, new Color(170, 170, 170, 77),
                new Font("微软雅黑", Font.BOLD, 12), 10, 0.05f, 0.25f, 0.15f
        );
        textItems.add(leftup);

        DrawTextParameter dtp = new DrawTextParameter(textItems);

        return dtp;
    }
}
