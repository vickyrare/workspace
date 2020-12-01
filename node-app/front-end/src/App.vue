<template>
  <v-app>
      <loading
        :active.sync="isLoading"
        :can-cancel="false"
        :is-full-page="true"
      ></loading>
      <v-layout align-start>
        <SideMenu v-if="showSideMenu" />
        <router-view :key="$route.fullPath"></router-view>
      </v-layout>
  </v-app>
</template>

<script>
import SideMenu from './components/SideMenu'
import Loading from 'vue-loading-overlay'
import 'vue-loading-overlay/dist/vue-loading.css'
import { mapActions } from 'vuex'

export default {
  name: 'App',
  components: {
    Loading,
    SideMenu,
  },
  computed: {
    isLoading: {
      get() {
        return this.$store.state.loading > 0
      },
    },
    forcePasswordReset: {
      get() { return this.$store.state.forcePasswordReset },
    },
    showSideMenu() {
      if (this.$route.name == null) return false
      if (this.$route.name.indexOf('login') >= 0) return false
      if (this.$route.name.indexOf('logout') >= 0) return false
      return true
    },
  },
  data: () => ({}),
  ...mapActions(['getForcePasswordReset']),

  methods: {},
  watch: {
      '$route':{
        handler: (to, from) => {
          document.title = to.meta.title || 'MCP'
        },
         immediate: true
      }
    },
}

</script>

<style>
.v-btn {
  text-transform: none;
}
.v-input__slot {
  cursor: pointer !important;
}

.v-list__item__action {
  cursor: pointer !important;
}
.pageTitle {
  height: 41px;
  opacity: 0.87;
  font-size: 34px;
  font-weight: 500;
  font-style: normal;
  font-stretch: normal;
  line-height: normal;
  letter-spacing: normal;
}

@media only screen and (min-width: 1264px) {
  .container {
    max-width: 100%;
  }
}

.v-data-table > .v-data-table__wrapper > table > tbody > tr > td, .v-data-table > .v-data-table__wrapper > table > thead > tr > td, .v-data-table > .v-data-table__wrapper > table > tfoot > tr > td {
  font-family: 'Roboto';
  font-weight: 400;
  font-size: 13px;
}

button:focus { 
  outline: 2px dashed blue;
}
button:focus:not(:focus-visible) {
  outline: none;
}
</style>
