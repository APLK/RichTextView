package example.ricktextview.view.richtext;

import java.io.Serializable;

/**
 * 用户model
 */

public class UserModel implements Serializable {

    public UserModel() {

    }

    public UserModel(String user_name, int user_id, int type) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.type = type;
    }

    public UserModel(String user_name, int user_id) {
        this.user_name = user_name;
        this.user_id = user_id;
    }

    /**
     * 名字不能带@和空格
     */
    private String user_name;

    private int user_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return this.user_name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserModel userModel = (UserModel) o;

        if (user_id != userModel.user_id)
            return false;
        if (type != userModel.type)
            return false;
        return user_name.equals(userModel.user_name);

    }

    @Override
    public int hashCode() {
        int result = user_name.hashCode();
        result = 31 * result + user_id;
        result = 31 * result + type;
        return result;
    }
}
