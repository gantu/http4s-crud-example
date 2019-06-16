package postoffice.config

import cats.effect.{Async, Sync}
import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import scala.concurrent.ExecutionContext

case class DatabaseConfig(driver: String, url: String, user: String, password: String)

object DatabaseConfig {

  def transactor(config: DatabaseConfig): IO[HikariTransactor[IO]] = {
    HikariTransactor.newHikariTransactor[IO](config.driver, config.url, config.user, config.password)
  }

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { datasource =>
      IO {
        val flyWay = new Flyway()
        flyWay.setDataSource(datasource)
        flyWay.migrate()
      }
    }
  }
}
