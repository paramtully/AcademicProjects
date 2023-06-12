package model;

import java.lang.reflect.Field;

public abstract class PrintableData {

    public String getHeader() {
        String header = "";
        for (Field x : this.getClass().getDeclaredFields()) {
            header = header + "  " + x.toString().substring(x.toString().lastIndexOf(".") + 1);;
        }
        System.out.println(header);
        return header;
    }

    public abstract String getDataString();

}
