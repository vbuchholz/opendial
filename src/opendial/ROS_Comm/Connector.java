package opendial.ROS_Comm;

import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

/**
 * This class starts rosCore and executes the talker and listener nodes.
 * Created by vbuchholz on 25.05.17.
 */
public class Connector {

    private Talker talker;
    private Listener listener;

    public Connector() {
        this.talker = new Talker();
        this.listener = new Listener();
    }

    public void connect() {
        RosCore rosCore = RosCore.newPublic(11311);
        rosCore.start();

        try {
            rosCore.awaitStart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NodeMainExecutor nodeExecutor = DefaultNodeMainExecutor.newDefault();

        NodeConfiguration talkerConfig = NodeConfiguration.newPrivate();
        talkerConfig.setMasterUri(rosCore.getUri());
        talkerConfig.setNodeName("opendial/publisher");
        nodeExecutor.execute(this.talker, talkerConfig);

        NodeConfiguration listenerConfig = NodeConfiguration.newPrivate();
        listenerConfig.setMasterUri(rosCore.getUri());
        listenerConfig.setNodeName("opendial/subscriber");
        nodeExecutor.execute(this.listener, listenerConfig);
    }

    public Talker getTalker() {
        return talker;
    }

    public Listener getListener() {
        return listener;
    }
}
