package be.cite.sample.domain;

import java.time.LocalDateTime;

public class MessageDTO {


    private String name;


    private Integer index;


    private Boolean isGood;


    private LocalDateTime date;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getGood() {
        return isGood;
    }

    public void setGood(Boolean good) {
        isGood = good;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "name='" + name + '\'' +
            ", index=" + index +
            ", isGood=" + isGood +
            ", date=" + date +
            '}';
    }
}
