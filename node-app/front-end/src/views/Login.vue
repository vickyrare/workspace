<template>
  <v-container fluid fill-height class="full-height">
    <v-layout align-center justify-center>
      <v-flex xs12 sm8 md4>
        <h1>{{VUE_APP_TITLE}}</h1>
        <h2>{{VUE_APP_COMPANY}}</h2>
        <v-card class="elevation-12">
          <v-form
              ref="form"
              v-model="valid"
              lazy-validation
              @submit.prevent="validate"
          >
          <v-card-text>
                  <v-alert
                    :value="true"
                    color="error"
                    icon="warning"
                    outlined
                    v-if="error"
                  >
                    {{error}}
                  </v-alert>

              <v-text-field
                id="login_email"
                v-model="email"
                :rules="emailRules"
                label="Email"
                required
              ></v-text-field>

              <v-text-field
                id="login_password"
                v-model="password"
                :rules="passwordRules"
                type="password"
                label="Password"
                required
              ></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn type="submit" block color="primary" :disabled="!valid">Login</v-btn>
          </v-card-actions>
          <v-alert class="caption"
            :value="true"
            color="info"
            icon="info"
            outlined
          >
          Contact an administrator if you have forgotten your password or your account has been disabled
          </v-alert>
          </v-form>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  import { mapActions } from "vuex"

  export default {
    components: {
    },
    computed: {
      email: {
        get() { return this.$store.state.email },
        set(value) { this.setEmail(value) },
      },
      password: {
        get() { return this.$store.state.password },
        set(value) { this.setPassword(value) },
      },
    },
    data: () => ({
      VUE_APP_TITLE: process.env.VUE_APP_TITLE,
      VUE_APP_COMPANY: process.env.VUE_APP_COMPANY,
      valid: true,
      error: null,
      passwordRules: [
        v => !!v || 'Password is required'
      ],
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+/.test(v) || 'Email must be a valid email'
      ]
    }),

    methods: {
      ...mapActions([
        'login',
        'getEmail',
        'setEmail',
        'getPassword',
        'setPassword'
      ]),
      validate () {
        if (this.$refs.form.validate()) {
          this.snackbar = true
          
          var vm = this
          this.login().then(() => {
              vm.error = null;
              this.$router.push(vm.$route.query.redirect? vm.$route.query.redirect : '/')
          }).catch(error => {
            vm.error = error
          })
        }
      },
      reset () {
        this.$refs.form.reset()
      },
      resetValidation () {
        this.$refs.form.resetValidation()
      }
    }
  }
</script>

<style>
.full-height {
  height: 100vh;
}
</style>
