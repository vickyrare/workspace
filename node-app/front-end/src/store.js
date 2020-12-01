// store.js
// The store defines the data state for the application is is managed by vuex

import Vue from 'vue'
import Vuex from 'vuex'
import api from './mcp_api_client'

Vue.use(Vuex)

// This is the root URL for the backend API
const VUE_APP_API_URL = process.env.VUE_APP_API_URL

// Login calls can of type password or refresh - This module uses the password type
const GRANT_TYPE_PASSWORD = 'password'

/**
 * initialState - Creates initial state
 * @return {Map}      the initial state to use
 */
function initialState() {
  return {
    accessToken: localStorage.accessToken, // The login access token - persisted to browser storage while logged in
    refreshToken: localStorage.refreshToken, // The login refresh token to use to refresh the access token - persisted to browser storage while logged in
    id: localStorage.id, // Logged in users id
    firstName: localStorage.firstName, // The firstname - persisted to browser storage while logged in
    lastName: localStorage.lastName, // The lastname - persisted to browser storage while logged in
    loading: 0, // When set to true shows the wait spinner
    message: {
      text: '',
      status: '',
    },
    snackbar: false,
    email: localStorage.email, // Logged in users email
    password: '', // Temporarily stored in Login form,
    menuItems: localStorage.menuItems ? JSON.parse(localStorage.menuItems) : [], // The list of allowed menu items
    pidList: [], // The list of PIDs
    userList: [], // The list of Users
    searchText: localStorage.searchText, // Text for search filter
    searchCriteria: {}, // Criteria for work order search filter
    // What Map renderer should we use
    renderChoice: localStorage.renderChoice
      ? localStorage.renderChoice
      : 'canvas'
  }
}

