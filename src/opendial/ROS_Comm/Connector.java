package opendial.ROS_Comm;

import opendial.DialogueSystem;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

/**
 * This class starts rosCore and executes the talker and listener nodes.
 * Created by vbuchholz on 25.05.17.
 */
public class Connector {

    private Talker talker;
    private DialogueSystem system;

    public Connector() {
        this.talker = new Talker();
    }

    public void connect() {
        //URI ROS_MASTER_URI = URI.create("http://10.42.0.1:11311");
        URI ROS_MASTER_URI = URI.create("http://localhost:11311");
        NodeMainExecutor nodeExecutor = DefaultNodeMainExecutor.newDefault();

        NodeConfiguration talkerConfig = NodeConfiguration.newPrivate();
        talkerConfig.setMasterUri(ROS_MASTER_URI);
        talkerConfig.setNodeName("opendial/publisher");
        nodeExecutor.execute(this.talker, talkerConfig);

        Listener listener = new Listener(this.system);
        NodeConfiguration listenerConfig = NodeConfiguration.newPrivate();
        listenerConfig.setMasterUri(ROS_MASTER_URI);
        listenerConfig.setNodeName("opendial/subscriber");
        nodeExecutor.execute(listener, listenerConfig);
    }

    public Talker getTalker() {
        return talker;
    }

    public void setSystem(DialogueSystem system) {
        this.system = system;
    }
}
