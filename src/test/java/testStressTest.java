import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * Stress test
 * Still under development
 */
@Disabled("This test is still under development and should not be run automatically")
public class testStressTest {
    private static HTTPSamplerProxy createHttpSamplerProxy() {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setName("HTTP Request");
        httpSamplerProxy.setDomain("yourwebsite.com");
        httpSamplerProxy.setPort(80);
        httpSamplerProxy.setProtocol("http");
        httpSamplerProxy.setMethod("GET");
        httpSamplerProxy.setPath("/search");
        httpSamplerProxy.setFollowRedirects(true);
        httpSamplerProxy.setAutoRedirects(false);
        httpSamplerProxy.setUseKeepAlive(true);
        httpSamplerProxy.setDoMultipartPost(false);
        httpSamplerProxy.setMonitor(false);
        return httpSamplerProxy;
    }

    @Disabled("This test is still under development and should not be run automatically")
    @Test
    public void stressTest() {
        // Initialize JMeter
        loadJMeterProperties();
        JMeterUtils.initLocale();

        // Create Test Plan
        TestPlan testPlan = new TestPlan("Stress Test");


        // Create Thread Group
        // Get the current thread's ThreadGroup
        ThreadGroup parentGroup = Thread.currentThread().getThreadGroup();

        // Specify the name of the new thread group
        String groupName = "Stress Users";

        ThreadGroup threadGroup = new ThreadGroup(parentGroup, groupName);
        threadGroup.setMaxPriority(Thread.currentThread().getPriority());


        // Create Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(10);
        loopController.setContinueForever(false);
        loopController.addTestElement(createHttpSamplerProxy());

        // Build Test Plan Tree
        HashTree testPlanTree = new HashTree();
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(loopController);

        // Run Test Plan
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
    }

    private Properties loadJMeterProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jmeter.properties")
        ) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}

