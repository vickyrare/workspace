<template>
  <v-main>
    <v-container class="px-9">
      <v-layout
              wrap
              align-start justify-center row fill-height
      >
      <v-flex xs12>
        <v-container pa-0>
          <v-layout col>
            <v-flex>
              <h1 class="pageTitle">Manage Users</h1>
            </v-flex>
            <v-flex>
              <UserSearch/>
            </v-flex>
          </v-layout>
        </v-container>
      </v-flex>
        <v-flex xs12>
          <UserList />
        </v-flex>
      </v-layout>
    </v-container>
  </v-main>
</template>

<script>
  import UserSearch from '../components/UserSearch'
  import UserList from '../components/UserList'

  export default {
    components: {
      UserSearch,
      UserList
    },
    data: () => ({
      items: [
        { title: 'Create User' },
        /*{ title: 'Edit Columns' },*/
        { title: 'Export' }
      ]
    })
  }
</script>
