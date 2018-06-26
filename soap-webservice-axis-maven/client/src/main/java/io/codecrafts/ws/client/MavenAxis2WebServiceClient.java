package io.codecrafts.ws.client;

import java.rmi.RemoteException;

public class MavenAxis2WebServiceClient   {
   public static void main (String [] args) throws RemoteException
   {
        MavenAxis2WebServiceStub servicesStub = new MavenAxis2WebServiceStub("http://localhost:8080/MavenAxis2WS/services/MavenAxis2WebService");
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