package com.online.yugaofan.controller.dom;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.online.yugaofan.cache.PDFCache;
import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.domain.dom.PdfCache;
import com.online.yugaofan.enums.PdfFileActionEnum;
import com.online.yugaofan.utils.pdf.PdfToImgesUtils;
import com.spire.doc.Document;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.widget.PdfPageCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * pdf 文件处理
 */
@Slf4j
@RestController
@RequestMapping("/dom/pdf")
public class PDFController {

    public static final String filePath = "/home/file/pdf/";

    /**
     *
     * @param file 文件
     * @param action 操作
     * @return 结果
     * @throws IOException
     */
    @PostMapping("/upload")
    private BaseResponse upload(@RequestPart("file") MultipartFile file, @RequestParam("action") String action) throws IOException {
        String filename = file.getOriginalFilename();
        String uuid = IdUtil.fastSimpleUUID();
        String rootFilePath = filePath + uuid + ".pdf";
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        Map map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("name", filename);

        PdfCache pdfCache = new PdfCache();
        pdfCache.setFileName(filename);
        pdfCache.setStaus(0);
        // 根据action 处理操作
        switch (PdfFileActionEnum.getPdfFileActionEnum(action)) {
            case toWordDoc: {
                toFile(uuid, rootFilePath, ".doc", FileFormat.DOC, com.spire.doc.FileFormat.Doc);
                pdfCache.setFilePath(pdfCache.getFilePath() + ".doc");
                map.put("nameNew", filename.substring(0, filename.length() - 4) + ".doc");
                pdfCache.setFilePath(rootFilePath.substring(0, rootFilePath.length() - 4)+ ".doc");
                break;
            }
            case toWordDocx: {
                toFile(uuid, rootFilePath, ".docx", FileFormat.DOCX, com.spire.doc.FileFormat.Docx_2013);
                pdfCache.setFilePath(pdfCache.getFilePath() + ".docx");
                map.put("nameNew", filename.substring(0, filename.length() - 4) + ".docx");
                pdfCache.setFilePath(rootFilePath.substring(0, rootFilePath.length() - 4)+ ".docx");
                break;
            }
            case toExcel: {
                toExcel(uuid, ".xlsx", rootFilePath);
                map.put("nameNew", filename.substring(0, filename.length() - 4) + ".xlsx");
                pdfCache.setFilePath(rootFilePath.substring(0, rootFilePath.length() - 4) + ".xlsx");
                break;
            }
            case toImagePng: {
                pdfToImage(uuid, rootFilePath, ".png");
                map.put("nameNew", filename.substring(0, filename.length() - 4) + ".png");
                pdfCache.setFilePath(rootFilePath.substring(0, rootFilePath.length() - 4) + ".png");
                break;
            }
            case toImageJpg: {
                pdfToImage(uuid, rootFilePath, ".jpg");
                map.put("nameNew", filename.substring(0, filename.length() - 4) + ".jpg");
                pdfCache.setFilePath(rootFilePath.substring(0, rootFilePath.length() - 4) + ".jpg");
                break;
            }
            default:
                return BaseResponse.error("操作错误");
        }
        PDFCache.saveParm(uuid, pdfCache);
        return BaseResponse.ok(map);
    }


    /**
     * 将pdf转成图片
     * @param uuid 缓存id
     * @param fileAddress 文件地址 pdf
     * @param type 图片类型
     * @throws IOException
     */
    @Async
    void pdfToImage(String uuid, String fileAddress, String type) throws IOException {
        //加载PDF文件
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(fileAddress);

        // 保存PDF的每一页到图片
        BufferedImage image;
        for (int i = 0; i < doc.getPages().getCount(); i++) {
            image = doc.saveAsImage(i);
            File file = new File(fileAddress.substring(0, fileAddress.length() - 4) + type);
            ImageIO.write(image, type.substring(1), file);
        }
        doc.close();

        updateUuidParam(uuid);
    }

    private void updateUuidParam(String uuid) {
        // 更新uuid 参数
        PdfCache parm = PDFCache.getParm(uuid);
        parm.setStaus(1);
        PDFCache.saveParm(uuid, parm);
    }


    /**
     * 异步转换文件 excel
     * @param rootFilePath 转换文件地址
     */
    @Async
    void toExcel(String uuid, String endPath, String rootFilePath) {
        //创建PdfDocument实例
        PdfDocument pdf = new PdfDocument();
        //加载PDF文档
        pdf.loadFromFile(rootFilePath);
        String desPath = rootFilePath.substring(0, rootFilePath.length() - 4) + endPath;
        //保存为Excel
        pdf.saveToFile(desPath, FileFormat.XLSX);
        pdf.dispose();

        updateUuidParam(uuid);
    }

    /**
     * 异步转换文件
     * @param rootFilePath 转换文件地址
     * @param endPath .doc .docx
     * @param docFormat com.spire.doc.FileFormat.Docx_2013
     */
    @Async
    void toFile(String uuid, String rootFilePath, String endPath, FileFormat fileFormat, com.spire.doc.FileFormat docFormat) {

        boolean resultFlg = false;
        try {
            String desPath = rootFilePath.substring(0, rootFilePath.length() - 4) + endPath;
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(rootFilePath);
            PdfPageCollection num = pdf.getPages();
            if (num.getCount() <= 10) {
                pdf.saveToFile(desPath, fileFormat);
            } else {
                pdf.split(splitPath + "test{0}.pdf", 0);
                File[] fs = getSplitFiles(splitPath);
                for (int i = 0; i < fs.length; i++) {
                    PdfDocument sonpdf = new PdfDocument();

                    Document document = new Document(docPath + "test0" + endPath);
                    for (int j = 1; j < fs.length; j++) {
                        document.insertTextFromFile(docPath + "test" + i + endPath, docFormat);
                    }
                    document.saveToFile(desPath);
                    resultFlg = true;
                    sonpdf.loadFromFile(fs[i].getAbsolutePath());
                    sonpdf.saveToFile(docPath + fs[i].getName().substring(0, fs[i].getName().length() - 4) + endPath, fileFormat);
                }
            }

            updateUuidParam(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultFlg) {
                clearFiles(splitPath);
                clearFiles(docPath);
            }
        }
    }

    /**
     * 文件下载
     * @param uuid 文件地址
     * @param response 响应
     */
    @GetMapping("/file/{uuid}")
    public void getFiles(@PathVariable String uuid, HttpServletResponse response) {
        OutputStream os;
        try {
            PdfCache parm = PDFCache.getParm(uuid);
            if (null != parm) {
                if (parm.getStaus() == 1) {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(parm.getFileName(), "UTF-8"));
                    response.setContentType("application/octet-stream");
                    byte[] bytes = FileUtil.readBytes(parm.getFilePath());
                    os = response.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    final String splitPath = "./split/";

    final String docPath = "./doc/";

    public void clearFiles(String workspaceRootPath){
        File file = new File(workspaceRootPath);
        if(file.exists()){
            deleteFile(file);
        }
    }
    public void deleteFile(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    private File[] getSplitFiles(String path) {
        File f = new File(path);
        File[] fs = f.listFiles();
        if (fs == null) {
            return null;
        }
        return fs;
    }

}
