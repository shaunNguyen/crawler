# How Spring boot crawl data from api
1. Init maven project by dependencies
                <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                </dependency>
2. quang20@quang20:~/practice/demo$ mvn test -X

3. coding 

>>> init crawler data 
commit 6b51e16aa3a87fec220e972bf09c34e0ac0c7d09
Author: Quang Ng <nhvquang@tma.com.vn>
Date:   Tue Aug 22 16:34:14 2023 +0700

    crawl data

diff --git a/pom.xml b/pom.xml
new file mode 100644
index 0000000..7ffb76b
--- /dev/null
+++ b/pom.xml
@@ -0,0 +1,41 @@
+<?xml version="1.0" encoding="UTF-8"?>
+<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
+       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
+       <modelVersion>4.0.0</modelVersion>
+       <parent>
+               <groupId>org.springframework.boot</groupId>
+               <artifactId>spring-boot-starter-parent</artifactId>
+               <version>2.5.3</version>
+               <relativePath/> <!-- lookup parent from repository -->
+       </parent>
+       <groupId>com.example</groupId>
+       <artifactId>demo</artifactId>
+       <version>0.0.1-SNAPSHOT</version>
+       <name>demo</name>
+       <description>Demo project for Spring Boot</description>
+       <properties>
+               <java.version>1.8</java.version>
+       </properties>
+       <dependencies>
+               <dependency>
+                       <groupId>org.springframework.boot</groupId>
+                       <artifactId>spring-boot-starter-web</artifactId>
+               </dependency>
+
+               <dependency>
+                       <groupId>org.springframework.boot</groupId>
+                       <artifactId>spring-boot-starter-test</artifactId>
+                       <scope>test</scope>
+               </dependency>
+       </dependencies>
+
+       <build>
+               <plugins>
+                       <plugin>
+                               <groupId>org.springframework.boot</groupId>
+                               <artifactId>spring-boot-maven-plugin</artifactId>
+                       </plugin>
+               </plugins>
+       </build>
+
+</project>
diff --git a/src/main/java/com/example/demo/DemoApplication.java b/src/main/java/com/example/demo/DemoApplication.java
new file mode 100644
index 0000000..64b538a
--- /dev/null
+++ b/src/main/java/com/example/demo/DemoApplication.java
@@ -0,0 +1,13 @@
+package com.example.demo;
+
+import org.springframework.boot.SpringApplication;
+import org.springframework.boot.autoconfigure.SpringBootApplication;
+
+@SpringBootApplication
+public class DemoApplication {
+
+       public static void main(String[] args) {
+               SpringApplication.run(DemoApplication.class, args);
+       }
+
+}
diff --git a/src/main/java/com/example/demo/dto/CovidTrackingDTO.java b/src/main/java/com/example/demo/dto/CovidTrackingDTO.java
new file mode 100644
index 0000000..847d555
--- /dev/null
+++ b/src/main/java/com/example/demo/dto/CovidTrackingDTO.java
@@ -0,0 +1,8 @@
+package com.example.demo.dto;
+
+public class CovidTrackingDTO {
+    public int date;
+    public String state;
+    public int positive;
+}
+
diff --git a/src/main/java/com/example/demo/service/CrawlerService.java b/src/main/java/com/example/demo/service/CrawlerService.java
new file mode 100644
index 0000000..a3f5f93
--- /dev/null
+++ b/src/main/java/com/example/demo/service/CrawlerService.java
@@ -0,0 +1,32 @@
+package com.example.demo.service;
+
+import javax.annotation.PostConstruct;
+import org.springframework.stereotype.Service;
+import org.springframework.stereotype.Component;
+import org.springframework.web.client.RestTemplate;
+import org.springframework.beans.factory.annotation.Value;
+
+import com.example.demo.dto.CovidTrackingDTO;
+
+@Service
+//@Component
+public class CrawlerService {
+    @Value("${covidTracking}")
+    public String covidTracking;
+
+    @PostConstruct
+    public void getAllCovidDTO() {
+        System.out.printf("Hello from getAllCovidDTO()\n");
+        RestTemplate restTemplate = new RestTemplate();
+        CovidTrackingDTO[] covidTrackingDTO = restTemplate.getForObject(covidTracking, CovidTrackingDTO[].class);
+
+       if (covidTrackingDTO != null) {
+            //System.out.printf("not NULL\n");
+            for (CovidTrackingDTO covid : covidTrackingDTO) {
+                System.out.printf("state %s, date %d, positive %d\n", covid.state, covid.date, covid.positive);
+           }
+       } else {
+            System.out.printf("null\n");
+        }
+    }
+}
diff --git a/src/main/resources/application.properties b/src/main/resources/application.properties
new file mode 100644
index 0000000..c05decd
--- /dev/null
+++ b/src/main/resources/application.properties
@@ -0,0 +1,3 @@
+#api
+covidTracking=https://api.covidtracking.com/v1/states/current.json
+

