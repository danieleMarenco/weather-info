
## Weather Info ##

#### How : ####

1. The application makes a GET call to a public API that returns IP and location information
2. analyzes the data with Spark and adds a URL column

![](ipStackAnalize.png)

3. I will need the URL column to call a second public service
   given latitude and longitude which will return the weather information.
```
{
   "coord":{
      "lon":9.2,
      "lat":45.47
   },
   "weather":[
      {
         "id":800,
         "main":"Clear",
         "description":"clear sky",
         "icon":"01d"
      }
   ],
   "base":"stations",
   "main":{
      "temp":32.25,
      "pressure":1009,
      "humidity":36,
      "temp_min":30.56,
      "temp_max":33.33
   },
   "visibility":10000,
   "wind":{
      "speed":2.6,
      "deg":240
   },
   "clouds":{
      "all":0
   },
   "dt":1564500226,
   "sys":{
      "type":1,
      "id":6742,
      "message":0.0086,
      "country":"IT",
      "sunrise":1564459456,
      "sunset":1564512890
   },
   "timezone":7200,
   "id":6542283,
   "name":"Milan",
   "cod":200
}
```

4. Analyze the weather information received with spark 
5. I expose the result with Swing (very simply) only for a visual matter.

#### Result: ####
![](result.png)


#### Why: ####
I introduced spark for a training, to improvement my skill with (dataSet and dataFrame)

you could expand this functionality for many IP addresses (the correct use of spark)
