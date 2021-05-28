package com.ssp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class File {
    private String fid;
    private String fileName;
    private String ext;
    private String path;
    private String size;
    private String type;
    private Date uploadTime;
    private String department;
    private String uploadPeople;
}
