package postoffice.infra.endpoints
import cats.effect.IO
import cats.data._
import postoffice.domain.Errors._
import postoffice.infra.db.OfficeDAO
import postoffice.domain.offices.Office
import io.circe.syntax._
import org.http4s.{HttpService, MediaType, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import org.http4s.headers.{Location, `Content-Type`}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._


case class OfficeEndpoint(dao: OfficeDAO) extends Http4sDsl[IO] {

  implicit val decodeOffice: Decoder[Office] = deriveDecoder[Office]
  implicit val encodeOffice: Encoder[Office] = deriveEncoder[Office]

  val service = HttpService[IO] {

      case GET -> Root / "offices" =>
        for {
          getResult <- dao.list
          response <- Ok(getResult.asJson)
        } yield response

      case req @ POST -> Root / "offices" =>
        for {
          office <- req.decodeJson[Office]
          updateResult <- dao.create(office)
          response <- result(updateResult)
        } yield response

      case GET -> Root / "offices" / LongVar(id) =>
        for {
          getResult <- dao.get(id)
          response <- result(getResult)
        } yield response

      case req @ PUT -> Root / "offices" =>
        for {
          office <- req.decodeJson[Office]
          updateResult <- dao.update(office)
          response <- result(updateResult)
        } yield response

      case DELETE -> Root / "offices" / LongVar(id) =>
        dao.delete(id).flatMap {
          case Right(_) => NoContent()
          case Left(NotFoundError) => NotFound(NotFoundError.message)
          case Left(c:Errors) => InternalServerError(c.message)
          case Left(e) => InternalServerError(e.toString)
        }
    }

  def result(result: Either[Errors, Office]) = {
    result match {
      case Left(NotFoundError) => NotFound(NotFoundError.message)
      case Left(UniqueConstraintError) => InternalServerError(UniqueConstraintError.message)
      case Left(c:CustomError) => InternalServerError(c.message)
      case Left(e) => InternalServerError(e.toString)
      case Right(item) => Ok(item.asJson)
    }
  }

}
