# monkeypatcher

Modify the behaviour of applications without touching the jars. Just add the javaagent as a parameter when launching the java application.

    java -javaagent:monkeypatcher-1.0-SNAPSHOT-fat.jar=agentconfig.xml -jar somejar.jar

Sample agentconfig.xml

    <?xml version='1.0' encoding='UTF-8'?>
    <com.github.apixandru.utils.monkeypatcher.reimpl.ReimplTransformer>

    <classes>
        <class name="com.github.apixandru.SampleClass">
            <methods>
                <method longname="com.github.apixandru.SampleClass.method1()">
                    <insert-before>System.out.println("Entered method1");</insert-before>
                    <body>System.out.println("Inside method1");</body>
                    <insert-after>System.out.println("Exiting method1");</insert-after>
                </method>
            </methods>
        </class>
    </classes>

    </com.github.apixandru.utils.monkeypatcher.reimpl.ReimplTransformer>
