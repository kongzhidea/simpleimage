package com.kk.simpleimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import org.apache.commons.io.IOUtils;

import com.alibaba.simpleimage.io.ByteArrayOutputStream;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.WriteParameter;
import com.alibaba.simpleimage.render.WriteRender;

/**
 * 类ColorQuantPerfTest.java的实现描述
 *
 * @author wendell 2011-8-18 下午07:06:28
 */
public class ColorQuantPerfTest {

    public static void main(String[] args) throws Exception {
        String appDir = "";
        int threads = 0;
        int times = 0;
        String algName = "";

        appDir = args[0];
        threads = Integer.parseInt(args[1]);
        times = Integer.parseInt(args[2]);
        algName = args[3];

        new ColorQuantPerfTest(appDir, threads, times, algName).start();
    }

    int threadsNum;
    int times;
    String appDir;
    String algName;

    public ColorQuantPerfTest(String appDir, int threads, int times, String algName) {
        this.appDir = appDir;
        this.threadsNum = threads;
        this.times = times;
        this.algName = algName;
    }

    public void start() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(times);
        File rootDir = new File(new File(appDir), "/src/test/resources/conf.test/simpleimage");

        for (int s = 0; s < threadsNum; s++) {
            Thread t = new Thread(new Worker(latch, rootDir, algName));
            t.start();
        }

        latch.await();
    }

    class Worker implements Runnable {
        CountDownLatch latch;
        File imgDir;
        WriteParameter.QuantAlgorithm quantAlg;

        public Worker(CountDownLatch latch, File imgDir, String algName) {
            this.latch = latch;
            this.imgDir = imgDir;
            if ("m".equalsIgnoreCase(algName)) {
                quantAlg = WriteParameter.QuantAlgorithm.MedianCut;
            } else if ("N".equalsIgnoreCase(algName)) {
                quantAlg = WriteParameter.QuantAlgorithm.NeuQuant;
            } else {
                quantAlg = WriteParameter.QuantAlgorithm.OctTree;
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            for (; ; ) {
                if (latch.getCount() > 0) {
                    latch.countDown();
                } else {
                    return;
                }

                List<File> imgs = new ArrayList<File>();
                for (File f : new File(imgDir, "scale").listFiles()) {
                    if (f.isDirectory()) {
                        continue;
                    }
                    imgs.add(f);
                }
                for (File imgFile : imgs) {
                    ImageRender wr = null;
                    InputStream inStream = null;
                    OutputStream outStream = new ByteArrayOutputStream();
                    try {
                        inStream = new FileInputStream(imgFile);
                        ImageRender rr = new ReadRender(inStream);
                        wr = new WriteRender(rr, outStream, ImageFormat.GIF, new WriteParameter(quantAlg));

                        wr.render();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(inStream);
                        IOUtils.closeQuietly(outStream);

                        if (wr != null) {
                            try {
                                wr.dispose();
                            } catch (SimpleImageException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                System.out.println("-----");
            }
        }

    }
}