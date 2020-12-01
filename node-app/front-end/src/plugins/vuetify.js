// vuetify.js
// Sets up the base Vuetify settings for the app

import Vue from 'vue'
import Vuetify, { VIcon, VSnackbar, VTextField, VTooltip, VToolbar, VCard, VBtn } from 'vuetify/lib'
import VuetifyToast from 'vuetify-toast-snackbar'
import '@/sass/variables.scss'
//import 'vuetify/dist/vuetify.min.css'
//import { TiptapVuetifyPlugin } from 'tiptap-vuetify'
//import 'tiptap-vuetify/dist/main.css'

import 'material-design-icons-iconfont/dist/material-design-icons.css'
import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify, {
  components: {
    VIcon,
    VSnackbar,
    VTextField,
    VTooltip,
    VToolbar,
    VCard,
    VBtn,
  }
});

const vueObj = new Vuetify({
  icons: {
    iconfont: 'mdi',  // 'mdi' || 'mdiSvg' || 'md' || 'fa' || 'fa4'
  },
  theme: {
    themes: {
      light: {
        primary: '#535353',
        secondary: '#303131',
        accent: '#82B1FF',
        error: '#FF5252',
        info: '#2196F3',
        success: '#4CAF50',
        warning: '#FFC107'
      },
    },
  },
  //theme: { disable: true }
})

// Vue.use(TiptapVuetifyPlugin, {
//   iconsGroup: 'md'
// })

export default vueObj;

Vue.use(VuetifyToast, { 
  $vuetify: vueObj.framework,
  x: 'center', // default
  y: 'top', // default
  color: 'info', // default
  icon: 'info',
  timeout: 3000, // default
  dismissable: true, // default
  autoHeight: true, // default
  multiLine: true, // default
  vertical: false, // default
  shorts: {
    custom: {
      color: 'purple',
    },
  },
  classes: ['body-2'],
  property: '$toast', // default
});
//import VueLayers from 'vuelayers'
//import 'vuelayers/lib/style.css'

//Vue.use(VueLayers)
