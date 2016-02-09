package fr.group8.elive.models;

/**
 * Created by psyko on 09/02/16.
 */
public class Relationship {
    private String userSrcUniqId;
    private String userTargetUniqId;
    private int relationTypeId;

    public String getUserSrcUniqId() {
        return userSrcUniqId;
    }

    public void setUserSrcUniqId(String userSrcUniqId) {
        this.userSrcUniqId = userSrcUniqId;
    }

    public String getUserTargetUniqId() {
        return userTargetUniqId;
    }

    public void setUserTargetUniqId(String userTargetUniqId) {
        this.userTargetUniqId = userTargetUniqId;
    }

    public int getRelationTypeId() {
        return relationTypeId;
    }

    public void setRelationTypeId(int relationTypeId) {
        this.relationTypeId = relationTypeId;
    }

    @Override
    public String toString() {
        return "relationship [userSrcUniqId="
                + userSrcUniqId
                + ", userTargetUniqId="
                + userTargetUniqId
                + ", relationTypeId="
                + relationTypeId + "]";
    }
}
