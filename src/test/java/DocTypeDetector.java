
import com.lut.lucTika.FileTools;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocTypeDetector {

    @Test
    public static String detectDocTypeUsingDetector(InputStream stream)
            throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }

    @Test
    public void test2() throws IOException,
            TikaException, org.xml.sax.SAXException {
        // 文件路径
        String filepath = "D:\\文档\\pdf\\极客时间\\mysql\\01丨基础架构：一条SQL查询语句是如何执行的？.pdf";
        // 新建File对象
        File pdfFile = new File(filepath);
        // 创建内容处理器对象
        BodyContentHandler handler = new BodyContentHandler();
        // 创建元数据对象
        Metadata metadata = new Metadata();
        // 读入文件
        FileInputStream inputStream = new FileInputStream(pdfFile);
        // InputStream inputStream=TikaInputStream.get(pdfFile);
        // 创建内容解析器对象
        ParseContext parseContext = new ParseContext();
        // 实例化PDFParser对象
        PDFParser parser = new PDFParser();
        //
        OOXMLParser parserXml = new OOXMLParser();
        TXTParser parserTxt = new TXTParser();
        OfficeParser officeParser = new OfficeParser();

        // 调用parse()方法解析文件
        parser.parse(inputStream, handler, metadata, parseContext);
        // 遍历元数据内容
        System.out.println("文件属性信息：");
        for (String name : metadata.names()) {
            System.out.println(name + ":" + metadata.get(name));
        }
        // 打印pdf文件中的内容
        System.out.println("pdf文件中的内容：");
        System.out.println(handler.toString());

    }

    @Test
    public void test3() throws IOException,
            TikaException, org.xml.sax.SAXException {
        {
            Tika tika = new Tika();
            // 新建存放各种文件的files文件夹
            File fileDir = new File("D:\\work\\javawork\\tika\\testfile");
            // 如果文件夹路径错误，退出程序
            if (!fileDir.exists()) {
                System.out.println("文件夹不存在，请检查！");
                System.exit(0);
            }
            // 获取文件夹下的所有文件，存放在File数组中
            File[] fileArr = fileDir.listFiles();
            String filecontent;
            for (File f : fileArr) {
                filecontent = tika.parseToString(f); // 自动解析
                System.out.println("Extracted Content: " + filecontent);
            }
        }
    }

    @Test
    public void test4() throws TikaException, SAXException, IOException {
        // 新建存放各种文件的files文件夹
        File fileDir = new File("D:\\work\\javawork\\tika\\testfile");
        // 如果文件夹路径错误，退出程序
        if (!fileDir.exists()) {
            System.out.println("文件夹不存在，请检查！");
            System.exit(0);
        }
        // 获取文件夹下的所有文件，存放在File数组中
        File[] fileArr = fileDir.listFiles();
        // 创建内容处理器对象
        BodyContentHandler handler = new BodyContentHandler();
        // 创建元数据对象
        Metadata metadata = new Metadata();
        FileInputStream inputStream = null;
        Parser parser = new AutoDetectParser();
        // 自动检测分词器
        ParseContext context = new ParseContext();
        for (File f : fileArr) {
            inputStream = new FileInputStream(f);
            parser.parse(inputStream, handler, metadata, context);
            System.out.println(f.getName() + ":\n" + handler
                    .toString());
        }

    }

    @Test
    public void test5() throws IOException {
        FileTools.praserTest();
    }
}


