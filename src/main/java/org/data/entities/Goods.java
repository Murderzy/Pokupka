package org.data.entities;

public class Goods {
    private String id;
    private String user_id;
    private float price;

    private String title;
    private String description;
    private String image;



    public String getId() {
        return this.id;
    }

    public String getUser_id() {
        return user_id;
    }

    public float getPrice() {
        return price;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPrice(float price) {
        this.price = price;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
