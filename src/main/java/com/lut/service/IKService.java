package com.lut.service;

import com.lut.domain.FileModel;
import com.lut.lucTika.luc.analyzer.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
public class IKService {

    public ArrayList<FileModel> getTopDoc(String key, String indexpathStr, int N) {
        ArrayList<FileModel> hitsList = new ArrayList<FileModel>();
        //检索域
        String[] fields = {"title", "content"};
        Path indexPath = Paths.get(indexpathStr);
        Directory dir;
        try {
            dir = FSDirectory.open(indexPath);
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new IKAnalyzer6x();
            MultiFieldQueryParser parser2 = new
                    MultiFieldQueryParser(fields, analyzer);
            // 查询字符串
            Query query = parser2.parse(key);
            TopDocs topDocs = searcher.search(query, N);
            // 定制高亮标签
            SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style =\"colo r:red; \">", "</span>");
            QueryScorer scoreTitle = new QueryScorer(query, fields[0]);
            Highlighter hlqTitle = new Highlighter(fors, scoreTitle);
            QueryScorer scoreContent = new QueryScorer(query, fields[0]);
            Highlighter hlqContent = new Highlighter(fors, scoreTitle);
            TopDocs hits = searcher.search(query, 100);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                String title = doc.get("title");
                String content = doc.get("content");
                TokenStream tokenStream = TokenSources.getAnyTokenStream
                        (searcher.getIndexReader(), sd.doc, fields[0],
                                new IKAnalyzer6x()); // 获取tokenstream
                Fragmenter fragment = new SimpleSpanFragmenter(scoreTitle);
                hlqTitle.setTextFragmenter(fragment);
                String hl_title = hlqTitle.getBestFragment(tokenStream, title);
                // 获取高亮的片段，可以对其数量进行限制
                tokenStream = TokenSources.getAnyTokenStream(searcher.
                        getIndexReader(), sd.doc, fields[1], new IKAnalyzer6x());
                fragment = new SimpleSpanFragmenter(scoreContent);
                hlqContent.setTextFragmenter(fragment);
                String hl_content = hlqTitle.getBestFragment(tokenStream,
                        content); // 获取高亮的片段，可以对其数量进行限制
                FileModel fm = new FileModel(hl_title != null ? hl_title : title, hl_content != null ? hl_content : content);
                hitsList.add(fm);
            }
            dir.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hitsList;
    }

}
