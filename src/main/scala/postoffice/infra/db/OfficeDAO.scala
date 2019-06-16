package postoffice.infra.db

import postoffice.domain.offices.Office
import postoffice.domain.CommonRepoAlgebra
import cats.data.OptionT
import cats.implicits._
import doobie.implicits._
import doobie._
import cats._
import cats.effect.IO
import postoffice.domain.Errors._
import doobie.util.transactor.Transactor

private object OfficeSQL {
  def select(officeId: Long): Query0[Office] = sql"SELECT id,zip,name FROM offices WHERE ID=$officeId".query[Office]

  def insert(office: Office): Update0 = sql"INSERT INTO offices(zip, name) VALUES(${office.zip}, ${office.name})".update

  def delete(officeId: Long): Update0 = sql"DELETE FROM offices WHERE id=$officeId".update

  def selectAll: Query0[Office] = sql"SELECT * FROM offices".query[Office]

  def update(office: Office): Update0 = sql"UPDATE OFFICES SET ZIP=${office.zip}, NAME=${office.name} WHERE ID=${office.id}".update

}
case class OfficeDAO(val xa: Transactor[IO]) extends CommonRepoAlgebra[Office] {
  import OfficeSQL._
  def create(office: Office): IO[Either[Errors, Office]] = OfficeSQL.insert(office)
    .withUniqueGeneratedKeys[Long]("id")
    .attemptSomeSqlState{
      case SqlState("23505") => UniqueConstraintError
      case state => CustomError(state)
    }
    .transact(xa)
    .map {
      case Right(id: Long) => Right(office.copy(id = Some(id)))
      case Left(error) => Left(error)
    }
  def delete(officeId: Long): IO[Either[Errors, Unit]] = OfficeSQL.delete(officeId)
    .run
    .transact(xa)
    .map { affectedRows =>
      if (affectedRows == 1) Right(())
      else Left(NotFoundError)
    }
  def get(officeId: Long): IO[Either[Errors, Office]] = OfficeSQL.select(officeId)
    .option
    .transact(xa)
    .map {
      case Some(office) => Right(office)
      case None => Left(NotFoundError)
    }

  def list(): IO[List[Office]] = OfficeSQL.selectAll
    .to[List]
    .transact(xa)

  def update(office: Office): IO[Either[Errors, Office]] = OfficeSQL.update(office)
    .run
    .transact(xa)
    .map { affectedRows =>
      if (affectedRows == 1) Right(office)
      else Left(NotFoundError)
    }
}
