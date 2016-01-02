# monkeypatcher

Modify the behaviour of applications without touching the jars. Just add the javaagent as a parameter when launching the java application.

    java -javaagent:monkeypatcher-1.0-SNAPSHOT-fat.jar=agentconfig.xml -jar somejar.jar

Sample agentconfig.xml

    <?xml version='1.0' encoding='UTF-8'?>
    <agent-config>
    
        <properties>
            <logging.pattern>%1$tT %4$s %2$s %5$s%6$s%n</logging.pattern>
        </properties>
    
        <classes>
            <class name="com.github.apixandru.SampleClass">
                <class-pool>
                    <stub>com.github.apixandru.otherproject.Class1</stub>
                </class-pool>
                <methods>
                    <method longname="com.github.apixandru.SampleClass.method1()">
                        <body>System.out.println("method1 removed");</body>
                    </method>
                </methods>
            </class>
        </classes>
    
    </agent-config>
