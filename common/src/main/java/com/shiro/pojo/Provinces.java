package com.shiro.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Provinces implements Serializable {

    private long id;
    private String name;
    private List<Cities> children;//cities
}
