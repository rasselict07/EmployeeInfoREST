## Employee Information create, update, filter by age through api using JSON file as a Database

#### Update the file directory and file name in application.yml file

```    
app:
  filedir: 'directory path' 
  filename: 'file name'
```

This property included in employee domain :-
```
String fullName
int age
double salary
```

All data will store, add or update in defined JSON file in local system.

####This are the api list:

```
For getting all
http://localhost:8080/employee >> Method type GET

Get employee info by id
http://localhost:8080/employee/1  >> Method type GET

Create employee with form data
http://localhost:8080/employee  >>Method type POST

Delete employee by id
http://localhost:8080/employee/10  >> Method type DELETE

Search employee by age less than or equal
http://localhost:8080/filterByAge/5 >> Method type GET
```

Thanks to all