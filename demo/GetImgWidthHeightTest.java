package com.kk.simpleimage;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.render.ReadRender;
import com.kk.util.HttpClientUtil;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class GetImgWidthHeightTest {

    public static ImageWrapper read(InputStream in) throws Exception {
        ImageRender rr = null;
        try {
            rr = new ReadRender(in);

            return rr.render();
        } finally {
            if (rr != null) {
                rr.dispose();
            }
        }
    }

    public static ImageWrapper read(File file) throws Exception {
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

    public static void main(String[] args) throws Exception {
        String file = "D:/data/image/test/1460540712011";
        ImageWrapper wrapper = read(new File(file));
        System.out.println(wrapper.getWidth() + "," + wrapper.getHeight());

        String url = "http://ebydata.oss-cn-beijing.aliyuncs.com/data/images/upload/2016-04-13/1460540712011";
        byte[] _bytes = HttpClientUtil.sendGetReturnByte(url);
        InputStream in = new ByteArrayInputStream(_bytes);
        wrapper = read(in);
        System.out.println(wrapper.getWidth() + "," + wrapper.getHeight());
    }
}
