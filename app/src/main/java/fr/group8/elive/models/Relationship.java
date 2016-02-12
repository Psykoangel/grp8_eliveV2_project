package fr.group8.elive.models;

/**
 * Created by psyko on 09/02/16.
 */
public class Relationship {
    private String userUniqSrcId;
    private String userUniqTargetId;
    private int relationTypeId;

    public String getUserUniqSrcId() {
        return userUniqSrcId;
    }

    public String getUserUniqTargetId() {
        return userUniqTargetId;
    }

    public int getRelationTypeId() { return relationTypeId; }

    public void setUserUniqSrcId(String userUniqSrcId) { this.userUniqSrcId = userUniqSrcId; }

    public void setUserUniqTargetId(String userUniqTargetId) { this.userUniqTargetId = userUniqTargetId; }

    public void setRelationTypeId(int relationTypeId) {
        this.relationTypeId = relationTypeId;
    }

    @Override
    public String toString() {
        return "relationship [userUniqSrcId="
                + userUniqSrcId
                + ", userUniqTargetId="
                + userUniqTargetId
                + ", relationTypeId="
                + relationTypeId + "]";
    }
}
