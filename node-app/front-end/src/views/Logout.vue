<template>
  <v-container fluid fill-height class="full-height">
    <v-layout align-center justify-center>
      <v-flex xs12 sm8 md4>
        <h1>Logging out</h1>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  components: {},
  computed: {},
  data: () => ({
    valid: true,
    error: null,
  }),
  mounted() {
    this.logout()
    this.$router.push('/login')
  },
  methods: {
    ...mapActions(['logout']),
  },
}
</script>

<style>
.full-height {
  height: 100vh;
}
</style>
