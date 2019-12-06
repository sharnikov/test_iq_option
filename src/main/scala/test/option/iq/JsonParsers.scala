package test.option.iq

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object JsonParsers extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val areaFormat = jsonFormat2(Area)
  implicit val salaryFormat = jsonFormat4(Salary)
  implicit val typeBisFormat = jsonFormat2(TypeBis)
  implicit val employerFormat = jsonFormat5(Employer)
  implicit val snippetFormat = jsonFormat2(Snippet)
  implicit val departmentFormat = jsonFormat2(Department)
  implicit val addressFormat = jsonFormat8(Address)
  implicit val itemsFormat = jsonFormat15(Items)
  implicit val vacanciesFormat = jsonFormat8(Vacancies)
}
