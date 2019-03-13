package io.codecrafts;

import javax.jws.WebParam;
import javax.jws.WebService;

import javax.ws.rs.core.Response;

@WebService(name = "employeeManagerService")

public interface EmployeeManagerService {
    public Employee getEmployee(@WebParam(name="id") String id);
    public Response addEmployee(Employee employee);
    public Response updateEmployee(Employee employee);
    public Response deleteEmployee(@WebParam(name="id") String id);
}