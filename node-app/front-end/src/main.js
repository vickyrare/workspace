import Vue from 'vue'
import vuetify from'./plugins/vuetify'
import App from './App.vue'
import router from './router'
import store from './store'
//import 'vuetify/dist/vuetify.min.css'
import './vuetify-overrides.scss'
import VueLayers from 'vuelayers'
import 'vuelayers/lib/style.css'
import VueObserveVisibility from 'vue-observe-visibility'

Vue.use(VueLayers, { dataProjection: 'EPSG:4326' })
Vue.use(require('vue-shortkey'))

Vue.use(VueObserveVisibility)

Vue.config.productionTip = false
Vue.config.devtools = false
const VUE_APP_API_URL = process.env.VUE_APP_API_URL

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App),
}).$mount('#app')
