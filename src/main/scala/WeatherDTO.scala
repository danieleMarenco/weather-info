import org.apache.spark.sql.{Encoder, Encoders}

case class WindDTO(
    speed: Double
)
case class SysDTO(
    country: String
)
case class MainDTO(
    temp: Double,
    pressure: Long,
    humidity: Long,
    temp_min: Double,
    temp_max: Double
)
case class CloudDTO(
    main: Array[String],
    icon: Array[String]
)

case class WeatherDTO(
    wind: WindDTO,
    sys: SysDTO,
    weather: CloudDTO,
    name: String,
    visibility: Long,
    main: MainDTO
)

object WeatherDTO {
  implicit val configEncoder: Encoder[WeatherDTO] = Encoders.product[WeatherDTO]
  implicit val configEncoder2: Encoder[WindDTO]   = Encoders.product[WindDTO]
  implicit val configEncoder3: Encoder[SysDTO]    = Encoders.product[SysDTO]
  implicit val configEncoder4: Encoder[MainDTO]   = Encoders.product[MainDTO]
  implicit val configEncoder5: Encoder[CloudDTO]  = Encoders.product[CloudDTO]
}
