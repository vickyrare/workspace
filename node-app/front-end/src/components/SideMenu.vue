<template>
  <span>
    <v-navigation-drawer
      dark
      v-model="drawer"
      permanent
      class="full-height"
      transition="slide-x-transition"
      mini-variant-width="80"
      width="300"
      :mini-variant.sync="showMiniMenu"
      hide-overlay
      stateless
      app
    >
      <span class="menu-layout">
        <v-list
          v-shortkey="['ctrl', 'q']"
          @shortkey.native="scmVersionShown = !scmVersionShown"
        >
          <v-list-item>
            <v-list-item-title class="title" v-if="!showMiniMenu">
              {{ VUE_APP_ABBR_NAME }}
            </v-list-item-title>
            <v-list-item-action>
              <v-btn
                icon
                @click.stop="showMiniMenu = !showMiniMenu"
                :aria-label="showMiniMenu ? 'Expand Sidebar' : ' Collapse Sidebar'"
              >
                <v-icon v-if="showMiniMenu">arrow_forward</v-icon>
                <v-icon v-else>arrow_back</v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
                <v-list-item-subtitle v-if="!showMiniMenu">
                  {{ VUE_APP_SHORT_NAME }}<br />
                  v {{ VUE_APP_VERSION }} {{ scmVersionShown ? VUE_APP_SCM_VERSION : '' }}
                </v-list-item-subtitle>
              </v-list-item-content>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list dense class="pt-0">
          <template v-for="item in menuItems">
            <v-list-item
            exact
              :key="item.title"
              :to="item.route == null ? undefined : { name: item.route }"
              active-class="active-menu-item"
            >
              <v-list-item-action>
                <v-icon size="21px">{{ item.icon }}</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-action-text v-if="item.button">
                  <v-btn
                    color="primary"
                    rounded
                    small
                    dark
                    class="menu-button"
                    :aria-label="item.title"
                    >{{ item.title }}</v-btn
                  >
                </v-list-item-action-text>
                <v-list-item-title v-else>{{ item.title }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </template>
        </v-list>
        <v-spacer></v-spacer>
        <v-list dense class="pt-0">
          <v-list-item>
            <v-list-item-action>
              <v-icon size="21px">perm_identity</v-icon>
            </v-list-item-action>
            <v-list-item-content>{{ fullName }}</v-list-item-content>
          </v-list-item>
          <template v-for="item in userMenuItems">
            <v-list-item
              :key="item.title"
              :to="item.route == null ? undefined : { name: item.route }"
              active-class="active-menu-item"
            >
              <v-list-item-action>
                <v-icon size="21px">{{ item.icon }}</v-icon>
              </v-list-item-action>

              <v-list-item-content>
                <v-list-item-action-text v-if="item.button">
                  <v-btn
                    v-if="item.title === 'Update password'"
                    color="primary"
                    rounded
                    small
                    class="menu-button"
                    aria-label="Update password"
                    @click="updatePasswordDialog = true"
                    ><UpdatePasswordDialog :dialog.sync="updatePasswordDialog"
                  /></v-btn>
                  <v-btn
                    v-else-if="item.title === 'Manage Alerts'"
                    color="primary"
                    rounded
                    small
                    dark
                    class="menu-button"
                    aria-label="Manage Alerts"
                    @click="manageAlertsDialog = true"
                  ><ManageAlertsDialog :dialog.sync="manageAlertsDialog"
                  /></v-btn>
                  <v-btn
                    v-else
                    color="primary"
                    rounded
                    small
                    dark
                    class="menu-button"
                    :aria-label="item.title"
                    >{{ item.title }}</v-btn
                  >
                </v-list-item-action-text>
                <v-list-item-title v-else>{{ item.title }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </template>
          <v-list-item href="/about" active-class="active-menu-item">
            <v-list-item-action>
              <v-icon>info</v-icon>
            </v-list-item-action>
            <v-list-item-content>About</v-list-item-content>
          </v-list-item>
          <v-list-item href="/logout" active-class="active-menu-item">
            <v-list-item-action>
              <v-icon></v-icon>
            </v-list-item-action>
            <v-list-item-content>Logout</v-list-item-content>
          </v-list-item>
        </v-list>
      </span>
    </v-navigation-drawer>
  </span>
</template>

<script>
// Shows the expanding collapsing Side menu (based on a Navigation Drawer)
import { mapActions } from 'vuex'

import UpdatePasswordDialog from './UpdatePasswordDialog'

export default {
  name: 'SideMenu',
  components: {
    UpdatePasswordDialog
  },
  computed: {
    fullName: {
      get() {
        return this.$store.state.firstName + ' ' + this.$store.state.lastName
      },
    },
    isExternalUser: {
      get() {
        return this.$store.state.isExternalUser
      },
    },
    email: {
      get() {
        return this.$store.state.email
      },
      set(value) {
        this.setEmail(value)
      },
    },
    showMiniMenu: {
      get() {
        return this.$store.state.showMiniMenu
      },
      set(value) {
        this.setShowMiniMenu(value)
      },
    },
    menuItems: {
      get() {
        var allowedMenuItems = this.$store.state.menuItems
        allowedMenuItems = allowedMenuItems.map(a => a.toLowerCase())
        return this.items.filter(i =>
          allowedMenuItems.includes(i.title.toLowerCase()) && i.title !== 'Update password'
        )
      },
    },
    userMenuItems: {
      get() {
        var allowedMenuItems = this.$store.state.menuItems
        allowedMenuItems = allowedMenuItems.map(a => a.toLowerCase())
        return this.items.filter(i =>
          allowedMenuItems.includes(i.title.toLowerCase()) && (i.title === 'Update password')
        )
      },
    },
  },
  data() {
    return {
      VUE_APP_SHORT_NAME: process.env.VUE_APP_SHORT_NAME,
      VUE_APP_ABBR_NAME: process.env.VUE_APP_ABBR_NAME,
      VUE_APP_COMPANY: process.env.VUE_APP_COMPANY,
      VUE_APP_VERSION: process.env.VUE_APP_VERSION,
      VUE_APP_SCM_VERSION: process.env.VUE_APP_SCM_VERSION === 0 ? '' : 'hg' + process.env.VUE_APP_SCM_VERSION,
      updatePasswordDialog: false,
      // make sure they are synced with sms.api_role_menu tabls
      items: [
        { title: 'Manage Users', icon: 'people', route: 'users' },
        { title: 'Update password', icon: 'lock', button: true }
      ],
      right: null,
      drawer: true,
      scmVersionShown: false,
    }
  },
  methods: {
    ...mapActions(['setShowMiniMenu']),
  },
}
</script>

<style scoped>
.full-height {
  height: 100vh;
}

.menu-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.menu-button {
  width: 130px;
}
</style>
