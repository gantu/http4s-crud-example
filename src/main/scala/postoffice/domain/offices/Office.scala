package postoffice.domain.offices


case class Office(
  id: Option[Long],
  zip: String,
  name: String
)
