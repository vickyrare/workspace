package io.codecrafts;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * An Simple example of exposing a single implementation as both
 * <p>
 * REST Based and SOAP SOAP based service.
 *
 */

@Path("/employeeManagerService")
@Produces("application/json")
@WebService(endpointInterface = "io.codecrafts.EmployeeManagerService",
            serviceName = "defaultEmployeeManagerService",
            wsdlLocation = "http://localhost:8000/employeeManagerService?wsdl")

public class EmployeeManagerImpl implements EmployeeManagerService {
    /**
     * Initial Data map which acts like a Data Source.
     */
    private static Map empMap = new HashMap();
    /**
     * A Simple id counter.
     */
    private int i;
    /*
     * Creating Initial Dummy Data .
     */
    {
        Employee employee;
        for (i = 0; i < 10; i++) {
            employee = new Employee();
            employee.setEmployeeID(i + "");
            employee.setEmployeeName("Name " + i);
            employee.setPhone("1800000000");
            employee.setDepartment("R&D");
            empMap.put(employee.getEmployeeID(), employee);
        }
    }

    /**
     * Gets the {@link Employee} identified.
     *
     * @return {@link Employee} employee identified by the specified id.
     */

    @GET
    @Path("/employee/{id}")
    public Employee getEmployee(@PathParam("id") String id) {
        System.out.println("Invoking getEmployee , Employee id is: " + id);
        return (Employee) empMap.get(id);
    }

    /**
     * Adds a new {@link Employee}.
     *
     * @return {@link Response} HTTP Response is returned.
     */

    @POST
    @Path("/employee")
    @Produces("application/json")
    public Response addEmployee(Employee employee) {
        System.out.println("Invoking addEmployee , Employee Name is: " + employee.getEmployeeName());
        employee.setEmployeeID(++i + "");
        empMap.put(employee.getEmployeeID(), employee);
        return Response.ok(employee).build();
    }

    /**
     * Updates an Existing {@link Employee}.
     *
     * @return {@link Response} HTTP Response is returned.
     */

    @PUT
    @Path("/employee")
    @Produces("application/json")

    public Response updateEmployee(Employee employee) {
        System.out.println("Invoking updateEmployee , Employee id is: " + employee.getEmployeeID());
        Employee emp = (Employee) empMap.get(employee.getEmployeeID());

        Response response;
        if (emp != null) {
            empMap.put(employee.getEmployeeID(), employee);
            response = Response.ok().build();
        } else {
            response = Response.notModified().build();
        }
        return response;
    }

    /**
     * Deletes an Existing {@link Employee}.
     *
     * @return {@link Response} HTTP Response is returned.
     */

    @DELETE
    @Path("/employee/{id}")
    public Response deleteEmployee(@PathParam("id") String id) {
        System.out.println("Invoking deleteEmployee , Employee id is: " + id);
        Employee employee = (Employee) empMap.get(id);
        Response response;
        if (employee != null) {
            response = Response.ok().build();
            empMap.remove(id);
        } else {
            response = Response.notModified().build();
        }
        return response;
    }

    public static void main(String[] args) {
        //Service instance
        EmployeeManagerService employeeManagerService = new EmployeeManagerImpl();

        //SOAP based service Server settings.
        JaxWsServerFactoryBean wsServer = new JaxWsServerFactoryBean();
        wsServer.setServiceClass(EmployeeManagerService.class);
        wsServer.setAddress("http://localhost:8000/employeeManagerService");
        wsServer.setServiceBean(employeeManagerService);
        wsServer.create();

        //REST based service Server Setting.
        JAXRSServerFactoryBean restServer = new JAXRSServerFactoryBean();
        //this line is a must, check the article for explanation.
        restServer.setResourceClasses(Employee.class);
        restServer.setServiceBean(employeeManagerService);
        restServer.setAddress("http://localhost:9000/");
        restServer.create();
    }
}