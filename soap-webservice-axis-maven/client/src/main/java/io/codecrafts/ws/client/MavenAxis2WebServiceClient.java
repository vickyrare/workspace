package io.codecrafts.ws.client;

import java.rmi.RemoteException;

public class MavenAxis2WebServiceClient {
   public static void main(String [] args) throws RemoteException {
       MavenAxis2WebServiceClient mavenAxis2WebServiceClient = new MavenAxis2WebServiceClient();

       //http
       mavenAxis2WebServiceClient.callWebservice("http://localhost:8080/MavenAxis2WS/services/MavenAxis2WebService");

       //https
       mavenAxis2WebServiceClient.callWebservice("https://localhost:8443/MavenAxis2WS/services/MavenAxis2WebService");
   }

   public void callWebservice(String URL) throws RemoteException {
       String certificatesTrustStorePath = "C:/dev/workspace/soap-webservice-axis-maven/server/truststore.jks";
       System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);

       MavenAxis2WebServiceStub servicesStub = new MavenAxis2WebServiceStub(URL);
       MavenAxis2WebServiceStub.Ping ping = new MavenAxis2WebServiceStub.Ping();
       MavenAxis2WebServiceStub.PingResponse pingResponse= servicesStub.ping(ping);
       System.out.println(pingResponse.get_return());

       MavenAxis2WebServiceStub.Sum sum = new MavenAxis2WebServiceStub.Sum();
       sum.setX(10);
       sum.setY(20);
       MavenAxis2WebServiceStub.SumResponse sumResponse= servicesStub.sum(sum);
       System.out.println(sumResponse.get_return());
   }
}