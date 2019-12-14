package test.option.iq.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import test.option.iq._

object JsonParsers extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val areaFormat = jsonFormat2(Area)
  implicit val salaryFormat = jsonFormat4(Salary)
  implicit val employerFormat = jsonFormat5(Employer)
  implicit val snippetFormat = jsonFormat2(Snippet)
  implicit val departmentFormat = jsonFormat2(Department)
  implicit val addressFormat = jsonFormat7(Address)
  implicit val itemsFormat = jsonFormat14(Items)
  implicit val vacanciesFormat = jsonFormat8(Vacancies)
}
