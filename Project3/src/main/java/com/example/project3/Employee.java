// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

// Stores employee data and provides getter/setter methods.
public class Employee {

    private int    employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String jobClass;
    private String jobDescription;
    private double salary;

    public Employee() {}

    public Employee(int employeeId, String firstName, String lastName,
                    String email, String phone, String jobClass,
                    String jobDescription, double salary) {
        this.employeeId     = employeeId;
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.email          = email;
        this.phone          = phone;
        this.jobClass       = jobClass;
        this.jobDescription = jobDescription;
        this.salary         = salary;
    }

    // Getters
    public int    getEmployeeId()     { return employeeId;     }
    public String getFirstName()      { return firstName;      }
    public String getLastName()       { return lastName;       }
    public String getEmail()          { return email;          }
    public String getPhone()          { return phone;          }
    public String getJobClass()       { return jobClass;       }
    public String getJobDescription() { return jobDescription; }
    public double getSalary()         { return salary;         }

    // Setters
    public void setEmployeeId(int employeeId)              { this.employeeId     = employeeId;     }
    public void setFirstName(String firstName)             { this.firstName      = firstName;      }
    public void setLastName(String lastName)               { this.lastName       = lastName;       }
    public void setEmail(String email)                     { this.email          = email;          }
    public void setPhone(String phone)                     { this.phone          = phone;          }
    public void setJobClass(String jobClass)               { this.jobClass       = jobClass;       }
    public void setJobDescription(String jobDescription)   { this.jobDescription = jobDescription; }
    public void setSalary(double salary)                   { this.salary         = salary;         }

    // Returns employee data as a JSON-formatted string.
    public String toJson() {
        return "{\n" +
                "  \"employeeId\": "      + employeeId                + ",\n" +
                "  \"firstName\": \""     + firstName                 + "\",\n" +
                "  \"lastName\": \""      + lastName                  + "\",\n" +
                "  \"email\": \""         + email                     + "\",\n" +
                "  \"phone\": \""         + phone                     + "\",\n" +
                "  \"jobClass\": \""      + jobClass                  + "\",\n" +
                "  \"jobDescription\": \"" + jobDescription           + "\",\n" +
                "  \"salary\": "          + salary                    + "\n"   +
                "}";
    }
}
