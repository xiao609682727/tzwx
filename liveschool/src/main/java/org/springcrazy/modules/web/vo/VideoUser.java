package org.springcrazy.modules.web.vo;

import lombok.Data;

@Data
public class VideoUser {

    private String usedMediaLength;
    private String usableMediaLength;
    private String usedStorage;
    private String usableStorage;
    private String usableFlow;
    private String usedFlow;
    private String expireTime;
    private String status;

    private String largeClassStatus;
    private String videoStatus;
}
