package opendial.ROS_Comm;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import std_msgs.String;

/**
 * Created by vbuchholz on 25.05.17.
 */
public class Listener extends AbstractNodeMain {

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("opendial/subscriber");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Log log = connectedNode.getLog();
        Subscriber<String> subscriber = connectedNode.newSubscriber("chatter", std_msgs.String._TYPE);
        subscriber.addMessageListener(message -> log.info("I heard: \"" + message.getData() + "\""));
    }
}
