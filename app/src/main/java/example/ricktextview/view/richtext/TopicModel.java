package example.ricktextview.view.richtext;

import java.io.Serializable;

/**
 * 话题model
 */

public class TopicModel implements Serializable {
    /**
     * 话题名字内部不能有#和空格
     */
    private String topicName;
    private int topicId;

    public TopicModel() {

    }

    public TopicModel(String topicName, int topicId) {
        this.topicName = topicName;
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return this.topicName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TopicModel that = (TopicModel) o;

        if (topicId != that.topicId)
            return false;
        return topicName.equals(that.topicName);
    }

    @Override
    public int hashCode() {
        int result = topicName.hashCode();
        result = 31 * result + topicId;
        return result;
    }
}
