package test.option.iq

object Utils {

  def getCvsHeader(): List[String] =
    List(
      "id",
      "premium",
      "name",
      "department_id",
      "department_name",
      "has_test",
      "response_letter_required",
      "area_id",
      "area_name",
      "salary_from",
      "salary_to",
      "salary_currency",
      "salary_gross",
      "adress_street",
      "adress_building",
      "adress_description",
      "adress_lat",
      "adress_lng",
      "adress_raw",
      "adress_id",
      "employer_id",
      "employer_name",
      "employer_url",
      "employer_vacancies_url",
      "employer_trusted",
      "created_at",
      "url",
      "alternate_url",
      "snippet_requirement",
      "snippet_responsibility"
    )
}