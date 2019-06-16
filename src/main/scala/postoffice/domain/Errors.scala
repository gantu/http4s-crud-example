package postoffice.domain

import doobie.enum.SqlState
import org.http4s.Status

object Errors {

  abstract class Errors(val message:String){}

  object NotFoundError extends Errors("Not Found")

  object UniqueConstraintError extends Errors("Duplicate data")

  case class CustomError(state: SqlState) extends Errors("SQL error: " + state.value)

}
