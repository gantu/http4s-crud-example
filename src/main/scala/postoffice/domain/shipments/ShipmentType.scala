package postoffice.domain.shipments

abstract sealed class ShipmentType(val value: String)
case object Letter extends ShipmentType("letter")
case object Package extends ShipmentType("package")

object ShipmentType {
  private def values = Set(Letter, Package)

    def unsafeFromString(value: String): ShipmentType = {
      values.find(_.value == value).get
    }
}
