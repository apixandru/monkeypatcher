# monkeypatcher

Modify the behaviour of applications without touching the jars. Just add the javaagent as a parameter when launching the java application.

    java -javaagent:monkeypatcher-1.0-SNAPSHOT-fat.jar=agentconfig.yml -jar somejar.jar

Sample agentconfig.yml

    overrideSecurityManager: true

    inspectClasses:
      enabled: false

      includeNameEquals:
        - com.apixandru.xbox.XboxMarketplaceEntry
        - com.apixandru.xbox.XboxMarketplace

      includeNameStartsWith:
        - org.slf4j

    logExecutions:
      enabled: true
      includeAll: false
      includeReturnValues: true
      includeNameStartsWith:
        - com.apixandru.xbox

      excludeNameStartsWith:
        - java
        - javax
        - sun
        - jdk
        - com.sun
        - com.github.apixandru
        - com.apixandru
        - org.slf4j

    alterExecutions:
      overrides:
        - className: com.apixandru.xbox.XboxMarketplace
          methods:
            - name: purchase(com.apixandru.xbox.XboxMarketplaceEntry)
              before: |-
                      if (!entry.isOnSale()) {
                          throw new IllegalStateException("Wait for the sale!");
                      }

        - className: com.apixandru.xbox.XboxMarketplaceEntry
          methods:
            - name: isOnSale()
              body: |-
                    return false;
