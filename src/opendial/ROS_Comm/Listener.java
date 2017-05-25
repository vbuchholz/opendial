package opendial.ROS_Comm;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import std_msgs.String;

/**
 * This class subscribes to ROS topics.
 * Created by vbuchholz on 25.05.17.
 */
public class Listener extends AbstractNodeMain {

    private Subscriber<String> subscriberUserSpeech;
    //private Subscriber<String> subscriberSomethingDifferent;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("opendial/subscriberUserSpeech");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Log log = connectedNode.getLog();
        this.subscriberUserSpeech = connectedNode.newSubscriber("systemspeech", std_msgs.String._TYPE);
        subscriberUserSpeech.addMessageListener(new MessageListener<String>() {
            @Override
            public void onNewMessage(String message) {
                log.info("I heard: \"" + message.getData() + "\"");
                //TODO: do something with message
            }
        });

        //this.subscriberSomethingDifferent = connectedNode.newSubscriber("userspeech", std_msgs.String._TYPE);
        //subscriberSomethingDifferent.addMessageListener(message -> log.info("I heard: \"" + message.getData() + "\""));
    }

    public boolean isRunning() {
        return this.subscriberUserSpeech != null;
    }
}
