import java.io.PrintWriter
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HttpClient {
  import akka.http.scaladsl.unmarshalling.Unmarshal
  implicit val system       = ActorSystem("http-client")
  implicit val materializer = ActorMaterializer()

  val spark = SparkSession.builder().appName("Spark test").master("local[*]").getOrCreate()

  def get(uri: String) = {
    val request = HttpRequest(HttpMethods.GET, uri)
    for {
      response <- Http().singleRequest(request)
      content  <- Unmarshal(response.entity).to[String]
    } yield content
  }

  def createdUrlByLocations(locations: DataFrame, uriLatLon: String)(implicit spark: SparkSession): DataFrame = {
    import org.apache.spark.sql.functions._
    import spark.implicits._

    val getReplacedUrl = udf((lati: String, longi: String) => {
      uriLatLon.replace("{latitudine}", lati).replace("{longitudine}", longi).replace("{appid}", "121324345656")
    })

    val newDataFrameWithUrl = locations.withColumn("url", getReplacedUrl($"latitude", $"longitude"))
    newDataFrameWithUrl
  }

  def callWeatherService(urlWeatherApi: String) = {
    val weatherResponse: String = Await.result(get(urlWeatherApi), 10.seconds)
    new PrintWriter("weatherResponse") { write(weatherResponse); close }

    val datasetResponse: Dataset[WeatherDTO] = spark.read.format("json").load("weatherResponse").select("*").as[WeatherDTO]
    datasetResponse.foreach(weatherInfo => SwingView.windowView(weatherInfo))
  }

  def start(): Unit = {
    val uriLocation = "http://api.ipstack.com/check?access_key=4d0bca2749c2c839bb1cf5956f1e9bdd"
    val uriGPS =
      "https://api.openweathermap.org/data/2.5/weather?lat={latitudine}&lon={longitudine}&appid=713f66bf9e4f033b6b6f15ec68373e13"

    val ipStack: String = Await.result(get(uriLocation), 10.seconds)
    new PrintWriter("ipStackResponse") { write(ipStack); close }
    val dataFrame = spark.read.format("json").load("ipStackResponse").select("ip", "latitude", "longitude")

    val onlyUrls: DataFrame = createdUrlByLocations(dataFrame, uriGPS)(spark)
    onlyUrls.foreach(dataFrameUrl => {
      val url: String = dataFrameUrl.getAs[String]("url")
      print(url)
      callWeatherService(url)
    })

    println("Shutting down...")
    Http().shutdownAllConnectionPools().foreach(_ => system.terminate)
  }

  def main(args: Array[String]): Unit = {
    HttpClient.start()
  }
}
