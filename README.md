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