const express = require('express')
const app = express()
const cors = require('cors')
const port = 8083

let appInsights = require("applicationinsights");
appInsights.setup().start();

app.use(cors())
app.get('/schedule', (req, res) => {

  let r = Math.floor(Math.random() * 10);

  if (r < 5) {
    let data = [
      { "id": 1, "time": "10:00-11:00", "title": "お客様と打ち合わせ" },
      { "id": 2, "time": "14:00-18:00", "title": "外出" }
    ];
    console.log("Schedule API success")
    res.header('Content-Type', 'application/json; charset=utf-8')
    res.status(200)
    res.send(data)

  } else {
    console.log("Schedule API error")
    res.header('Content-Type', 'application/json; charset=utf-8')
    res.status(500)
    res.send({"message": "error"})
  }

})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})