// Returns the default store
export default new Vuex.Store({
  state: initialState,
  getters: {
    accessToken: state => state.accessToken,
    refreshToken: state => state.refreshToken,
    message: state => state.message,
    snackbar: state => state.snackbar,
    id: state => state.id,
    firstName: state => state.firstName,
    lastName: state => state.lastName,
    email: state => state.email,
    phone: state => state.phone,
    password: state => state.password,
    menuItems: state => state.menuItems,
    userList: state => state.userList,
    searchText: state => state.searchText,
    renderChoice: state => state.renderChoice
  },
  mutations: {
    INCREMENT_LOADING(state) {
      state.loading++
    },
    DECREMENT_LOADING(state) {
      state.loading--
    },
    SET_EMAIL(state, email) {
      state.email = email
      localStorage.email = email
    },
    SET_PASSWORD(state, password) {
      state.password = password
    },
    SET_ACCESS_TOKEN(state, accessToken) {
      state.accessToken = accessToken
      localStorage.accessToken = accessToken
    },
    SET_REFRESH_TOKEN(state, refreshToken) {
      state.refreshToken = refreshToken
      localStorage.refreshToken = refreshToken
    },
    SET_ID(state, id) {
      state.id = id
      localStorage.id = id
    },
    SET_FIRST_NAME(state, firstName) {
      state.firstName = firstName
      localStorage.firstName = firstName
    },
    SET_LAST_NAME(state, lastName) {
      state.lastName = lastName
      localStorage.lastName = lastName
    },
    SET_MENU_ITEMS(state, menuItems) {
      state.menuItems = menuItems
      localStorage.menuItems = JSON.stringify(menuItems)
    },
    SET_USER_LIST(state, userList) {
      state.userList = userList
    },
    ADD_USER(state, user) {
      user.display_role = user.role.replace(/(Admin|User|Super)/g, ' $1').trim()
      state.userList.push(user)
    },
    SET_SEARCH_TEXT(state, text) {
      state.searchText = text
      localStorage.searchText = state.searchText
    },
    SET_SHOW_MINI_MENU(state, show) {
      state.showMiniMenu = show
      localStorage.showMiniMenu = show.toString()
    },
    UPDATE_USER(state, updated_user) {
      state.userList.map(function(user, index) {
        if (user.user_id == updated_user.user_id) {
          updated_user.role = updated_user.role.role_name
          state.userList.splice(index, 1, updated_user)
          return
        }
      })
    },
  },
  actions: {
    setAccessToken({ commit, state, accessToken }) {
      commit('SET_ACCESS_TOKEN', accessToken)
    },
    setRefreshToken({ commit, state, refreshToken }) {
      commit('SET_REFRESH_TOKEN', refreshToken)
    },
    setId({ commit, state }, id) {
      commit('SET_ID', id)
    },
    setFirstname({ commit, state, firstName }) {
      commit('SET_FIRST_NAME', firstName)
    },
    setLastname({ commit, state, lastName }) {
      commit('SET_LAST_NAME', lastName)
    },
    setEmail({ commit, state }, email) {
      commit('SET_EMAIL', email)
    },
    setPassword({ commit, state }, password) {
      commit('SET_PASSWORD', password)
    },
    setUserList({ commit, state, userList }) {
      commit('SET_USER_LIST', userList)
    },
    setSearchText({ commit, state }, text) {
      //PTV-5066: Clearing search box with X button then refreshing page results in the word "null" being the search term
      // vuetify clearable sets v-model to null, so explicitly set text to empty string
      if (text == null) {
        text = ''
      }
      commit('SET_SEARCH_TEXT', text)
    },
    setShowMiniMenu({ commit, state }, show) {
      commit('SET_SHOW_MINI_MENU', show)
    },
    setRenderChoice({ commit, state }, choice) {
      commit('SET_RENDER_CHOICE', choice)
    },
    /**
     * login - Handles the login api call
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    login({ commit, state }) {
      var data = {
        grant_type: GRANT_TYPE_PASSWORD,
        email: state.email,
        password: state.password,
      }
      return new Promise((resolve, reject) => {
        api
          .post('/login', data)
          .then(response => {
            commit('SET_ACCESS_TOKEN', response.data.access_token)
            commit('SET_REFRESH_TOKEN', response.data.refresh_token)
            commit('SET_ID', response.data.user_id)
            commit('SET_FIRST_NAME', response.data.first_name)
            commit('SET_LAST_NAME', response.data.last_name)
            commit('SET_EMAIL', response.data.email)
            commit('SET_PASSWORD', '')
            commit('SET_MENU_ITEMS', ['Manage Users', 'Update password'])
            //commit('SET_MENU_ITEMS', response.data.menuitems)
            resolve()
          })
          .catch(function(error) {
              console.log(error)
              reject('Login failed')
          })
      })
    },
    /**
     * logout - Handles the logout api call
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    logout({ commit, state }) {
      return new Promise((resolve, reject) => {
        api
          .get('/logout')
          .then(response => {
            commit('SET_ACCESS_TOKEN', '')
            commit('SET_REFRESH_TOKEN', '')
            commit('SET_FIRST_NAME', '')
            commit('SET_LAST_NAME', '')
            commit('SET_EMAIL', '')
            commit('SET_ID', '')
            commit('SET_MENU_ITEMS', [])
            resolve()
          })
          .catch(function(error) {
            // Even if logout fails we are still gonna wipe out our copy of credentials on the client
            commit('SET_ACCESS_TOKEN', '')
            commit('SET_REFRESH_TOKEN', '')
            commit('SET_FIRST_NAME', '')
            commit('SET_LAST_NAME', '')
            commit('SET_EMAIL', '')
            commit('SET_ID', '')
            commit('SET_MENU_ITEMS', [])
            // Swallow reject on logout
            // reject()
          })
      })
    },
    /**
     * downloadUsers - Downloads Users via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    downloadUsers({ commit, state }) {
      return new Promise((resolve, reject) => {
        api
          .get('/users')
          .then(response => {
            var userList = response.data.users
            userList.map(u => u.role = u.role.role_name)

            commit('SET_USER_LIST', userList)
            resolve()
          })
          .catch(function(error) {
            console.error('Download Users failed: ', error)
            reject()
          })
      })
    },
    /**
     * updatePassword - Updates User password via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    updatePassword({ commit, state }, password) {
      return new Promise((resolve, reject) => {
        api
          .post('/auth/changepassword', password)
          .then(response => {
            resolve()
          })
          .catch(function(error) {
            if (error.response.data.error) {
              reject(error.response.data.error)
            } else {
              reject(error.response.data)
            }
          })
      })
    },
    /**
     * addUser - Add User via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    addUser({ commit, state }, user) {
      return new Promise((resolve, reject) => {
        api
          .post('/user', user)
          .then(response => {
            commit('ADD_USER', response.data)
            resolve()
          })
          .catch(function(error) {
            reject(error.response.data.error)
          })
      })
    },
    /**
     * editUser - Edit User via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    editUser({ commit, state }, user) {
      return new Promise((resolve, reject) => {
        api
          .put('/users/' + user.user_id, user)
          .then(response => {
            commit('UPDATE_USER', response.data)
            resolve()
          })
          .catch(function(error) {
            console.error('Edit User failed: ', error.response.data.error)
            reject(error.response.data.error)
          })
      })
    },
    /**
     * activateUser - Activate User via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    activateUser({ commit, state }, user) {
      return new Promise((resolve, reject) => {
        api
          .get('/user/activate/' + user.email)
          .then(response => {
            user.active = 1
            commit('UPDATE_USER', user)
            resolve()
          })
          .catch(function(error) {
            console.error('Activate User failed: ', error.response.data)
            reject()
          })
      })
    },
    /**
     * deactivateUser - Deactivate User via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    deactivateUser({ commit, state }, user) {
      return new Promise((resolve, reject) => {
        api
          .get('/user/deactivate/' + user.email)
          .then(response => {
            user.active = 0
            commit('UPDATE_USER', user)
            resolve()
          })
          .catch(function(error) {
            console.error('Deactivate User failed: ', error.response.data)
            reject()
          })
      })
    },
    /**
     * resetPassword - Reset password for a user via the API
     * @param  {fn} commit State commit callback
     * @param  {Map} state Current state
     * @return {Promise}   AJAX response callback
     */
    resetPassword({ commit, state }, user) {
      return new Promise((resolve, reject) => {
        api
          .get('/user/resetpassword/' + user.email)
          .then(response => {
            resolve()
          })
          .catch(function(error) {
            console.error('Reset Password failed: ', error.response.data)
            reject()
          })
      })
    },
  },
})
