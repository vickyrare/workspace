const request = require('request')
const jwkToPem = require('jwk-to-pem');
const jwt = require('jsonwebtoken');

global.fetch = require('node-fetch');
global.navigator = () => null;
const AmazonCognitoIdentity = require('amazon-cognito-identity-js');
const poolData = {
  UserPoolId: process.env.AWS_USER_POOL_ID,
  ClientId: process.env.AWS_CLIENT_ID
};
const userPool = new AmazonCognitoIdentity.CognitoUserPool(poolData);

var cognitoUser;

exports.Register = function (body, callback) {
  console.log(body)
  var name = body.name;
  var given_name = body.given_name;
  var family_name = body.family_name;
  var picture = body.picture;
  var email = body.email;
  var password = body.password;
  var attributeList = [];

  attributeList.push(new AmazonCognitoIdentity.CognitoUserAttribute({ Name: "email", Value: email }));
  attributeList.push(new AmazonCognitoIdentity.CognitoUserAttribute({ Name: "given_name", Value: given_name }));
  attributeList.push(new AmazonCognitoIdentity.CognitoUserAttribute({ Name: "family_name", Value: family_name }));
  attributeList.push(new AmazonCognitoIdentity.CognitoUserAttribute({ Name: "picture", Value: picture }));
  userPool.signUp(name, password, attributeList, null, function (err, result) {
    if (err)
      callback(err);
    var cognitoUser = result.user;
    callback(null, cognitoUser);
  })
}

exports.Login = function (body, callback) {
  var userName = body.username;
  var password = body.password;
  var authenticationDetails = new AmazonCognitoIdentity.AuthenticationDetails({
    Username: userName,
    Password: password
  });
  var userData = {
    Username: userName,
    Pool: userPool
  }
  cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);
  cognitoUser.authenticateUser(authenticationDetails, {
    onSuccess: function (result) {
      var idToken = result.getIdToken().getJwtToken();
      var accessToken = result.getAccessToken().getJwtToken();
      var refreshToken = result.getRefreshToken().getToken();

      var res = {
        "id_token": idToken,
        "access_token": accessToken,
        "refresh_token": refreshToken,
      }
      callback(null, res);
    },
    onFailure: (function (err) {
      callback(err);
    })
  })
};

exports.Logout = function (body, callback) {
  cognitoUser.globalSignOut({
    onSuccess: function (result) {
      callback(null, result);
    },
    onFailure: (function (err) {
      callback(null, err);
    })
  })
};

