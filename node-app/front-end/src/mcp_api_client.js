// mcp_api_client.js
// The client manages access to the MCP API including token authentication

import axios from 'axios'
import jwt from "jsonwebtoken";
import store from "./store";
import router from "./router";


// This is the root URL for the backend API
const VUE_APP_API_URL = process.env.VUE_APP_API_URL

// Login calls can of type password or refresh - This module uses the refresh type
const GRANT_TYPE_REFRESH = 'refresh_token'

// This is the root API object that handles the AJAX calls to the API
const api = axios.create({
  baseURL: VUE_APP_API_URL,
  timeout: 20000,
  params: {}
});

var authTokenRequest = null

/**
 * getAuthToken - Handle Auth failures that require refresh token
 * @return {Promise}
 */
function getAuthToken() {
  var currentToken = store.getters['accessToken']
  var tokenParsed = jwt.decode(currentToken, { complete: true })
  var refreshToken = store.getters['refreshToken']
  var refreshTokenParsed = jwt.decode(refreshToken, { complete: true })
  // if the current access token expires soon - Next 240 seconds
  if (tokenParsed.payload.exp - 240 <= (Date.now() / 1000).toFixed(0)) {
    // if not already requesting a token
    if (authTokenRequest === null) {
      var payload = {
        "grant_type": GRANT_TYPE_REFRESH,
        "refresh_token": refreshToken
      }
      authTokenRequest = api.post('/refresh', payload, { withCredentials: true })
        .then(response => {
          // request complete and store
          resetAuthTokenRequest()
          store.commit('SET_ACCESS_TOKEN', response.data.access_token)
          store.commit('SET_REFRESH_TOKEN', response.data.refresh_token)
          return response.data.access_token
        })
    }
    return authTokenRequest
  }
  return store.getters['accessToken']
}

// tokenRequest dirty bit reseter
function resetAuthTokenRequest () {
  authTokenRequest = null
}

// Axios Intercept Requests
api.interceptors.request.use(async function (config) {
  config.headers['Content-Type'] = 'application/json'
  // If not one of these specific pages that doesn't need a token, use method to get the current token or request a new one.  Otherwise, use current token (possibly none)
  if (!config.url.includes('login') && !config.url.includes('refresh') && !config.url.includes('forgot_password') && !config.url.includes('reset_password') && !config.url.includes('activate') && !config.url.includes('status')) {
    config.headers['Authorization'] = 'Bearer ' + await getAuthToken()
  } else if (!config.url.includes('login')) {
    config.headers['Authorization'] = 'Bearer ' + store.getters['accessToken']
  }
  store.commit('INCREMENT_LOADING')
  return config
}, function (error) {
  return Promise.reject(error)
})

// Interceptor for the response coming back from the API
api.interceptors.response.use(function (config) {
  store.commit('DECREMENT_LOADING')
  return config
}, function (error) {
  store.commit('DECREMENT_LOADING')
  // Prevent endless redirects (login is where you should end up)
  if (error.request !== undefined) {
    if (error.request.responseURL.includes('login') || error.request.responseURL.includes('logout') || error.request.responseURL.includes('refresh')) {
      store.commit('SET_ACCESS_TOKEN', '')
      store.commit('SET_REFRESH_TOKEN', '')
      
      if (! router.currentRoute.path.includes('login')) {
        router.push({name: 'login'})
      }
      return Promise.reject(error)
    }
  }

  // If you can't refresh your token or you are sent Unauthorized on any request, logout and go to login
  if (error.request !== undefined && (error.request.responseURL.includes('refresh') || error.request.status === 401 && error.config.__isRetryRequest)) {
    store.dispatch('logout')
    router.push({name: 'login'})
  } else if (error.request !== undefined && error.request.status === 401) {
    error.config.__isRetryRequest = true
    return api.request(error.config)
  }
  return Promise.reject(error)
})

export default api
