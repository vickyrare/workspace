soap-webservice-axis-maven project:

compile:
mvn install
run:
mvn jetty:run

SSL:
//Generate the self signed certificate.
keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 360

//List certicates inside a keystore.
keytool -list -keystore keystore.jks -storepass password

// extract the certificate from the keystores
keytool -export -alias selfsigned -file key.crt -keystore keystore.jks -storepass password

// import certificate into truststore
keytool -import -alias selfsigned -file key.crt -keystore truststore.jks -storepass password

// reference truststore
String certificatesTrustStorePath = "C:/dev/workspace/soap-webservice-axis-maven/server/truststore.jks";
System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);