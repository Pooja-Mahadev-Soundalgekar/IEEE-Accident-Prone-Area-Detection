# Graph  on Accidents Paris 

## Usage ##


```bash
docker-compose build
docker-compose up
```

When all is containers have started OK, drop ./source/back/accidentology23.csv to to ./source that will trigger the filebeat shipper.

Open a browser 192.168.99.100:5601 and setup the index pattern  "accidentology". Check all events are there (there should be around 13.6K )

Play with the plugin Graph
