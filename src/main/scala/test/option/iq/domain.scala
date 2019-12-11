package test.option.iq

case class Area(id: String,
                name: String)

case class Salary(from: Option[Double],
                  to: Option[Double],
                  currency: String,
                  gross: Option[Boolean])

case class Employer(id: Option[String],
                    name: Option[String],
                    url: Option[String],
                    vacancies_url: Option[String],
                    trusted: Option[Boolean])

case class Snippet(requirement: Option[String],
                   responsibility: Option[String])

case class Address(street: Option[String],
                   building: Option[String],
                   description: Option[String],
                   lat: Option[Double],
                   lng: Option[Double],
                   raw: Option[String],
                   id: Option[String])

case class Department(id: String,
                      name: String)

case class Items(id: String,
                 premium: Boolean,
                 name: String,
                 department: Option[Department],
                 has_test: Boolean,
                 response_letter_required: Boolean,
                 area: Area,
                 salary: Option[Salary],
                 address: Option[Address],
                 employer: Employer,
                 created_at: String,
                 url: String,
                 alternate_url: String,
                 snippet: Snippet)

case class Vacancies(items: List[Items],
                     found: Double,
                     pages: Double,
                     per_page: Double,
                     page: Double,
                     clusters: Option[String],
                     arguments: Option[String],
                     alternate_url: Option[String])
