package com.joulin.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HitStorage implements Serializable {
    private List<HitResult> hitList = Collections.synchronizedList(new LinkedList<>());

    public boolean add(HitResult hitResult) {
        return hitList.add(hitResult);
    }

    public void clear() {
        hitList.clear();
    }

    public List<Map<String, String>> getMap() {
        return hitList.stream().map(HitResult::getMap).collect(Collectors.toList());
    }
}
