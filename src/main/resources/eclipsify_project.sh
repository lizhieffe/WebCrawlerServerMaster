cd ../../../../WebCrawlerServerCommon
mvn source:jar install
cd ../WebCrawlerServerMaster
mvn eclipse:eclipse -DdownloadSources=true
