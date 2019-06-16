package postoffice.infra.endpoints
import cats.effect.IO
import cats.data._
import postoffice.domain.Errors._
import postoffice.infra.db.ShipmentDAO
import postoffice.domain.shipments.{Shipment, ShipmentType}
import io.circe.syntax._
import org.http4s.{HttpService, MediaType, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import org.http4s.headers.{Location, `Content-Type`}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._


case class ShipmentEndpoint(dao: ShipmentDAO) extends Http4sDsl[IO] {

  private implicit val encodeShipmentType: Encoder[ShipmentType] = Encoder.encodeString.contramap[ShipmentType](_.value)
  private implicit val decodeShipmentType: Decoder[ShipmentType] = Decoder.decodeString.map[ShipmentType](ShipmentType.unsafeFromString)

  implicit val decodeOffice: Decoder[Shipment] = deriveDecoder[Shipment]
  implicit val encodeOffice: Encoder[Shipment] = deriveEncoder[Shipment]

  val service = HttpService[IO] {

      case GET -> Root / "shipments" =>
        for {
          getResult <- dao.list
          response <- Ok(getResult.asJson)
        } yield response

      case req @ POST -> Root / "shipments" =>
        for {
          office <- req.decodeJson[Shipment]
          updateResult <- dao.create(office)
          response <- result(updateResult)
        } yield response

      case req @ PUT -> Root / "shipments" =>
        for {
          office <- req.decodeJson[Shipment]
          updateResult <- dao.update(office)
          response <- result(updateResult)
        } yield response

      case DELETE -> Root / "shipments" / LongVar(id) =>
        dao.delete(id).flatMap {
          case Right(_) => NoContent()
          case Left(NotFoundError) => NotFound(NotFoundError.message)
          case Left(c:Errors) => InternalServerError(c.message)
          case Left(e) => InternalServerError(e.toString)
        }
    }

  def result(result: Either[Errors, Shipment]) = {
    result match {
      case Left(NotFoundError) => NotFound(NotFoundError.message)
      case Left(UniqueConstraintError) => InternalServerError(UniqueConstraintError.message)
      case Left(c:CustomError) => InternalServerError(c.message)
      case Left(e) => InternalServerError(e.toString)
      case Right(item) => Ok(item.asJson)
    }
  }
}
