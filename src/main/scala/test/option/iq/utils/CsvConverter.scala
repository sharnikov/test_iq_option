package test.option.iq.utils

import test.option.iq.Items

trait CsvConverter {

  private implicit class DefValue[T](value: Option[T]) {
    def dv = value.getOrElse("")
  }

  def itemsToCsv(items: Seq[Items]): Seq[String] = {

    items.map { item =>
      new StringBuilder(f"${item.id};")
        .append(f"${item.premium};")
        .append(f"${item.name};")
        .append(f"${item.department.map(_.id).dv};")
        .append(f"${item.department.map(_.name).dv};")
        .append(f"${item.has_test};")
        .append(f"${item.response_letter_required};")
        .append(f"${item.area.id};")
        .append(f"${item.area.name};")
        .append(f"${item.salary.flatMap(_.from).dv};")
        .append(f"${item.salary.flatMap(_.to).dv};")
        .append(f"${item.salary.flatMap(_.currency).dv};")
        .append(f"${item.salary.flatMap(_.gross).dv};")

        .append(f"${item.address.flatMap(_.street).dv};")
        .append(f"${item.address.flatMap(_.building).dv};")
        .append(f"${item.address.flatMap(_.description).dv};")
        .append(f"${item.address.flatMap(_.lat).dv};")
        .append(f"${item.address.flatMap(_.lng).dv};")
        .append(f"${item.address.flatMap(_.raw).dv};")
        .append(f"${item.address.flatMap(_.id).dv};")

        .append(f"${item.employer.id.dv};")
        .append(f"${item.employer.name.dv};")
        .append(f"${item.employer.url.dv};")
        .append(f"${item.employer.vacancies_url.dv};")
        .append(f"${item.employer.trusted.dv};")

        .append(f"${item.created_at};")
        .append(f"${item.url};")
        .append(f"${item.alternate_url};")
        .append(f"${item.snippet.requirement.dv};")
        .append(f"${item.snippet.responsibility.dv}")
        .append("\n").toString()
    }
  }
}
