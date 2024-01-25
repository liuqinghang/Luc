package com.lut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 路瞳
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileModel {

    private String fileName;

    private Enum fileType;

    private String filePath;

    private String content;

    private String title;

    public FileModel(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
