package AI.hotel.bean;

import java.io.Serializable;

public class OrderIdAndImgName implements Serializable {

    private Integer id;
    private Integer house_Id;
    private String recent;

    public Integer getHouse_Id() {
        return house_Id;
    }

    public void setHouse_Id(Integer house_Id) {
        this.house_Id = house_Id;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgName() {
        return recent;
    }

    public void setImgName(String imgName) {
        this.recent = imgName;
    }

}
