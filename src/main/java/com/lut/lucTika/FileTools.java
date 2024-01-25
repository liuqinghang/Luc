package com.lut.lucTika;

import com.lut.domain.FileModel;
import com.lut.lucTika.luc.analyzer.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 路瞳
 */
public class FileTools {

    public static List<FileModel> extractFile() throws IOException {
        ArrayList<FileModel> list = new ArrayList<FileModel>();
        File fileDir = new File("D:\\work\\javawork\\tika\\testfile");
        File[] allFiles = fileDir.listFiles();
        for (File f : allFiles) {
            FileModel sf = new FileModel(f.getName(), ParserExtraction(f));
            list.add(sf);
        }
        return list;
    }

    public static String ParserExtraction(File file) {
        String fileContent = ""; //接收文档内容
        BodyContentHandler handler = new BodyContentHandler();
        Parser parser = new AutoDetectParser(); //自动解析器接口
        Metadata metadata = new Metadata();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            ParseContext context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
            fileContent = handler.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void praserTest() throws IOException {
        // IK分词器对象
        Analyzer analyzer = new IKAnalyzer6x();
        IndexWriterConfig icw = new IndexWriterConfig(analyzer);
        icw.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = null;
        IndexWriter inWriter = null;
        Path indexPath = Paths.get("D:\\work\\javawork\\tika\\indexPath");
        FieldType fileType = new FieldType();
        fileType.setIndexOptions(IndexOptions.
                DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fileType.setStored(true);
        fileType.setTokenized(true);
        fileType.setStoreTermVectors(true);
        fileType.setStoreTermVectorPositions(true);
        fileType.setStoreTermVectorOffsets(true);
        Date start = new Date(); // 开始时间
        if (!Files.isReadable(indexPath)) {
            System.out.println(indexPath.toAbsolutePath() + "不存在或者不可读，请检查！");
            System.exit(1);
        }
        dir = FSDirectory.open(indexPath);
        inWriter = new IndexWriter(dir, icw);
        ArrayList<FileModel> fileList = (ArrayList<FileModel>) extractFile();
        // 遍历fileList，建立索引
        for (FileModel f : fileList) {
            Document doc = new Document();
            doc.add(new Field("title", f.getTitle(), fileType));
            doc.add(new Field("content", f.getContent(), fileType));
            inWriter.addDocument(doc);
        }
        inWriter.commit();
        inWriter.close();
        dir.close();
        Date end = new Date(); // 结束时间
        // 打印索引耗时
        System.out.println("索引文档完成，共耗时：" + (end.getTime() -
                start.getTime()) + "毫秒．");
    }

}
