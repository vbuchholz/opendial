package opendial.ROS_Comm;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import std_msgs.String;

/**
 * This class publishes speech via ROS.
 * Created by vbuchholz on 25.05.17.
 */
public class Talker extends AbstractNodeMain {

    private Publisher<String> publisherSystemSpeech;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("opendial/publisher");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        this.publisherSystemSpeech = connectedNode.newPublisher("/set/speech", std_msgs.String._TYPE);
    }

    public void publishSystemSpeech(java.lang.String speech) {
        if (this.publisherSystemSpeech != null) {
            std_msgs.String str = this.publisherSystemSpeech.newMessage();
            str.setData(speech);
            this.publisherSystemSpeech.publish(str);
        } else {
            System.out.println("------------------publisher null-----------------");
        }
    }

    public boolean isRunning() {
        return this.publisherSystemSpeech != null;
    }
}
