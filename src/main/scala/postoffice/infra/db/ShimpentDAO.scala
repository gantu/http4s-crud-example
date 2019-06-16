package postoffice.infra.db

import postoffice.domain.shipments.{Shipment, ShipmentType}
import postoffice.domain.CommonRepoAlgebra
import cats.data.OptionT
import cats.implicits._
import doobie.implicits._
import doobie._
import cats._
import cats.effect.IO
import postoffice.domain.Errors._
import doobie.util.transactor.Transactor

private object ShipmentSQL {
  implicit val shipmentTypeMeta: Meta[ShipmentType] = Meta[String].xmap(ShipmentType.unsafeFromString(_), _.value)
  def select(shipmentId: Long): Query0[Shipment] = sql"SELECT id,office_id,shipment_type FROM shipments WHERE ID=$shipmentId".query[Shipment]

  def insert(shipment: Shipment): Update0 = sql"INSERT INTO shipments(office_id, shipment_type) VALUES(${shipment.officeId}, ${shipment.shipmentType})".update

  def delete(shipmentId: Long): Update0 = sql"DELETE FROM shipments WHERE id=$shipmentId".update

  def selectAll: Query0[Shipment] = sql"SELECT * FROM shipments".query[Shipment]

  def update(shipment: Shipment): Update0 = sql"UPDATE SHIPMENTS SET office_id=${shipment.officeId}, shipment_type=${shipment.shipmentType} WHERE ID=${shipment.id}".update

}
case class ShipmentDAO(val xa: Transactor[IO]) extends CommonRepoAlgebra[Shipment] {
  import ShipmentSQL._


  def create(shipment: Shipment): IO[Either[Errors, Shipment]] = ShipmentSQL.insert(shipment)
    .withUniqueGeneratedKeys[Long]("id")
    .attemptSomeSqlState{
      case SqlState("23505") => UniqueConstraintError
      case state => CustomError(state)
    }
    .transact(xa)
    .map {
      case Right(id: Long) => Right(shipment.copy(id = Some(id)))
      case Left(error) => Left(error)
    }
  def delete(shipmentId: Long): IO[Either[Errors, Unit]] = ShipmentSQL.delete(shipmentId)
    .run
    .transact(xa)
    .map { affectedRows =>
      if (affectedRows == 1) Right(())
      else Left(NotFoundError)
    }
  def get(shipmentId: Long): IO[Either[Errors, Shipment]] = ShipmentSQL.select(shipmentId)
    .option
    .transact(xa)
    .map {
      case Some(shipment) => Right(shipment)
      case None => Left(NotFoundError)
    }

  def list(): IO[List[Shipment]] = ShipmentSQL.selectAll
    .to[List]
    .transact(xa)

  def update(shipment: Shipment): IO[Either[Errors, Shipment]] = ShipmentSQL.update(shipment)
    .run
    .transact(xa)
    .map { affectedRows =>
      if (affectedRows == 1) Right(shipment)
      else Left(NotFoundError)
    }
}
