call mvn install:install-file -Dfile=lib/com.mercury.qualitycenter.otaclient-9.2.jar -DgroupId=com.mercury.qualitycenter -DartifactId=otaclient -Dversion=9.2 -Dpackaging=jar

call mvn install:install-file -Dfile=lib/com4j.jar -DgroupId=com.mercury.qualitycenter -DartifactId=com4j -Dversion=1.0 -Dpackaging=jar

call mvn deploy