>>> schedule crawl job

commit b2d879dbe67e35363c4d987a3fdd7d1b094cc2db
Author: Quang Ng <nhvquang@tma.com.vn>
Date:   Tue Aug 22 16:34:43 2023 +0700

    schedule crawl job

diff --git a/src/main/java/com/example/demo/DemoApplication.java b/src/main/java/com/example/demo/DemoApplication.java
index 64b538a..58cde1b 100644
--- a/src/main/java/com/example/demo/DemoApplication.java
+++ b/src/main/java/com/example/demo/DemoApplication.java
@@ -2,7 +2,9 @@ package com.example.demo;

 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
+import org.springframework.scheduling.annotation.EnableScheduling;

+@EnableScheduling
 @SpringBootApplication
 public class DemoApplication {

diff --git a/src/main/java/com/example/demo/service/CrawlerService.java b/src/main/java/com/example/demo/service/CrawlerService.java
index a3f5f93..c5a4960 100644
--- a/src/main/java/com/example/demo/service/CrawlerService.java
+++ b/src/main/java/com/example/demo/service/CrawlerService.java
@@ -5,16 +5,18 @@ import org.springframework.stereotype.Service;
 import org.springframework.stereotype.Component;
 import org.springframework.web.client.RestTemplate;
 import org.springframework.beans.factory.annotation.Value;
+import org.springframework.scheduling.annotation.Scheduled;

 import com.example.demo.dto.CovidTrackingDTO;

-@Service
-//@Component
+//@Service
+@Component
 public class CrawlerService {
     @Value("${covidTracking}")
     public String covidTracking;

-    @PostConstruct
+    //@PostConstruct
+    @Scheduled(fixedRateString = "${schedule.fixed.time.mil.seconds}")
     public void getAllCovidDTO() {
         System.out.printf("Hello from getAllCovidDTO()\n");
         RestTemplate restTemplate = new RestTemplate();
diff --git a/src/main/resources/application.properties b/src/main/resources/application.properties
index c05decd..5c79682 100644
--- a/src/main/resources/application.properties
+++ b/src/main/resources/application.properties
@@ -1,3 +1,8 @@
 #api
 covidTracking=https://api.covidtracking.com/v1/states/current.json

+#schedule
+schedule.fixed.time.mil.seconds=10000
+

>>> change to another api
quang20@quang20:~/practice/demo$ git show 00d26da0549b02a3bc77c0593666a43219308d1e
commit 00d26da0549b02a3bc77c0593666a43219308d1e (HEAD -> practice, master)
Author: Quang Ng <nhvquang@tma.com.vn>
Date:   Tue Aug 22 16:58:15 2023 +0700

    change another api

diff --git a/src/main/java/com/example/demo/dto/CovidTrackingDTO.java b/src/main/java/com/example/demo/dto/CovidTrackingDTO.java
index 847d555..f1241b9 100644
--- a/src/main/java/com/example/demo/dto/CovidTrackingDTO.java
+++ b/src/main/java/com/example/demo/dto/CovidTrackingDTO.java
@@ -1,8 +1,9 @@
 package com.example.demo.dto;

 public class CovidTrackingDTO {
-    public int date;
-    public String state;
-    public int positive;
+    public String country;
+    public String recovered;
+    public String  infected;
+    public String lastUpdatedApify;
 }

diff --git a/src/main/java/com/example/demo/service/CrawlerService.java b/src/main/java/com/example/demo/service/CrawlerService.java
index c5a4960..eeaa070 100644
--- a/src/main/java/com/example/demo/service/CrawlerService.java
+++ b/src/main/java/com/example/demo/service/CrawlerService.java
@@ -25,7 +25,7 @@ public class CrawlerService {
        if (covidTrackingDTO != null) {
             //System.out.printf("not NULL\n");
             for (CovidTrackingDTO covid : covidTrackingDTO) {
-                System.out.printf("state %s, date %d, positive %d\n", covid.state, covid.date, covid.positive);
+                System.out.printf("country: %s: \n \tinfected: %s, \n\trecovered: %s, \n\t--%s--\n", covid.country, covid.infected, covid.recovered, covid.lastUpdatedApify);
            }
        } else {
             System.out.printf("null\n");
diff --git a/src/main/resources/application.properties b/src/main/resources/application.properties
index 5c79682..ee4eaf1 100644
--- a/src/main/resources/application.properties
+++ b/src/main/resources/application.properties
@@ -1,5 +1,6 @@
 #api
-covidTracking=https://api.covidtracking.com/v1/states/current.json
+#covidTracking=https://api.covidtracking.com/v1/states/current.json
+covidTracking=https://api.apify.com/v2/key-value-stores/tVaYRsPHLjNdNBu7S/records/LATEST?disableRedirect=true

 #schedule
 schedule.fixed.time.mil.seconds=10000
 
# How Spring boot write to MongoDB

# How Spring boot producer to Kafka
