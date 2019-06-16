package postoffice.domain.shipments

case class Shipment (
  id: Option[Long],
  officeId: Int,
  shipmentType: ShipmentType
 )
