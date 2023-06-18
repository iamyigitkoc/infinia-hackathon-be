package com.infinia.scrollup.driver.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinia.scrollup.driver.dto.ScrollUpVideoItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScrollUpClientResponse implements Serializable {

    private int count;

    private boolean play;

    private boolean loop;

    private int playingIndex;

    private String brightness;

    private List<ScrollUpVideoItem> playList;
}
