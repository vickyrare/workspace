<template>
  <v-dialog
    v-model="localDialog"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
    @keydown.esc="closeDialog"
  >
    <template v-slot:activator="{ on }">
      <span color="primary" dark v-on="on">Update Password</span>
    </template>
    <v-card>
    <div>
      <v-toolbar color="black" dark fixed>
        <v-app-bar-nav-icon @click="closeDialog">
          <v-icon>close</v-icon>
        </v-app-bar-nav-icon>
        <v-toolbar-title>Update Password</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-btn dark text @click="onSave">Save</v-btn>
        </v-toolbar-items>
      </v-toolbar>
      <v-card style="padding-top: 40px;" text>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex xs8>
              <span class="headline">User Information</span>
              <v-text-field id="id" label="ID" v-model="id" readonly></v-text-field>
              <v-text-field
                id="name"
                label="Name"
                v-model="fullName"
                readonly
              ></v-text-field>
              <v-text-field
                id="email"
                label="Email"
                v-model="email"
                readonly
              ></v-text-field>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card>
      <v-card text>
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs8>
                <v-text-field
                  id="current_password"
                  label="Current password*"
                  type="password"
                  v-model="password.current_password"
                  :rules="currentPasswordRules"
                  required
                ></v-text-field>
                <v-text-field
                  id="new_password"
                  label="New password*"
                  type="password"
                  v-model="password.new_password"
                  :rules="newPasswordRules"
                  required
                >
                  <v-tooltip slot="append" bottom>
                    <template v-slot:activator="{ on }">
                      <v-icon v-on="on" color="primary" dark>help</v-icon>
                    </template>
                    <span>{{passwordPolicy}}</span>
                  </v-tooltip>
                </v-text-field>
                <v-text-field
                  id="confirm_password"
                  label="Confirm new password*"
                  type="password"
                  v-model="password.confirm_password"
                  :rules="confirmPasswordRules"
                  required
                ></v-text-field>
                <small>*indicates required field</small>
              </v-flex>
            </v-layout>
          </v-container>
        </v-form>
      </v-card>
    </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'UpdatePassword',
  props: {
    dialog: Boolean
  },
  components: {},
  watch: {
    dialog: function(newVal, oldVal) {
      this.localDialog = newVal
    },
  },
  computed: {
    id: {
      get() {
        return this.$store.state.id
      },
    },
    email: {
      get() {
        return this.$store.state.email
      },
    },
    fullName: {
      get() {
        return this.$store.state.firstName + ' ' + this.$store.state.lastName
      },
    },
  },
  data() {
    return {
      non_empty: false,
      localDialog: false,
      valid: true,
      password: {
        current_password: '',
        new_password: '',
        confirm_password: '',
      },
      passwordPolicy: 'Password policy: Password must be at least 8 characters long, and include at least one digit, at least one lower case character, at least one upper case character, at least one special character from !@#$%^&*()_+',
      currentPasswordRules: [v => !!v || 'Current password is required'],
      newPasswordRules: [v => !!v || 'New password is required'],
      confirmPasswordRules: [
        v =>
          (!!v && v) === this.password.new_password ||
          'Password does not match',
      ],
    }
  },
  methods: {
    ...mapActions(['updatePassword', 'getMessage', 'getSnackbar']),
    onSave() {
      if (this.$refs.form.validate()) {
        const password = {
          oldpassword: this.password.current_password,
          newpassword: this.password.new_password,
        }
        var vm = this
        this.updatePassword(password)
          .then(() => {
            this.$toast.success('Password updated successfully', {
              icon: 'done',
            })
            this.dialog = false
            this.reset()
          })
          .catch(error => {
            this.$toast.error(error, { 
              icon: 'error', showClose: true, timeout: 10000
            })
          })
      }
    },
    reset() {
      this.$refs.form.reset()
    },
    resetValidation() {
      this.$refs.form.resetValidation()
    },
    closeDialog() {
      this.localDialog = false
      this.$emit('update:dialog', this.localDialog)
      this.reset()
    },
  },
}
</script>

<style scoped>
.edit-group-button {
  width: 130px;
}

.handle {
  cursor: move !important;
  /*max-width: 28px;*/
}

.selected {
  background-color: lightblue;
}
</style>
