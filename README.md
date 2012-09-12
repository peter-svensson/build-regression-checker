build-regression-checker
========================

This Jenkins plugin provides a way to prevent regressions by marking any 
build which decreases the quality of the code.

Compiling
---------

Since this is a Jenkins plugin, you will need to have the Jenkins repository 
set up in the settings.xml that you use. For your convenience, such a settings.xml is provided here:

     <?xml version="1.0"?>
     <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0                       http://maven.apache.org/xsd/settings-1.0.0.xsd">
      <localRepository>${user.home}/.m2/repository</localRepository>
      <interactiveMode>true</interactiveMode>
      <usePluginRegistry>false</usePluginRegistry>
      <offline>false</offline>
      <pluginGroups>
        <pluginGroup>org.jenkins-ci.tools</pluginGroup>
      </pluginGroups>

      <profiles>
        <!-- Give access to Jenkins plugins -->
        <profile>
          <id>jenkins</id>
          <activation>
            <activeByDefault>true</activeByDefault> <!-- change this to false, if you don't like to have it on per default -->
          </activation>
          <repositories>
            <repository>
              <id>repo.jenkins-ci.org</id>
              <url>http://repo.jenkins-ci.org/public/</url>
            </repository>
          </repositories>
          <pluginRepositories>
            <pluginRepository>
              <id>repo.jenkins-ci.org</id>
              <url>http://repo.jenkins-ci.org/public/</url>
            </pluginRepository>
          </pluginRepositories>
        </profile>
      </profiles>
     </settings>

Installing
----------

Install the plugin manually in Jenkins.


