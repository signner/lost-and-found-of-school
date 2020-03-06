
package com.shiro.pojo;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
@Data
public class School implements Serializable {
    @NonNull
    private long id;
    @NonNull
    private String name;
    public School(){}
}
