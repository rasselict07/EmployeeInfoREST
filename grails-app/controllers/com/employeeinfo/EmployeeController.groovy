package com.employeeinfo

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.validation.ValidationException
import groovy.json.JsonSlurper

import static com.employeeinfo.Tools.SLASH

class EmployeeController {
    GrailsApplication grailsApplication

    EmployeeService employeeService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

        String fileDir = grailsApplication.config.getProperty('app.filedir')
        def folder = new File(fileDir)
        if (!folder.exists()) {
            log.error("Directory not found")
            return
        }

        String fileName = grailsApplication.config.getProperty('app.filename')
        def file = new File(fileDir + SLASH + fileName)

        if (!file.exists()) {
            log.error("File not exists")
            return
        }

        List<Employee> employeeList = []
        def employeeListJSON = new JsonSlurper().parse(file)
        employeeListJSON.each {
            Employee employee = new Employee()
            employee.id = it.id
            employee.age = it.age
            employee.salary = it.salary
            employee.fullName = it.fullName

            employeeList.add(employee)
        }

        params.max = Math.min(max ?: 10, 100)
        render employeeListJSON as JSON
    }

    def show(Long id) {

        String fileDir = grailsApplication.config.getProperty('app.filedir')
        String fileName = grailsApplication.config.getProperty('app.filename')
        def file = new File(fileDir + SLASH + fileName)
        if (!file.exists()) {
            log.error("File not exists")
            return
        }
        Employee employee = new Employee()
        def employeeInfoList = new JsonSlurper().parse(file)
        employeeInfoList.each {
            if (id == it.id) {
                employee.id = it.id
                employee.age = it.age
                employee.salary = it.salary
                employee.fullName = it.fullName
            }
        }

        render employee as JSON
    }

    def save() {
        Employee employee = new Employee()
        try {
            String fileDir = grailsApplication.config.getProperty('app.filedir')
            String fileName = grailsApplication.config.getProperty('app.filename')
            def file = new File(fileDir + SLASH + fileName)
            if (!file.exists()) {
                log.error("File not exists")
                return
            }
            def employeeInfoList = new JsonSlurper().parse(file)

            List ids = employeeInfoList ? employeeInfoList.collect { it.id } : []
            long currentMax = (ids.size() > 0) ? ids.max() : 0

            employee.id = currentMax + 1
            employee.age = Integer.parseInt(params.age)
            employee.salary = Double.parseDouble(params.salary)
            employee.fullName = params.fullName

            employeeInfoList = employeeInfoList << employee as JSON

            file.delete()
            new File(fileDir, fileName).withWriterAppend { writer ->
                writer << employeeInfoList
            }
        } catch (ValidationException e) {
            render employee.errors as JSON
            return
        }

        render employee as JSON
    }

    def update() {
        Employee employee = new Employee()
        try {
            String fileDir = grailsApplication.config.getProperty('app.filedir')
            String fileName = grailsApplication.config.getProperty('app.filename')
            def file = new File(fileDir + SLASH + fileName)
            if (!file.exists()) {
                log.error("File not exists")
                return
            }
            def employeeInfoList = new JsonSlurper().parse(file)
            for (int i = 0; i < employeeInfoList.size(); i++) {
                if (Long.parseLong(params.id) == employeeInfoList.get(i).id) {
                    employee.id = Long.parseLong(params.id)
                    employee.age = Integer.parseInt(params.age)
                    employee.salary = Double.parseDouble(params.salary)
                    employee.fullName = params.fullName

                    employeeInfoList.remove(employeeInfoList.get(i))
                    employeeInfoList = employeeInfoList << employee as JSON
                    break
                }
            }

            file.delete()
            new File(fileDir, fileName).withWriterAppend { writer ->
                writer << employeeInfoList
            }

        } catch (ValidationException e) {
            render employee.errors as JSON
            return
        }

        render employee as JSON
    }

    def delete(Long id) {
        String fileDir = grailsApplication.config.getProperty('app.filedir')
        String fileName = grailsApplication.config.getProperty('app.filename')
        def file = new File(fileDir + SLASH + fileName)
        if (!file.exists()) {
            log.error("File not exists")
            return
        }
        Employee employee = new Employee()
        def employeeInfoList = new JsonSlurper().parse(file)
        for (int i = 0; i < employeeInfoList.size(); i++) {
            if (id == employeeInfoList.get(i).id) {
                employee.id = employeeInfoList.get(i).id
                employee.age = employeeInfoList.get(i).age
                employee.salary = employeeInfoList.get(i).salary
                employee.fullName = employeeInfoList.get(i).fullName

                employeeInfoList.remove(employeeInfoList.get(i))
                break
            }
        }

        def employeeInfoListJson = employeeInfoList as JSON
        file.delete()
        new File(fileDir, fileName).withWriterAppend { writer ->
            writer << employeeInfoListJson
        }

        log.println(employee.fullName + " deleted successfully")
        Map successMap = ["success": (employee.fullName + " deleted successfully")]
        render successMap as JSON
    }


    def filterByAge(int age) {

        String fileDir = grailsApplication.config.getProperty('app.filedir')
        def folder = new File(fileDir)
        if (!folder.exists()) {
            log.error("Directory not found")
            return
        }

        String fileName = grailsApplication.config.getProperty('app.filename')
        def file = new File(fileDir + SLASH + fileName)

        if (!file.exists()) {
            log.error("File not exists")
            return
        }

        List<Employee> employeeList = []
        def employeeListJSON = new JsonSlurper().parse(file)
        for (int i = 0; i < employeeListJSON.size(); i++) {
            if (age >= employeeListJSON.get(i).age) {
                Employee employee = new Employee()
                employee.id = employeeListJSON.get(i).id
                employee.age = employeeListJSON.get(i).age
                employee.salary = employeeListJSON.get(i).salary
                employee.fullName = employeeListJSON.get(i).fullName

                employeeList.add(employee)
            }
        }

//        params.max = Math.min(max ?: 10, 100)
        render employeeList as JSON
    }

}
