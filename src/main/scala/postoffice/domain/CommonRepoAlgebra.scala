package postoffice.domain

import cats.effect.IO
import postoffice.domain.Errors._

trait CommonRepoAlgebra[T] {
  def create(data: T): IO[Either[Errors, T]]
  def update(data: T): IO[Either[Errors, T]]
  def delete(id: Long): IO[Either[Errors, Unit]]
  def get(id: Long): IO[Either[Errors, T]]
  def list(): IO[List[T]]
}
