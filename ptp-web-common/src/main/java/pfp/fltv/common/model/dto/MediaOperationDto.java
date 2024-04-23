package pfp.fltv.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfp.fltv.common.enums.MediaOperationType;

import java.io.File;
import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/23 PM 8:47:36
 * @description 媒体OSS的操作的DTO
 * @filename MediaOperationDto.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaOperationDto implements Serializable {


    private String region;
    private String bucketName;
    private String storePath;
    private File file;
    private MediaOperationType mediaOperationType;


}
