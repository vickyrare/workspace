require('dotenv').config();
var app = require('./app');
var server = app.listen(3000, function(){
  console.log("Server is running on port 3000");
});