import java.awt.{BorderLayout, Dimension, Image}
import javax.imageio.ImageIO
import java.net.URL
import javax.swing.{ImageIcon, JFrame, JLabel, JPanel}

object SwingView extends App {

  def windowView(info: WeatherDTO) = {
    val url        = new URL("http://openweathermap.org/img/w/" + info.weather.icon.head + ".png")
    val buffImmage = ImageIO.read(url).getScaledInstance(100, 100, Image.SCALE_DEFAULT)
    val imageIcon  = new ImageIcon(buffImmage)

    val panel = new JPanel()
      .add(new JLabel(imageIcon))

    val frame = new JFrame("\t" + info.name + "\t" + info.sys.country)

    val contentPane = frame.getContentPane()
    contentPane.setLayout(new BorderLayout())
    contentPane.add(new JLabel("wind speed: " + info.wind.speed), BorderLayout.NORTH)
    contentPane.add(new JLabel("temperature: " + info.main.temp), BorderLayout.WEST)
    contentPane.add(new JLabel("pressure: " + info.main.pressure), BorderLayout.EAST)
    contentPane.add(new JLabel("humidity: " + info.main.humidity), BorderLayout.SOUTH)

    contentPane.add(panel, BorderLayout.CENTER)

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(new Dimension(400, 215))
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
  }

}
