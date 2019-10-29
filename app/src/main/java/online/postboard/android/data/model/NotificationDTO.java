
package online.postboard.android.data.model;

public class NotificationDTO extends AnyItem {

    private String content;
    private String createdAt;
    private String id;
    private String nodeIdOfInterest;
    private boolean read;
    private String subContent;
    private String updatedAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        id = id;
    }

    public String getNodeIdOfInterest() {
        return nodeIdOfInterest;
    }

    public void setNodeIdOfInterest(String nodeIdOfInterest) {
        nodeIdOfInterest = nodeIdOfInterest;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        read = read;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        subContent = subContent;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        updatedAt = updatedAt;
    }

}
