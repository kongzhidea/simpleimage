package com.kk.simpleimage;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.simpleimage.font.FontManager;
import com.alibaba.simpleimage.render.CornerDrawTextItem;
import com.alibaba.simpleimage.render.DrawTextItem;
import com.alibaba.simpleimage.render.DrawTextParameter;
import com.alibaba.simpleimage.render.DrawTextRender;
import com.alibaba.simpleimage.render.FixDrawTextItem;
import com.alibaba.simpleimage.render.FootnoteDrawTextItem;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.WriteRender;

/**
 * @author wendell
 */
public class DrawTextTest {

    static File path = new File("D:/data/image/test");
    static File rpath = new File("D:/data/image/test/result");

    static final Color FONT_COLOR = new Color(255, 255, 255, 115);
    static final Color FONT_SHADOW_COLOR = new Color(170, 170, 170, 77);
    static final Font FONT = new Font("黑体", Font.PLAIN, 10);

    protected DrawTextParameter getParam() {
        DrawTextParameter param = new DrawTextParameter();
        param.addTextInfo(new FixDrawTextItem("这是测试中文好不好用"));

        return param;
    }

    protected void write(ImageRender dr) throws SimpleImageException, IOException {
        ImageRender wr = new WriteRender(dr, rpath.getCanonicalPath() + File.separator
                + "DRAWTEXT_334.jpg");
        wr.render();
        wr.dispose();
    }

    protected String getFileStr() throws IOException {
        return path.getCanonicalPath() + File.separator + "334.jpg";
    }

    /**
     * Test method for
     * {@link com.alibaba.simpleimage.render.DrawTextRender#DrawTextRender(java.io.InputStream, com.alibaba.simpleimage.render.DrawTextParameter)}
     * .
     */
    public void testDrawTextRenderInputStreamDrawTextParameter() throws Exception {
        InputStream file = new FileInputStream(getFileStr());
        ImageRender dr = new DrawTextRender(file, getParam());
        write(dr);
        file.close();
    }

    /**
     * Test method for
     * {@link com.alibaba.simpleimage.render.DrawTextRender#DrawTextRender(java.io.InputStream, boolean, com.alibaba.simpleimage.render.DrawTextParameter)}
     * .
     */
    public void testDrawTextRenderInputStreamBooleanDrawTextParameter() throws Exception {
        InputStream file = new FileInputStream(getFileStr());
        ImageRender dr = new DrawTextRender(file, true, getParam());
        write(dr);
        file.close();
    }

    // 按照中心点， 左上和 右下  两块  对应画两个一样的 文字。
    public void testCornerDrawTextItem() throws Exception {
        CornerDrawTextItem item = new CornerDrawTextItem("阿里巴巴！");
        doDrawImageText("hydrangeas.jpg", "corner-", item);
    }

    // 中间位置
    public void testFixDrawTextItem() throws Exception {
        // textWidthPercent 文本占宽度大小比例
        // position 文字位置
        FixDrawTextItem item = new FixDrawTextItem("远华贸易实业有限公司", new Color(255, 255, 255, 115), new Color(170, 170, 170, 77),
                FontManager.getFont("方正黑体"), 10, FixDrawTextItem.Position.CENTER, 0.5f);
        doDrawImageText("hydrangeas.jpg", "fix-", item);
    }

    // 底部
    public void testFootnoteDrawTextItem() throws Exception {
        FootnoteDrawTextItem item = new FootnoteDrawTextItem("阿里巴巴网络(中国)有限公司", "cheneychenc.alibaba.com.cn");
        doDrawImageText("hydrangeas.jpg", "footnote-", item);
    }

    public void doDrawImageText(String imageName, String desc, DrawTextItem... items) throws Exception {
        InputStream in = null;
        ImageRender fr = null;

        try {
            in = new FileInputStream(new File(path, imageName));
            ImageRender rr = new ReadRender(in);

            DrawTextParameter dp = new DrawTextParameter();
            if (items != null) {
                for (DrawTextItem itm : items) {
                    if (itm.getFont() == null) {
                        itm.setFont(FONT);
                    }
                    dp.addTextInfo(itm);
                }
            }
            DrawTextRender dtr = new DrawTextRender(rr, dp);

            fr = new WriteRender(dtr, new File(rpath, "DRAWTEXT_" + desc + imageName.substring(0, imageName.lastIndexOf("."))
                    + ".jpg"));
            fr.render();
        } finally {
            IOUtils.closeQuietly(in);
            if (fr != null) {
                fr.dispose();
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        new DrawTextTest().testFootnoteDrawTextItem();
//        new DrawTextTest().testFixDrawTextItem();
        new DrawTextTest().testCornerDrawTextItem();
    }
}