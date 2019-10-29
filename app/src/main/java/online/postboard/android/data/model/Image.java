
package online.postboard.android.data.model;


import java.io.Serializable;

public class Image implements Serializable {

    private double heightByWidth;
    private String imageURL;
    private String imageURLThumbnail;

    public Double getHeightByWidth() {
        return heightByWidth;
    }

    public void setHeightByWidth(Double heightByWidth) {
        this.heightByWidth = heightByWidth;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURLThumbnail() {
        return imageURLThumbnail;
    }

    public void setImageURLThumbnail(String imageURLThumbnail) {
        this.imageURLThumbnail = imageURLThumbnail;
    }

}