<template>
  <div>
    Active Users
    <v-data-table
      :headers="headers"
      :items="activeUsers"
      :search="searchText"
      class="elevation-0"
    >
      <template v-slot:item="props">
        <tr>
          <td class="text-xs-left">{{ props.item.user_id }}</td>
          <td class="text-xs-left">
            {{ props.item.first_name }} {{ props.item.last_name }}
          </td>
          <td class="text-xs-left">{{ props.item.email }}</td>
          <td class="text-xs-left">{{ props.item.role }}</td>
          <td class="text-xs-left">{{ props.item.phone }}</td>
          <td class="text-xs-right">
            <CreateUserDialog mode="Edit" :user="props.item"/>
            |
            <v-btn
              color="primary"
              rounded
              small
              class="menu-button"
              @click="reset(props.item)"
              >Reset Password
            </v-btn>
            |
            <v-btn
              color="primary"
              rounded
              small
              dark
              class="menu-button"
              @click="deactivate(props.item)"
            >
              Deactivate
            </v-btn>
          </td>
          <td></td>
        </tr>
      </template>
      <template v-slot:header.create>
        <v-btn @click="createUserDialog = true">
          <CreateUserDialog :dialog.sync="createUserDialog"/>
        </v-btn>
      </template>
    </v-data-table>
    Inactive Users
    <v-data-table
      :headers="headers"
      :items="inactiveUsers"
      :search="searchText"
      class="elevation-0"
    >
      <template v-slot:headers="props">
        <tr>
          <th
            v-for="header in props.headers"
            :key="header.text"
          >
            <v-icon small>arrow_upward</v-icon>
            {{ header.text }}
          </th>
        </tr>
      </template>
      <template v-slot:item="props">
        <tr>
          <td class="text-xs-left">{{ props.item.id }}</td>
          <td class="text-xs-left">
            {{ props.item.first_name }} {{ props.item.last_name }}
          </td>
          <td class="text-xs-left">{{ props.item.email }}</td>
          <td class="text-xs-left">{{ props.item.role }}</td>
          <CreateUserDialog mode="Edit" :user="props.item"/>
          |
          <v-btn
            color="primary"
            rounded
            small
            dark
            class="menu-button"
            @click="activate(props.item)"
          >
            Activate</v-btn
          >
        </tr>
      </template>
    </v-data-table>
  </div>
</template>

<script>
// Shows the list of user returned from the API
import { mapActions } from 'vuex'
import CreateUserDialog from '../components/CreateUserDialog'

export default {
  name: 'UserList',
  components: {
    CreateUserDialog,
  },

  computed: {
    activeUsers: {
      get() {
        return this.users.filter(u => u.active == true)
      },
    },
    inactiveUsers: {
      get() {
        return this.users.filter(u => u.active == false)
      },
    },
    users: {
      get() {
        return this.$store.state.userList
      },
    },
    /*dataForExport: {
        get() {
          return this.users.map(p => ({
            'ID': p.id,
            'User': p.user,
            'Email': p.email,
            "Phone": p.phone
          }))
        }
      },*/
    searchText: {
      get() {
        return this.$store.state.searchText
      },
      set(value) {
        this.setSearchText(value)
      },
    },
    showExportMenu: {
      get() {
        return this.$store.state.showExportMenu
      },
      set(value) {
        this.$store.state.showExportMenu = value
      }
    }
  },
  data() {
    return {
      headers: [
        { text: 'ID', value: 'id', sortable: true },
        { text: 'Name', value: 'full_name', sortable: true },
        { text: 'Email', value: 'email', align: 'left', sortable: true },
        { text: 'Role', value: 'role', align: 'left' },
        { text: 'Actions', sortable: false },
        { text: 'Create', value: 'create', sortable: false, align: 'left' },
      ],
      createUserDialog: false
    }
  },
  methods: {
    ...mapActions([
      'downloadUsers',
      'setSearchText',
      'activateUser',
      'deactivateUser',
      'resetPassword',
    ]),

    activate(user) {
      this.activateUser(user)
        .then(() => {
          this.$toast.success(
            user.first_name +
              ' ' +
              user.last_name +
              ' has successfully been activated',
            { icon: 'done' }
          )
        })
        .catch(() => {
          this.$toast.error(
            user.first_name +
              ' ' +
              user.last_name +
              ' activation  was unsuccessfull',
            { icon: 'error' , showClose: true, timeout: 10000 }
          )
        })
    },
    deactivate(user) {
      this.deactivateUser(user)
        .then(() => {
          this.$toast.success(
            user.first_name +
              ' ' +
              user.last_name +
              ' has successfully been deactivated',
            { icon: 'done' }
          )
        })
        .catch(() => {
          this.$toast.error(
            user.first_name +
              ' ' +
              user.last_name +
              ' deactivation was unsuccessfull',
            { icon: 'error', showClose: true, timeout: 10000  }
          )
        })
    },
    reset(user) {
      this.resetPassword(user)
        .then(() => {
          this.$toast.success(
            'An updated password has been emailed to ' +
              user.first_name +
              ' ' +
              user.last_name,
            { icon: 'done' }
          )
        })
        .catch(() => {
          this.$toast.error(
            'Password reset for ' +
              user.first_name +
              ' ' +
              user.last_name +
              ' was unsuccessfull',
            { icon: 'error', showClose: true, timeout: 10000  }
          )
        })
    },
  },
  created() {
    this.setSearchText('')
    this.downloadUsers()
  },
}
</script>

<style>
.quick-view-background {
  background-color: #ececec;
}

.float-right {
  float: right;
}

.Offline {
  color: #cf202f;
  font-weight: bold;
}

.fault {
  color: #fe5000;
}
</style>
