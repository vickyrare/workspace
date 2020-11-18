const request = require('request')
const jwkToPem = require('jwk-to-pem');
const jwt = require('jsonwebtoken');

exports.Validate = function (req, res, next) {
  var token = req.headers['authorization'];
  const pool_region = process.env.AWS_POOL_REGION;
  const poolData = {
    UserPoolId: process.env.AWS_USER_POOL_ID,
    ClientId: process.env.AWS_CLIENT_ID
  };
  request({
    url: `https://cognito-idp.${pool_region}.amazonaws.com/${poolData.UserPoolId}/.well-known/jwks.json`,
    json: true
  }, function (error, response, body) {
    if (!error && response.statusCode === 200) {
      pems = {};
      var keys = body['keys'];
      for (var i = 0; i < keys.length; i++) {
        var key_id = keys[i].kid;
        var modulus = keys[i].n;
        var exponent = keys[i].e;
        var key_type = keys[i].kty;
        var jwk = {kty: key_type, n: modulus, e: exponent};
        var pem = jwkToPem(jwk);
        pems[key_id] = pem;
      }
      var decodedJwt = jwt.decode(token, {complete: true});
      if (!decodedJwt) {
        console.log("Not a valid JWT token");
        res.status(401);
        return res.send("Invalid token");
      }
      var kid = decodedJwt.header.kid;
      var pem = pems[kid];
      if (!pem) {
        console.log('Invalid token');
        res.status(401);
        return res.send("Invalid token");
      }
      jwt.verify(token, pem, function (err, payload) {
        if (err) {
          console.log("Invalid Token.");
          res.status(401);
          return res.send("Invalid token");
        } else {
          console.log("Valid Token.");
          return next();
        }
      });
    } else {
      console.log("Error! Unable to download JWKs");
      res.status(500);
      return res.send("Error! Unable to download JWKs");
    }
  });
}
