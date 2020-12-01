// route.js
// This file defines all the routes used by the application as well as the login interceptor

import Vue from 'vue'
import Router from 'vue-router'
import About from './views/About.vue'
import User from './views/User.vue'
import Login from './views/Login.vue'
import Logout from './views/Logout.vue'
import NotFound from './views/NotFound.vue'
import store from './store'

Vue.use(Router)

var router = new Router({
  mode: 'history',
  base: '/',
  routes: [
    {
      path: '/',
      name: 'home',
      component: About,
      meta: { 
        requiresAuth: true,
        title: 'MCP - Home'
      },
    },
    {
      path: '/about',
      name: 'about',
      component: About,
      meta: {
        title: 'MCP - About'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        title: 'MCP - Login'
      }
    },
    {
      path: '/logout',
      name: 'logout',
      component: Logout,
      meta: { requiresAuth: true },
    },
    { path: '/404', name: '404', component: NotFound },
    { path: '*', redirect: '/404' },
    {
      path: '/users',
      name: 'users',
      component: User,
      meta: { 
        requiresAuth: true,
        title: 'MCP - Manage Users'
      },
    }
  ],
})

/**
 * beforeEach - Intercepts the route and directs user to login page if they need to be authenticated to access the target route
 * @param  {Route} to     - The target route
 * @param  {Route} from   - The route the user is coming from
 * @return {fn}    next   - The callback to direct to next route
 */
router.beforeEach((to, from, next) => {
  next()

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.getters.accessToken) {
      next({
        path: '/login',
        query: { redirect: to.fullPath },
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

// Returns the default router
export default router
