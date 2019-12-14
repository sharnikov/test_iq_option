package test.option.iq.settings

trait Config {
  def hh(): HH
}

class AppConfig extends Config {
  override def hh(): HH = new HHSettings()
}

trait HH {
  def userAgentHeader(): String
  def restUrl(): String
  def fetchVacanciesAmount(): Int
}

class HHSettings extends HH {
  override def userAgentHeader(): String = "IQ-Option-Test-App/1.0 (oleg.sharnikov@outlook.com)"
  override def restUrl(): String = "https://api.hh.ru/vacancies?area=2&vacancy_search_order=publication_time&per_page=100&page="
  override def fetchVacanciesAmount(): Int = 2000
}


