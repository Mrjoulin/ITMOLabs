package com.joulin.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class ResponseData {
    private List<Map<String, String>> result;
    private Integer requestsCount;

    public String toJson() throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
