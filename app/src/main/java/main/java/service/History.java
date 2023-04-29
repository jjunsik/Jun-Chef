package main.java.service;

import java.util.Date;

public interface History {
    String RecipeName(String name); // 되는 지 안되는 지 모르겠지만 Result에도 있는데 그 값을 쓰면 되지 않을까?
    Date SearchDateTime(Date dateTime);
}
