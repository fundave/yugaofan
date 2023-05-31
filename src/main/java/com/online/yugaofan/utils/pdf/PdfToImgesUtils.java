package com.online.yugaofan.utils.pdf;
import com.spire.pdf.PdfDocument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class PdfToImgesUtils {

    /**
     * 将pdf转成图片
     * @param uuid 缓存id
     * @param fileAddress 文件地址 pdf
     * @param type 图片类型
     * @throws IOException
     */
    public static void pdfToImage(String uuid, String fileAddress, String type) throws IOException {
        //加载PDF文件
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(fileAddress);
        //保存PDF的每一页到图片
        BufferedImage image;
        for (int i = 0; i < doc.getPages().getCount(); i++) {
            image = doc.saveAsImage(i);
            int rgb = image.getRGB(20,5);
            for(int y=0; y<21; y++){
                for(int x=30; x<680; x++){
                    image.setRGB(x,y,rgb);
                }
            }
            File file = new File(fileAddress.substring(0, fileAddress.length() - 4) + type);
            ImageIO.write(image, type, file);
        }
        doc.close();
    }
}

