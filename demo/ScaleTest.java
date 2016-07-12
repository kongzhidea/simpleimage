package com.kk.simpleimage;

import java.io.*;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageWrapper;
import org.apache.commons.io.IOUtils;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteRender;

public class ScaleTest {

    public static void main(String[] args) throws Exception {
        File in = new File("D:\\data\\image\\test\\1463107297591.png");      //原图片
        File out = new File("D:\\data\\image\\test\\1463107297591-caijian.png");       //目的图片
        File out2 = new File("D:\\data\\image\\test\\1463107297591-caijian2.png");       //目的图片
        ScaleParameter scaleParam = new ScaleParameter(500, 500);  //将图像缩略到 a*b以内，不足a*b则不做任何处理

        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        WriteRender wr = null;
        try {
            inStream = new FileInputStream(in);
            outStream = new FileOutputStream(out);
            ImageRender rr = new ReadRender(inStream);
            ImageRender sr = new ScaleRender(rr, scaleParam);
            wr = new WriteRender(sr, outStream);

            wr.render();                            //触发图像处理
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inStream);         //图片文件输入输出流必须记得关闭
            IOUtils.closeQuietly(outStream);
            if (wr != null) {
                try {
                    wr.dispose();                   //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                    // skip ... 
                }
            }
        }

        scale(new FileInputStream(in), scaleParam, out2, null);
    }

    public static void scale(InputStream in, ScaleParameter param, File output, ImageFormat format) throws Exception {
        WriteRender wr = null;
        try {
            ImageRender sr = new ScaleRender(in, param);
            if (format == null) {
                wr = new WriteRender(sr, output);
            } else {
                wr = new WriteRender(sr, output, format);
            }

            wr.render();
        } finally {
            if (wr != null) {
                wr.dispose();
            }
        }
    }
}