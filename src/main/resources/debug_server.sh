cd ../../../../WebCrawlerServerCommon
mvn source:jar install
cd ../WebCrawlerServerMaster
mvn eclipse:eclipse -DdownloadSources=true
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9998"
